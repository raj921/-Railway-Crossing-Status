package com.example.userregistration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/updateCrossing")
public class UpdateCrossingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String landmark = request.getParameter("landmark");
        String trainSchedule = request.getParameter("trainSchedule");
        String personInCharge = request.getParameter("personInCharge");
        String status = request.getParameter("status");

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "UPDATE railway_crossing SET name=?, address=?, landmark=?, train_schedule=?, person_in_charge=?, status=? WHERE id=?")) {
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, landmark);
            stmt.setString(4, trainSchedule);
            stmt.setString(5, personInCharge);
            stmt.setString(6, status);
            stmt.setInt(7, Integer.parseInt(id));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("adminDashboard.jsp");
    }
} 