package com.techsol.metrics;

import java.io.IOException;

import io.prometheus.client.Counter;
import io.prometheus.client.exporter.HTTPServer;

public class TrafficMonitor {
    static final Counter pageVisits = Counter.build().name("page_visits_total").help("Total page visits.")
            .labelNames("httpMethod", "endpoint", "ipAddress").register();

    static final Counter errors = Counter.build().name("errors_total").help("Total number of errors.")
            .labelNames("httpMethod", "errorType", "ipAddress", "endpoint").register();

            static final Counter apiRequests = Counter.build().name("api_requests").help("Total number of requests.")
            .labelNames("methodName", "httpMethod", "endpoint", "responseCode").register();

    public static void init() throws IOException {
        HTTPServer server = new HTTPServer(8085);
    }

    public static void logPageVisit(String httpMethod, String endpoint, String ipAddress) {
        pageVisits.labels(httpMethod, endpoint, ipAddress).inc();
    }

    public static void logErrors(String httpMethod, String errorType, String ipAddress, String endpoint) {
        errors.labels(httpMethod, errorType, ipAddress, endpoint).inc();
    }

    public static void logAPIRequests(String methodName, String httpMethod, String endpoint, String responseCode) {
        apiRequests.labels(methodName, httpMethod, endpoint, responseCode).inc();
    }
}
