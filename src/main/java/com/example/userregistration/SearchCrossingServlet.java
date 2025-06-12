package com.example.userregistration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

public class SearchCrossingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String searchTerm = request.getParameter("term");
        List<Crossing> crossings = new ArrayList<>();

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            // If search term is empty, return all crossings
            crossings = getAllCrossings();
        } else {
            // Search for crossings matching the search term
            crossings = searchCrossings(searchTerm);
        }

        // Convert crossings list to JSON and send as response
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(crossings);
        response.getWriter().write(jsonResponse);
    }

    private List<Crossing> searchCrossings(String searchTerm) {
        List<Crossing> crossings = new ArrayList<>();
        String sql = "SELECT * FROM railway_crossings WHERE name LIKE ? ORDER BY name";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + searchTerm + "%");
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
                    crossings.add(crossing);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Log the error or handle it appropriately
            System.err.println("Error searching crossings: " + e.getMessage());
        }
        
        return crossings;
    }
    
    private List<Crossing> getAllCrossings() {
        List<Crossing> crossings = new ArrayList<>();
        String sql = "SELECT * FROM railway_crossings ORDER BY name";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Crossing crossing = new Crossing();
                crossing.setId(rs.getInt("id"));
                crossing.setName(rs.getString("name"));
                crossing.setAddress(rs.getString("address"));
                crossing.setLandmark(rs.getString("landmark"));
                crossing.setTrainSchedule(rs.getString("train_schedule"));
                crossing.setPlatformInCharge(rs.getString("platform_in_charge"));
                crossing.setStatus(rs.getString("status"));
                crossings.add(crossing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Log the error or handle it appropriately
            System.err.println("Error getting all crossings: " + e.getMessage());
        }
        
        return crossings;
    }
    
    // Inner class to represent a Crossing (or use your existing Crossing class if it's defined elsewhere)
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
