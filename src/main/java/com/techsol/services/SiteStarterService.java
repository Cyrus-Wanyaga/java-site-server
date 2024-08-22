package com.techsol.services;

import com.techsol.server.HTTPServer;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@ApplicationScoped
public class SiteStarterService {
    private static final String SITES_DIR = "sites";
    private String siteDir;

    public boolean isPortAvailable(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean uploadFiles(InputStream inputStream, String appName) {
        File sitesDir = new File(SITES_DIR);
        if (!sitesDir.exists()) {
            if (sitesDir.mkdirs()) {
                System.out.println("Created " + sitesDir.getAbsolutePath() + " successfully");
            } else {
                System.out.println("Failed to create " + sitesDir.getAbsolutePath());
                return false;
            }
        } else {
            System.out.println("Sites directory already exists");
        }

        siteDir = SITES_DIR + File.separator + appName;
        File directory = new File(siteDir);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.out.println("Failed to create " + directory.getAbsolutePath());
                return false;
            } else {
                System.out.println("Created " + directory.getAbsolutePath() + " successfully");
            }
        } else {
            System.out.println("Site directory already exists");
            return true;
        }

        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry;

        try {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String entryName = zipEntry.getName();

                // Remove the first directory from the entry name
                String normalizedEntryName = entryName.substring(entryName.indexOf("/") + 1);

                Path filePath = Paths.get(siteDir, normalizedEntryName);
                if (!zipEntry.isDirectory()) {
                    Files.createDirectories(filePath.getParent());
                    Files.copy(zipInputStream, filePath);
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    public String startServer(int port) {
        try {
            System.out.println("Initiating server with site dir : " + siteDir + File.separator + "build");
            HTTPServer httpServer = new HTTPServer(siteDir, port);
            httpServer.start();
            return "Started server successfully";
        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
    }
}
