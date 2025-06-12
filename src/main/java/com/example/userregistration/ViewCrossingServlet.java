package com.example.userregistration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewCrossingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminId") == null) {
            response.sendRedirect("adminLogin.jsp");
            return;
        }
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("manageCrossings.jsp");
            return;
        }
        
        try {
            int crossingId = Integer.parseInt(idParam);
            RailwayCrossing crossing = getCrossingById(crossingId);
            
            if (crossing != null) {
                request.setAttribute("crossing", crossing);
                request.getRequestDispatcher("/viewCrossing.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Railway crossing not found.");
                response.sendRedirect("manageCrossings.jsp");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid crossing ID format.");
            response.sendRedirect("manageCrossings.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error retrieving crossing details: " + e.getMessage());
            response.sendRedirect("manageCrossings.jsp");
        }
    }
    
    private RailwayCrossing getCrossingById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT rc.*, a.name as admin_name " +
                   "FROM railway_crossings rc " +
                   "LEFT JOIN admins a ON rc.created_by = a.id " +
                   "WHERE rc.id = ?";
        
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
                crossing.setTrainSchedule(rs.getString("train_schedule"));
                crossing.setPersonInCharge(rs.getString("person_in_charge"));
                crossing.setStatus(rs.getString("status"));
                crossing.setCreatedAt(rs.getTimestamp("created_at"));
                crossing.setUpdatedAt(rs.getTimestamp("updated_at"));
                crossing.setCreatedBy(rs.getInt("created_by"));
                
                // Handle photo if needed
                if (rs.getBytes("photo") != null) {
                    crossing.setPhoto(rs.getBytes("photo"));
                }
                
                return crossing;
            }
            
            return null;
        }
    }
}
