package com.example.userregistration;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class UpdateAdminPassword {
    public static void main(String[] args) {
        String email = "admin@example.com";
        String newPassword = "admin123";
        
        // Generate a new BCrypt hash
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        
        System.out.println("Generated hash for 'admin123': " + hashedPassword);
        
        // Update the database
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Successfully updated admin password");
            } else {
                System.out.println("No user found with email: " + email);
            }
            
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
