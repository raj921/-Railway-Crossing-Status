package com.example.userregistration;

import com.example.userregistration.model.Crossing;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/login.jsp");
            return;
        }
        
        List<Crossing> crossings = new ArrayList<>();
        Integer userId = (session != null && session.getAttribute("userId") != null) ? 
                        (Integer) session.getAttribute("userId") : null;
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            // First, get all crossings
            String sql = "SELECT * FROM railway_crossings ORDER BY name";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    Crossing crossing = new Crossing();
                    int crossingId = rs.getInt("id");
                    crossing.setId(crossingId);
                    crossing.setName(rs.getString("name"));
                    crossing.setAddress(rs.getString("address"));
                    crossing.setLandmark(rs.getString("landmark"));
                    crossing.setTrainSchedule(rs.getTime("train_schedule") != null ? 
                                           rs.getTime("train_schedule").toString() : "");
                    crossing.setPersonInCharge(rs.getString("person_in_charge"));
                    crossing.setStatus(rs.getString("status"));
                    
                    // Check if this crossing is a favorite for the current user
                    if (userId != null) {
                        boolean isFavorite = isFavorite(conn, userId, crossingId);
                        crossing.setIsFavorite(isFavorite);
                    }
                    
                    crossings.add(crossing);
                }
            }
            
            request.setAttribute("crossings", crossings);
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error retrieving railway crossings: " + e.getMessage());
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
        }
    }
    
    private boolean isFavorite(Connection conn, int userId, int crossingId) throws Exception {
        String sql = "SELECT COUNT(*) FROM user_favorites WHERE user_id = ? AND crossing_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, crossingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    
}
