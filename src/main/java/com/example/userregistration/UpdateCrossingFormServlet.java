package com.example.userregistration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/updateCrossingForm")
public class UpdateCrossingFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM railway_crossing WHERE id = ?")) {
                stmt.setInt(1, Integer.parseInt(idStr));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    RailwayCrossing crossing = new RailwayCrossing(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("landmark"),
                        rs.getString("train_schedule"),
                        rs.getString("person_in_charge"),
                        rs.getString("status")
                    );
                    request.setAttribute("crossing", crossing);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        request.getRequestDispatcher("updateRailCrossingForm.jsp").forward(request, response);
    }
} 