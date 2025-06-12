package com.example.userregistration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/testadmin")
public class TestAdminUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h2>Admin User Test</h2>");
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            out.println("<p>Database connection successful!</p>");
            
            // Check if admin user exists
            String sql = "SELECT id, name, email, password FROM users WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "admin@example.com");
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    out.println("<p>Admin user found:</p>");
                    out.println("<ul>");
                    out.println("<li>ID: " + rs.getInt("id") + "</li>");
                    out.println("<li>Name: " + rs.getString("name") + "</li>");
                    out.println("<li>Email: " + rs.getString("email") + "</li>");
                    out.println("<li>Password hash: " + rs.getString("password") + "</li>");
                    out.println("</ul>");
                } else {
                    out.println("<p>Admin user not found in the database.</p>");
                }
            }
            
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
        
        out.println("</body></html>");
    }
}
