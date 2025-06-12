package com.example.userregistration;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDBConnection {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/railway_crossing?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "Root@1234";
        
        try {
            System.out.println("Connecting to database: " + jdbcUrl);
            Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
            System.out.println("Connection successful!");
            conn.close();
        } catch (Exception e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        }
    }
}
