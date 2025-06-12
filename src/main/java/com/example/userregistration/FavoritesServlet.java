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
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

public class FavoritesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Gson gson = new Gson();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"User not logged in\"}");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        
        try {
            List<Crossing> favorites = getFavoriteCrossings(userId);
            response.getWriter().write(gson.toJson(favorites));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Error retrieving favorites: " + e.getMessage() + "\"}");
        }
    }
    
    private List<Crossing> getFavoriteCrossings(int userId) throws SQLException, ClassNotFoundException {
        List<Crossing> favorites = new ArrayList<>();
        String sql = "SELECT rc.* FROM railway_crossings rc " +
                    "JOIN user_favorites uf ON rc.id = uf.crossing_id " +
                    "WHERE uf.user_id = ? ORDER BY uf.created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Crossing crossing = new Crossing();
                    crossing.setId(rs.getInt("id"));
                    crossing.setName(rs.getString("name"));
                    crossing.setAddress(rs.getString("address"));
                    crossing.setLandmark(rs.getString("landmark"));
                    crossing.setTrainSchedule(rs.getString("train_schedule"));
                    crossing.setPlatformInCharge(rs.getString("platform_in_charge"));
                    crossing.setStatus(rs.getString("status"));
                    favorites.add(crossing);
                }
            }
        }
        
        return favorites;
    }
    
    // Inner class to represent a Crossing (matching the structure in DashboardServlet)
    public static class Crossing {
        private int id;
        private String name;
        private String address;
        private String landmark;
        private String trainSchedule;
        private String platformInCharge;
        private String status;
        
        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        
        public String getLandmark() { return landmark; }
        public void setLandmark(String landmark) { this.landmark = landmark; }
        
        public String getTrainSchedule() { return trainSchedule; }
        public void setTrainSchedule(String trainSchedule) { this.trainSchedule = trainSchedule; }
        
        public String getPlatformInCharge() { return platformInCharge; }
        public void setPlatformInCharge(String platformInCharge) { this.platformInCharge = platformInCharge; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
