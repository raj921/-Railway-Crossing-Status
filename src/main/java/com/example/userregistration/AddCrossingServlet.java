package com.example.userregistration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/addCrossing")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 5,      // 5 MB
    maxRequestSize = 1024 * 1024 * 10    // 10 MB
)
public class AddCrossingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is authenticated
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect("adminLogin.jsp");
            return;
        }

        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String landmark = request.getParameter("landmark");
        String trainSchedule = request.getParameter("trainSchedule");
        String personInCharge = request.getParameter("personInCharge");
        String status = request.getParameter("status");

        // Validate required fields
        if (name == null || name.trim().isEmpty() || 
            address == null || address.trim().isEmpty() ||
            landmark == null || landmark.trim().isEmpty() ||
            trainSchedule == null || trainSchedule.trim().isEmpty() ||
            personInCharge == null || personInCharge.trim().isEmpty() ||
            status == null) {
            session.setAttribute("errorMessage", "All fields are required");
            response.sendRedirect("addCrossing.jsp");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO railway_crossing (name, address, landmark, train_schedule, person_in_charge, status, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)", 
                Statement.RETURN_GENERATED_KEYS)) {
                
            stmt.setString(1, name.trim());
            stmt.setString(2, address.trim());
            stmt.setString(3, landmark.trim());
            stmt.setString(4, trainSchedule.trim());
            stmt.setString(5, personInCharge.trim());
            stmt.setString(6, status);
            stmt.setInt(7, (Integer) session.getAttribute("adminId"));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating crossing failed, no rows affected.");
            }
            
            session.setAttribute("successMessage", "Railway crossing added successfully!");
            
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error adding railway crossing: " + e.getMessage());
            response.sendRedirect("addCrossing.jsp");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "An unexpected error occurred");
            response.sendRedirect("addCrossing.jsp");
            return;
        }
        
        response.sendRedirect("adminDashboard.jsp");
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirect to the add crossing form
        response.sendRedirect("addCrossing.jsp");
    }
}
