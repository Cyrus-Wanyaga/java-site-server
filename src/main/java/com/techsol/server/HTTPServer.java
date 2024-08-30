package com.techsol.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.techsol.metrics.TrafficMonitor;

public class HTTPServer {
    private final String rootDir;
    private final int port;
    private HttpServer server;

    public HTTPServer(String rootDir, int port) {
        this.rootDir = rootDir;
        this.port = port;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new FileHandler());
        server.setExecutor(Executors.newFixedThreadPool(4));
        server.start();
        System.out.println("Started server on port " + port);
    }

    private class FileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().getPath();
            String methodName = exchange.getRequestMethod();
            String ipAddress = exchange.getRemoteAddress().getAddress().getHostAddress();
            File file = new File(rootDir, requestPath);
            System.out.printf("{\n\t\"request\":\"%s\",\n\t\"filePath\":\"%s\"\n}\n", requestPath,
                    file.getAbsolutePath());

            if (file.isDirectory()) {
                System.out.println("The file is a directory");
                file = new File(file, "index.html");
            }

            if (!file.exists()) {
                String response = "404 (Not Found)\n";
                TrafficMonitor.logErrors(methodName, "Page Not Found", ipAddress, requestPath);
                exchange.sendResponseHeaders(404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                String contentType = Files.probeContentType(file.toPath());
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.sendResponseHeaders(200, file.length());
                try (OutputStream os = exchange.getResponseBody();
                        FileInputStream fs = new FileInputStream(file)) {
                    final byte[] buffer = new byte[0x10000];
                    int count;
                    while ((count = fs.read(buffer)) >= 0) {
                        os.write(buffer, 0, count);
                    }
                }
            }

            TrafficMonitor.logPageVisit(methodName, requestPath, ipAddress);
        }
    }
}
