package com.techsol;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class SiteServerStarter implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Site server application started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Site server application stopped");
    }
}
