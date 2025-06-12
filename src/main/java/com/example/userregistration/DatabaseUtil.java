package com.example.userregistration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class DatabaseUtil {
    private static final AtomicReference<String> url = new AtomicReference<>(
        "jdbc:mysql://localhost:3306/railway_crossing?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&autoReconnect=true");
    private static final AtomicReference<String> username = new AtomicReference<>("root");
    private static final AtomicReference<String> password = new AtomicReference<>("Root@1234");
    private static volatile boolean driverLoaded = false;
    
    static {
        // Try to load from properties file first
        try (InputStream input = DatabaseUtil.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                
                String dbUrl = prop.getProperty("db.url");
                String dbUser = prop.getProperty("db.username");
                String dbPass = prop.getProperty("db.password");
                
                if (dbUrl != null && !dbUrl.trim().isEmpty()) url.set(dbUrl);
                if (dbUser != null && !dbUser.trim().isEmpty()) username.set(dbUser);
                if (dbPass != null && !dbPass.trim().isEmpty()) password.set(dbPass);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load database.properties, using default configuration");
        }
        
        // Initialize the driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            driverLoaded = true;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Include it in your library path ");
            e.printStackTrace();
            throw new RuntimeException("Failed to load MySQL JDBC Driver", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        System.out.println("=== Database Connection Attempt ===");
        System.out.println("URL: " + url.get());
        System.out.println("Username: " + username.get());
        
        if (!driverLoaded) {
            String errorMsg = "Database driver not loaded. Check your configuration.";
            System.err.println("ERROR: " + errorMsg);
            throw new SQLException(errorMsg);
        }
        
        Connection conn = null;
        try {
            System.out.println("Attempting to connect to database...");
            conn = DriverManager.getConnection(url.get(), username.get(), password.get());
            
            // Test the connection
            try (Statement stmt = conn.createStatement()) {
                System.out.println("Connection established, testing with 'SELECT 1'...");
                stmt.execute("SELECT 1");
                System.out.println("Database connection test successful");
            }
            return conn;
        } catch (SQLException e) {
            String errorMsg = "Failed to create database connection: " + e.getMessage();
            System.err.println("ERROR: " + errorMsg);
            e.printStackTrace();
            
            if (conn != null) {
                try { 
                    conn.close(); 
                } catch (SQLException ex) { 
                    System.err.println("Error while closing connection: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            throw new SQLException(errorMsg, e);
        }
    }
    
    public static void init() {
        createUsersTable();
        createRailwayCrossingsTable();
        createFavoritesTable();
        createAdminsTable();
    }
    
    public static void createFavoritesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS user_favorites (" +
                   "id INT AUTO_INCREMENT PRIMARY KEY, " +
                   "user_id INT NOT NULL, " +
                   "crossing_id INT NOT NULL, " +
                   "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                   "UNIQUE KEY unique_favorite (user_id, crossing_id), " +
                   "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                   "FOREIGN KEY (crossing_id) REFERENCES railway_crossing(id) ON DELETE CASCADE" +
                   ")";
        
        try (Connection conn = getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void createAdminsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS admins (" +
                   "id INT AUTO_INCREMENT PRIMARY KEY, " +
                   "email VARCHAR(100) NOT NULL UNIQUE, " +
                   "password VARCHAR(255) NOT NULL, " +
                   "name VARCHAR(100) NOT NULL, " +
                   "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                   ")";
        
        try (Connection conn = getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            
            // Insert default admin if not exists
            String checkAdmin = "SELECT id FROM admins WHERE email = 'admin@railway.gov';";
            ResultSet rs = stmt.executeQuery(checkAdmin);
            if (!rs.next()) {
                // Default admin password is "admin123" (hashed with BCrypt)
                String hashedPassword = "$2a$10$N9qo8uLOickgx2ZMRZoMy.MQDq5phQY7iCQD7VpOpZ91rC6ma/4nK";
                String insertAdmin = "INSERT INTO admins (email, password, name) VALUES " +
                                   "('admin@railway.gov', '" + hashedPassword + "', 'System Admin')";
                stmt.executeUpdate(insertAdmin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                   "id INT AUTO_INCREMENT PRIMARY KEY, " +
                   "name VARCHAR(100) NOT NULL, " +
                   "email VARCHAR(100) UNIQUE NOT NULL, " +
                   "password VARCHAR(255) NOT NULL, " +
                   "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                   ")";
        
        try (Connection conn = getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void createRailwayCrossingsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS railway_crossing (" +
                   "id INT AUTO_INCREMENT PRIMARY KEY, " +
                   "name VARCHAR(100) NOT NULL, " +
                   "address TEXT NOT NULL, " +
                   "landmark VARCHAR(150), " +
                   "train_schedule TEXT, " +
                   "person_in_charge VARCHAR(100) NOT NULL, " +
                   "status VARCHAR(50) NOT NULL, " +
                   "photo LONGBLOB, " +
                   "created_by INT, " +
                   "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                   "updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP, " +
                   "FOREIGN KEY (created_by) REFERENCES admins(id) ON DELETE SET NULL" +
                   ")";
        
        try (Connection conn = getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void printBCryptHash(String plainPassword) {
        String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(plainPassword, org.mindrot.jbcrypt.BCrypt.gensalt());
        System.out.println("BCrypt hash for '" + plainPassword + "': " + hashed);
    }
}
