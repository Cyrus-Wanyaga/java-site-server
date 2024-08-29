package com.techsol.metrics;

import java.io.IOException;

import io.prometheus.client.Counter;
import io.prometheus.client.exporter.HTTPServer;

public class TrafficMonitor {
    static final Counter pageVisits = Counter.build().name("page_visits_total").help("Total page visits.")
            .labelNames("methodName", "endpoint", "ipAddress").register();

    static final Counter errors = Counter.build().name("errors_total").help("Total number of errors.")
            .labelNames("methodName", "errorType", "ipAddress", "endpoint").register();

    public static void init() throws IOException {
        HTTPServer server = new HTTPServer(8085);
    }

    public static void logPageVisit(String methodName, String endpoint, String ipAddress) {
        pageVisits.labels(methodName, endpoint, ipAddress).inc();
    }

    public static void logErrors(String methodName, String errorType, String ipAddress, String endpoint) {
        errors.labels(methodName, errorType, ipAddress, endpoint).inc();
    }
}
