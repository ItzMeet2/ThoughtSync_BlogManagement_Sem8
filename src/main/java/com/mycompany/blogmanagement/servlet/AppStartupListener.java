package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.util.JPAUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Starting up BlogManagement application...");
        // This will trigger the static block or initialization method in JPAUtil
        JPAUtil.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Shutting down BlogManagement application...");
        // Gracefully close the EntityManagerFactory to prevent ClassLoader leaks during redeployments
        JPAUtil.close();
    }
}
