package com.example.userregistration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB max file size
public class EditCrossingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in as admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminId") == null) {
            response.sendRedirect("adminLogin.jsp");
            return;
        }
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Invalid crossing ID");
            response.sendRedirect("adminDashboard.jsp");
            return;
        }
        
        try {
            int crossingId = Integer.parseInt(idParam);
            RailwayCrossing crossing = getCrossingById(crossingId);
            
            if (crossing != null) {
                request.setAttribute("crossing", crossing);
                request.getRequestDispatcher("editCrossing.jsp").forward(request, response);
            } else {
                session.setAttribute("errorMessage", "Railway crossing not found");
                response.sendRedirect("adminDashboard.jsp");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid crossing ID format");
            response.sendRedirect("adminDashboard.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error retrieving crossing details");
            response.sendRedirect("adminDashboard.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in as admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminId") == null) {
            response.sendRedirect("adminLogin.jsp");
            return;
        }
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Invalid crossing ID");
            response.sendRedirect("adminDashboard.jsp");
            return;
        }
        
        try {
            int crossingId = Integer.parseInt(idParam);
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String landmark = request.getParameter("landmark");
            String status = request.getParameter("status");
            String personInCharge = request.getParameter("personInCharge");
            String schedule = request.getParameter("schedule");
            
            // Handle file upload
            Part filePart = request.getPart("photo");
            InputStream fileContent = null;
            boolean hasNewPhoto = filePart != null && filePart.getSize() > 0;
            
            if (hasNewPhoto) {
                // Validate file type
                String fileName = filePart.getSubmittedFileName().toLowerCase();
                if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png")) {
                    session.setAttribute("errorMessage", "Only JPG, JPEG, and PNG files are allowed");
                    response.sendRedirect("edit-crossing?id=" + crossingId);
                    return;
                }
                fileContent = filePart.getInputStream();
            }
            
            // Update the database
            boolean success;
            if (hasNewPhoto) {
                success = updateCrossingWithPhoto(crossingId, name, address, landmark, status, personInCharge, schedule, fileContent);
            } else {
                success = updateCrossingWithoutPhoto(crossingId, name, address, landmark, status, personInCharge, schedule);
            }
            
            if (success) {
                session.setAttribute("successMessage", "Railway crossing updated successfully!");
                response.sendRedirect("view-crossing?id=" + crossingId);
            } else {
                session.setAttribute("errorMessage", "Failed to update railway crossing");
                response.sendRedirect("edit-crossing?id=" + crossingId);
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid crossing ID format");
            response.sendRedirect("adminDashboard.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error updating railway crossing: " + e.getMessage());
            response.sendRedirect("edit-crossing?id=" + idParam);
        }
    }
    
    private RailwayCrossing getCrossingById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM railway_crossings WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                RailwayCrossing crossing = new RailwayCrossing();
                crossing.setId(rs.getInt("id"));
                crossing.setName(rs.getString("name"));
                crossing.setAddress(rs.getString("address"));
                crossing.setLandmark(rs.getString("landmark"));
                crossing.setStatus(rs.getString("status"));
                crossing.setPersonInCharge(rs.getString("person_in_charge"));
                crossing.setTrainSchedule(rs.getString("train_schedule"));
                crossing.setPhoto(rs.getBytes("photo"));
                return crossing;
            }
        }
        return null;
    }
    
    private boolean updateCrossingWithPhoto(int id, String name, String address, String landmark, 
                                          String status, String personInCharge, String schedule, 
                                          InputStream photo) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE railway_crossings SET name = ?, address = ?, landmark = ?, "
                  + "status = ?, person_in_charge = ?, train_schedule = ?, photo = ?, updated_at = CURRENT_TIMESTAMP "
                  + "WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, landmark);
            pstmt.setString(4, status);
            pstmt.setString(5, personInCharge);
            pstmt.setString(6, schedule);
            pstmt.setBlob(7, photo);
            pstmt.setInt(8, id);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    private boolean updateCrossingWithoutPhoto(int id, String name, String address, String landmark, 
                                              String status, String personInCharge, String schedule) 
                                              throws SQLException, ClassNotFoundException {
        String sql = "UPDATE railway_crossings SET name = ?, address = ?, landmark = ?, "
                  + "status = ?, person_in_charge = ?, train_schedule = ?, updated_at = CURRENT_TIMESTAMP "
                  + "WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, landmark);
            pstmt.setString(4, status);
            pstmt.setString(5, personInCharge);
            pstmt.setString(6, schedule);
            pstmt.setInt(7, id);
            
            return pstmt.executeUpdate() > 0;
        }
    }
}
