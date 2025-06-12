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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class FavoriteServlet extends HttpServlet {
    private static final Gson gson = new Gson();
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        JsonObject jsonResponse = new JsonObject();
        
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonResponse.addProperty("error", "User not logged in");
            response.getWriter().write(jsonResponse.toString());
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        String action = request.getParameter("action");
        String crossingIdParam = request.getParameter("crossingId");
        
        if (action == null || crossingIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("error", "Missing parameters");
            response.getWriter().write(jsonResponse.toString());
            return;
        }
        
        try {
            int crossingId = Integer.parseInt(crossingIdParam);
            boolean success = false;
            
            if ("add".equalsIgnoreCase(action)) {
                success = addToFavorites(userId, crossingId);
                jsonResponse.addProperty("message", success ? "Added to favorites" : "Failed to add to favorites");
            } else if ("remove".equalsIgnoreCase(action)) {
                success = removeFromFavorites(userId, crossingId);
                jsonResponse.addProperty("message", success ? "Removed from favorites" : "Failed to remove from favorites");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("error", "Invalid action");
                response.getWriter().write(jsonResponse.toString());
                return;
            }
            
            jsonResponse.addProperty("success", success);
            response.getWriter().write(jsonResponse.toString());
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("error", "Invalid crossing ID");
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.addProperty("error", "Server error: " + e.getMessage());
            response.getWriter().write(jsonResponse.toString());
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        JsonObject jsonResponse = new JsonObject();
        
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonResponse.addProperty("error", "User not logged in");
            response.getWriter().write(jsonResponse.toString());
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        String crossingIdParam = request.getParameter("crossingId");
        
        try {
            if (crossingIdParam != null && crossingIdParam.contains(",")) {
                // Handle multiple crossing IDs (for batch checking)
                JsonObject result = new JsonObject();
                String[] crossingIds = crossingIdParam.split(",");
                for (String id : crossingIds) {
                    try {
                        int crossingId = Integer.parseInt(id.trim());
                        boolean isFav = isFavorite(userId, crossingId);
                        result.addProperty(String.valueOf(crossingId), isFav);
                    } catch (NumberFormatException e) {
                        // Skip invalid IDs
                    }
                }
                response.getWriter().write(gson.toJson(result));
            } else {
                // Handle single crossing ID
                boolean isFavorite = isFavorite(userId, crossingIdParam);
                jsonResponse.addProperty("isFavorite", isFavorite);
                response.getWriter().write(jsonResponse.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.addProperty("error", "Server error: " + e.getMessage());
            response.getWriter().write(jsonResponse.toString());
        }
    }
    
    private boolean addToFavorites(int userId, int crossingId) throws SQLException, ClassNotFoundException {
        String sql = "INSERT IGNORE INTO user_favorites (user_id, crossing_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, crossingId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    private boolean removeFromFavorites(int userId, int crossingId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM user_favorites WHERE user_id = ? AND crossing_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, crossingId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    private boolean isFavorite(int userId, String crossingIdParam) throws SQLException, ClassNotFoundException, NumberFormatException {
        if (crossingIdParam == null || crossingIdParam.trim().isEmpty()) {
            return false;
        }
        return isFavorite(userId, Integer.parseInt(crossingIdParam.trim()));
    }
    
    private boolean isFavorite(int userId, int crossingId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) as count FROM user_favorites WHERE user_id = ? AND crossing_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, crossingId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt("count") > 0;
            }
        }
    }
}
