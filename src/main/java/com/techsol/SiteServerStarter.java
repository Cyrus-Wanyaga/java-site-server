package com.techsol;

import java.io.IOException;

import com.techsol.metrics.TrafficMonitor;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class SiteServerStarter implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Site server application started");
        try {
            System.out.println("Attempting to start the metrics REST server");
            TrafficMonitor.init();
            System.out.println("Started metrics REST server successfully");
        } catch (IOException e) {
            System.out.println("Failed to start metrics REST server");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Site server application stopped");
    }
}
