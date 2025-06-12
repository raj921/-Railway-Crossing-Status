package com.example.userregistration;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ReadListener;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import org.mindrot.jbcrypt.BCrypt;

class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
    private byte[] cachedBody;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = requestInputStream.readAllBytes();
    }

    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream, StandardCharsets.UTF_8));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return byteArrayInputStream.read(b, off, len);
            }

            @Override
            public int available() throws IOException {
                return byteArrayInputStream.available();
            }

            @Override
            public void close() throws IOException {
                byteArrayInputStream.close();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException("Not implemented");
            }
        };
    }
    
    public String getBody() {
        return new String(cachedBody, StandardCharsets.UTF_8);
    }
}

public class AdminLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("\n" + LocalDateTime.now() + " === AdminLoginServlet INITIALIZED ===");
        System.out.println("Servlet Name: " + getServletName());
        System.out.println("Servlet Info: " + getServletInfo());
        System.out.println("Context Path: " + getServletContext().getContextPath());
        System.out.println("Server Info: " + getServletContext().getServerInfo());
        System.out.println("Servlet API Version: " + getServletContext().getMajorVersion() + "." + getServletContext().getMinorVersion());
        
        // Log all servlet mappings
        System.out.println("\n=== Servlet Mappings ===");
        if (getServletContext() != null) {
            try {
                java.util.Map<String, ? extends jakarta.servlet.ServletRegistration> servlets = getServletContext().getServletRegistrations();
                for (java.util.Map.Entry<String, ? extends jakarta.servlet.ServletRegistration> entry : servlets.entrySet()) {
                    System.out.println("Servlet: " + entry.getKey() + " -> " + entry.getValue().getClassName());
                    for (String mapping : entry.getValue().getMappings()) {
                        System.out.println("  Maps to: " + mapping);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error getting servlet mappings: " + e.getMessage());
            }
        }
        
        // Ensure default admin exists on startup
        try {
            System.out.println("\nEnsuring default admin exists...");
            ensureDefaultAdminExists();
            System.out.println("Default admin check completed");
        } catch (Exception e) {
            System.err.println("Error ensuring default admin exists: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public String getServletInfo() {
        return "AdminLoginServlet handles admin authentication";
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Set character encoding for request
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        // Log that we've entered the doPost method
        System.out.println("\n" + LocalDateTime.now() + " === AdminLoginServlet.doPost() called ===");
        System.out.println("Request URL: " + request.getRequestURL().toString());
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Context Path: " + request.getContextPath());
        System.out.println("Servlet Path: " + request.getServletPath());
        System.out.println("Path Info: " + request.getPathInfo());
        System.out.println("Request method: " + request.getMethod());
        System.out.println("Content type: " + request.getContentType());
        
        // Log all request parameters
        System.out.println("\n=== All Request Parameters ===");
        request.getParameterMap().forEach((key, values) -> {
            System.out.print(key + " = ");
            if ("password".equals(key)) {
                System.out.println("[PROTECTED]");
            } else {
                System.out.println(String.join(", ", values));
            }
        });
        
        // Get form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Log the received parameters
        System.out.println("\n=== Form Parameters ===");
        System.out.println("Username: " + (username != null ? username : "null"));
        System.out.println("Password: " + (password != null ? "[PROTECTED]" : "null"));
        
        // Log session info
        HttpSession sessionInfo = request.getSession(false);
        System.out.println("\n=== Session Info ===");
        System.out.println("Session ID: " + (sessionInfo != null ? sessionInfo.getId() : "No session"));
        if (sessionInfo != null) {
            System.out.println("Session is new: " + sessionInfo.isNew());
            System.out.println("Session attributes: " + java.util.Collections.list(sessionInfo.getAttributeNames()));
        }
        
        // Check if username and password are not empty
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println("Error: Username or password is empty");
            System.out.println("Username empty: " + (username == null || username.trim().isEmpty()));
            System.out.println("Password empty: " + (password == null || password.trim().isEmpty()));
            
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/adminLogin.jsp").forward(request, response);
            return;
        }
        
        String sql = "SELECT id, name, password, email FROM admins WHERE email = ?";
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            System.out.println("Database connection established successfully");
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                System.out.println("Executing SQL: " + sql + " with username: " + username);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int adminId = rs.getInt("id");
                        String adminName = rs.getString("name");
                        String dbEmail = rs.getString("email");
                        String hashedPassword = rs.getString("password");
                        
                        System.out.println("Found admin record:");
                        System.out.println("  ID: " + adminId);
                        System.out.println("  Name: " + adminName);
                        System.out.println("  Email: " + dbEmail);
                        System.out.println("  Stored hash: " + hashedPassword);
                        
                        System.out.println("Verifying password...");
                        boolean passwordMatches = BCrypt.checkpw(password, hashedPassword);
                        System.out.println("Password verification result: " + passwordMatches);
                        
                        if (passwordMatches) {
                            // Login successful
                            System.out.println("Login successful for admin: " + username);
                            // Get or create session
                            HttpSession session = request.getSession(true);
                            session.setAttribute("adminId", adminId);
                            session.setAttribute("admin", username);
                            session.setAttribute("adminName", adminName);
                            session.setMaxInactiveInterval(30 * 60); // 30 minutes
                            session.setAttribute("isAdmin", true);
                            
                            // Invalidate any existing user session
                            session.removeAttribute("userId");
                            session.removeAttribute("userName");
                            
                            System.out.println("Login successful, redirecting to admin dashboard");
                            System.out.println("Session attributes after login: " + java.util.Collections.list(session.getAttributeNames()));
                            
                            // Use context-relative URL
                            String contextPath = request.getContextPath();
                            response.sendRedirect(contextPath + "/adminDashboard.jsp");
                            return;
                        } else {
                            System.out.println("Password verification failed");
                            request.setAttribute("error", "Invalid username or password");
                            request.getRequestDispatcher("/adminLogin.jsp").forward(request, response);
                        }
                    } else {
                        System.out.println("No admin found with username: " + username);
                        request.setAttribute("error", "Invalid username or password");
                        request.getRequestDispatcher("/adminLogin.jsp").forward(request, response);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "An error occurred. Please try again.");
            request.getRequestDispatcher("/adminLogin.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Log the start of the doGet method with timestamp
        System.out.println("\n" + LocalDateTime.now() + " === AdminLoginServlet.doGet() START ===");
        System.out.println("Request URL: " + request.getRequestURL().toString());
        System.out.println("Context Path: " + request.getContextPath());
        System.out.println("Servlet Path: " + request.getServletPath());
        System.out.println("Path Info: " + request.getPathInfo());
        System.out.println("Request Method: " + request.getMethod());
        
        // Log all request parameters
        System.out.println("\n=== Request Parameters ===");
        request.getParameterMap().forEach((key, values) -> {
            System.out.println(key + " = " + String.join(", ", values));
        });
        
        // Check if this is a test request
        if (request.getRequestURI() != null && request.getRequestURI().endsWith("/test")) {
            handleTestRequest(request, response);
            return;
        }
        
        // If already logged in as admin, redirect to dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("adminId") != null) {
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/adminDashboard.jsp");
            return;
        }
        
        // Ensure default admin exists
        ensureDefaultAdminExists();
        
        // Forward to the login page
        System.out.println("Forwarding to /adminLogin.jsp");
        request.getRequestDispatcher("/adminLogin.jsp").forward(request, response);
    }
    
    /**
     * Handles test requests for debugging purposes
     */
    private void handleTestRequest(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        // Create a local final variable to hold the writer
        final PrintWriter writer = response.getWriter();
        
        try {
            // Write test endpoint information
            writer.println("AdminLoginServlet Test Endpoint");
            writer.println("Time: " + LocalDateTime.now());
            writer.println("Request URL: " + request.getRequestURL());
            writer.println("Context Path: " + request.getContextPath());
            writer.println("Servlet Path: " + request.getServletPath());
            writer.println("Path Info: " + request.getPathInfo());
            
            // Log request headers
            writer.println("\n=== Request Headers ===");
            request.getHeaderNames().asIterator()
                .forEachRemaining(headerName -> 
                    writer.println(headerName + ": " + request.getHeader(headerName)));
            
            // Log request parameters
            writer.println("\n=== Request Parameters ===");
            request.getParameterMap().forEach((key, values) -> 
                writer.println(key + " = " + String.join(", ", values)));
            
            // Log session info
            writer.println("\n=== Session Info ===");
            HttpSession session = request.getSession(false);
            if (session != null) {
                writer.println("Session ID: " + session.getId());
                writer.println("Creation Time: " + session.getCreationTime());
                writer.println("Last Accessed Time: " + session.getLastAccessedTime());
                writer.println("Max Inactive Interval: " + session.getMaxInactiveInterval() + " seconds");
                
                // Log session attributes
                writer.println("\n=== Session Attributes ===");
                session.getAttributeNames().asIterator()
                    .forEachRemaining(attrName -> 
                        writer.println(attrName + " = " + session.getAttribute(attrName)));
            } else {
                writer.println("No active session");
            }
            
            // Log server info
            writer.println("\n=== Server Info ===");
            writer.println("Server Name: " + request.getServerName());
            writer.println("Server Port: " + request.getServerPort());
            writer.println("Remote Addr: " + request.getRemoteAddr());
            writer.println("Remote Host: " + request.getRemoteHost());
            writer.println("Remote User: " + request.getRemoteUser());
            writer.println("Auth Type: " + request.getAuthType());
            
        } catch (Exception e) {
            System.err.println("Error in test endpoint: " + e.getMessage());
            e.printStackTrace();
            try {
                if (!response.isCommitted()) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                        "Error processing test request: " + e.getMessage());
                }
            } catch (IOException ioe) {
                System.err.println("Failed to send error response: " + ioe.getMessage());
            }
        }
        // Note: We don't close the writer as it's managed by the container
    }
    
    private void ensureDefaultAdminExists() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM admins WHERE email = 'admin@example.com'")) {
            
            // Check if admin user exists
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt("count") == 0) {
                // Create default admin user if not exists
                String hashedPassword = BCrypt.hashpw("admin123", BCrypt.gensalt());
                try (PreparedStatement pstmt2 = conn.prepareStatement(
                        "INSERT INTO admins (name, email, password) VALUES (?, ?, ?)")) {
                    pstmt2.setString(1, "Admin User");
                    pstmt2.setString(2, "admin@example.com");
                    pstmt2.setString(3, hashedPassword);
                    pstmt2.executeUpdate();
                    System.out.println("Created default admin user with email: admin@example.com");
                }
            } else {
                System.out.println("Admin user already exists with email: admin@example.com");
            }
        } catch (SQLException e) {
            System.err.println("Error ensuring default admin exists: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
