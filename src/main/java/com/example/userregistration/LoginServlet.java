package com.example.userregistration;

import java.io.IOException;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT id, name, email FROM users WHERE email = ? AND password = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, hashPassword(password));
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Login successful
                        HttpSession session = request.getSession();
                        session.setAttribute("userId", rs.getInt("id"));
                        session.setAttribute("userName", rs.getString("name"));
                        session.setAttribute("userEmail", rs.getString("email"));
                        
                        // Get the context path and use it in the redirect
                        String contextPath = request.getContextPath();
                        // Always redirect to the context path + dashboard
                        response.sendRedirect(contextPath + "/dashboard");
                    } else {
                        // Login failed
                        String contextPath = request.getContextPath();
                        response.sendRedirect(contextPath + "/login.jsp?error=invalid_credentials");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/login.jsp?error=server_error");
        }
    }
    
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
