package com.example.userregistration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Enumeration;

public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        // Log the request
        System.out.println("\n" + LocalDateTime.now() + " === TestServlet.doGet() called ===");
        System.out.println("Request URL: " + request.getRequestURL().toString());
        System.out.println("Context Path: " + request.getContextPath());
        System.out.println("Servlet Path: " + request.getServletPath());
        System.out.println("Path Info: " + request.getPathInfo());
        
        try (PrintWriter out = response.getWriter()) {
            out.println("TestServlet is working!");
            out.println("Method: " + request.getMethod());
            out.println("Time: " + LocalDateTime.now());
            out.println("Request URL: " + request.getRequestURL());
            out.println("Context Path: " + request.getContextPath());
            out.println("Servlet Path: " + request.getServletPath());
            out.println("Path Info: " + request.getPathInfo());
            
            // Log request headers
            out.println("\nRequest Headers:");
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                out.println(headerName + ": " + request.getHeader(headerName));
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        // Log the request
        System.out.println("\n" + LocalDateTime.now() + " === TestServlet.doPost() called ===");
        System.out.println("Request URL: " + request.getRequestURL().toString());
        System.out.println("Context Path: " + request.getContextPath());
        System.out.println("Servlet Path: " + request.getServletPath());
        System.out.println("Path Info: " + request.getPathInfo());
        
        // Log request parameters
        System.out.println("Request Parameters:");
        request.getParameterMap().forEach((key, values) -> {
            System.out.println(key + " = " + String.join(", ", values));
        });
        
        try (PrintWriter out = response.getWriter()) {
            out.println("TestServlet POST is working!");
            out.println("Method: " + request.getMethod());
            out.println("Time: " + LocalDateTime.now());
            out.println("Request URL: " + request.getRequestURL());
            out.println("Context Path: " + request.getContextPath());
            out.println("Servlet Path: " + request.getServletPath());
            out.println("Path Info: " + request.getPathInfo());
            
            // Log request parameters in response
            out.println("\nRequest Parameters:");
            request.getParameterMap().forEach((key, values) -> {
                out.println(key + " = " + String.join(", ", values));
            });
        }
    }
}
