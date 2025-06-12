package com.example.userregistration;

import java.io.IOException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            // Initialize the database tables
            DatabaseUtil.init();
            userDAO = new UserDAO();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize database", e);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Create user object
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        
        try {
            // Check if email already exists
            if (userDAO.emailExists(email)) {
                String contextPath = request.getContextPath();
                response.sendRedirect(contextPath + "/register.jsp?error=email_exists");
                return;
            }
            
            // Register user
            if (userDAO.registerUser(user)) {
                // Registration successful, redirect to login page
                String contextPath = request.getContextPath();
                response.sendRedirect(contextPath + "/login.jsp?registered=true");
            } else {
                // Registration failed
                String contextPath = request.getContextPath();
                response.sendRedirect(contextPath + "/register.jsp?error=registration_failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/register.jsp?error=server_error");
        }
    }
}
