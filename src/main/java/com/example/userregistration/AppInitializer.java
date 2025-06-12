package com.example.userregistration;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Initialize database tables
            DatabaseUtil.init();
            System.out.println("Database tables initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing database tables:");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup resources if needed
    }
}
