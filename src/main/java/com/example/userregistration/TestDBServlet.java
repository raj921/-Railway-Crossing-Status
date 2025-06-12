package com.example.userregistration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/testdb")
public class TestDBServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("=== Database Connection Test ===");
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            out.println("✅ Successfully connected to the database!");
            
            // Test admin user
            out.println("\n=== Testing Admin User ===");
            String sql = "SELECT id, email, name, password FROM admins WHERE email = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "admin@railway.gov");
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        out.println("✅ Found admin user: " + rs.getString("email"));
                        out.println("   Name: " + rs.getString("name"));
                        out.println("   Password hash: " + rs.getString("password"));
                        
                        // Test password verification
                        String storedHash = rs.getString("password");
                        boolean passwordMatches = BCrypt.checkpw("admin123", storedHash);
                        out.println("   Password 'admin123' matches hash: " + passwordMatches);
                    } else {
                        out.println("❌ No admin user found with email: admin@railway.gov");
                    }
                }
            }
            
        } catch (Exception e) {
            out.println("❌ Error: " + e.getMessage());
            e.printStackTrace(out);
        }
    }
}
