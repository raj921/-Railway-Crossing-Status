package com.example.userregistration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in as admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminId") == null) {
            response.sendRedirect(request.getContextPath() + "/adminLogin.jsp");
            return;
        }
        
        try {
            // Fetch all railway crossings from the database
            List<RailwayCrossing> crossings = getAllCrossings();
            
            // Set the crossings in request attribute
            request.setAttribute("crossings", crossings);
            
            // Forward to the admin dashboard JSP
            request.getRequestDispatcher("/adminDashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading crossings: " + e.getMessage());
            request.getRequestDispatcher("/adminDashboard.jsp").forward(request, response);
        }
    }
    
    private List<RailwayCrossing> getAllCrossings() throws SQLException, ClassNotFoundException {
        List<RailwayCrossing> crossings = new ArrayList<>();
        
        String sql = "SELECT * FROM railway_crossing ORDER BY name ASC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                RailwayCrossing crossing = new RailwayCrossing();
                crossing.setId(rs.getInt("id"));
                crossing.setName(rs.getString("name"));
                crossing.setAddress(rs.getString("address"));
                crossing.setLandmark(rs.getString("landmark"));
                crossing.setTrainSchedule(rs.getString("train_schedule"));
                crossing.setPersonInCharge(rs.getString("person_in_charge"));
                crossing.setStatus(rs.getString("status"));
                crossing.setCreatedBy(rs.getInt("created_by"));
                
                // Add the crossing to the list
                crossings.add(crossing);
            }
        }
        
        return crossings;
    }
}
