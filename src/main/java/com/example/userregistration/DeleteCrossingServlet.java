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
import java.sql.SQLException;

@WebServlet("/deleteCrossing")
public class DeleteCrossingServlet extends HttpServlet {
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

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            setErrorMessage(request, "Invalid crossing ID");
            response.sendRedirect("adminDashboard.jsp");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);
            
            try {
                // First, delete any favorites associated with this crossing
                try (PreparedStatement deleteFavorites = conn.prepareStatement(
                        "DELETE FROM favorites WHERE crossing_id = ?")) {
                    deleteFavorites.setInt(1, Integer.parseInt(idStr));
                    deleteFavorites.executeUpdate();
                }
                
                // Then delete the crossing
                try (PreparedStatement deleteCrossing = conn.prepareStatement(
                        "DELETE FROM railway_crossing WHERE id = ?")) {
                    deleteCrossing.setInt(1, Integer.parseInt(idStr));
                    int rowsAffected = deleteCrossing.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        conn.commit();
                        setSuccessMessage(request, "Crossing deleted successfully");
                    } else {
                        conn.rollback();
                        setErrorMessage(request, "No crossing found with ID: " + idStr);
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Invalid crossing ID format");
            e.printStackTrace();
        } catch (SQLException e) {
            setErrorMessage(request, "Error deleting crossing: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            setErrorMessage(request, "An unexpected error occurred");
            e.printStackTrace();
        }
        
        // Always redirect back to the dashboard
        response.sendRedirect("adminDashboard.jsp");
    }
    
    private void setErrorMessage(HttpServletRequest request, String message) {
        request.getSession().setAttribute("errorMessage", message);
    }
    
    private void setSuccessMessage(HttpServletRequest request, String message) {
        request.getSession().setAttribute("successMessage", message);
    }
}
