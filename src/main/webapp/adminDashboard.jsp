<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.userregistration.RailwayCrossing" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    // This page should only be accessed through the AdminDashboardServlet
    // which will set the 'crossings' attribute
    List<RailwayCrossing> crossings = (List<RailwayCrossing>) request.getAttribute("crossings");
    if (crossings == null) {
        // If no crossings found, redirect to the servlet
        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Home Page</title>
    <style>
        body {
            font-family: 'Times New Roman', Times, serif;
            background: #fff;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 80vw;
            margin: 40px auto;
            background: #fff;
            border: 1px solid #ccc;
            border-radius: 0;
            box-shadow: none;
            padding: 0 0 30px 0;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 0;
            padding: 30px 40px 0 40px;
        }
        .header h1 {
            margin: 0 0 10px 0;
            font-size: 2.2em;
            font-family: 'Times New Roman', Times, serif;
        }
        .logout-btn {
            background: #eee;
            border: none;
            padding: 6px 16px;
            border-radius: 6px;
            font-size: 1em;
            cursor: pointer;
            float: right;
        }
        .crossing-count {
            font-size: 1.1em;
            margin-bottom: 8px;
        }
        .nav {
            margin: 0 0 20px 40px;
        }
        .nav button {
            background: #eee;
            border: none;
            padding: 6px 16px;
            border-radius: 6px;
            margin-right: 8px;
            font-size: 1em;
            cursor: pointer;
        }
        .nav button:hover {
            background: #d4d4d4;
        }
        table {
            width: 95%;
            border-collapse: collapse;
            margin: 0 auto;
            background: #fff;
        }
        th, td {
            border: 1px solid #888;
            padding: 8px 8px;
            text-align: left;
        }
        th {
            background: #fff;
            font-weight: bold;
        }
        td:last-child {
            text-align: center;
        }
        .action-btn {
            background: #eee;
            border: none;
            padding: 4px 12px;
            border-radius: 6px;
            margin: 0 2px;
            font-size: 0.98em;
            cursor: pointer;
        }
        .action-btn:hover {
            background: #d4d4d4;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <div>
            <h1>Admin Home Page</h1>
            <div class="crossing-count">
                Railway Crossings [ <%= crossings.size() %> ]
            </div>
        </div>
        <form action="logout" method="post" style="margin:0;">
            <button class="logout-btn" type="submit">Logout</button>
        </form>
    </div>
    <div class="nav">
        <button onclick="location.href='<%= request.getContextPath() %>/admin/dashboard'">Home</button>
        <button onclick="location.href='<%= request.getContextPath() %>/addCrossing.jsp'">Add Railway Crossing</button>
        <button onclick="location.href='searchCrossing.jsp'">Search Crossing</button>
    </div>
    <table>
        <tr>
            <th>Sr. No</th>
            <th>Name</th>
            <th>Address</th>
            <th>Landmark</th>
            <th>Train Schedule</th>
            <th>Person In-charge</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <%
            int i = 1;
            for (RailwayCrossing crossing : crossings) {
        %>
        <tr>
            <td><%= i++ %></td>
            <td><%= crossing.getName() %></td>
            <td><%= crossing.getAddress() %></td>
            <td><%= crossing.getLandmark() %></td>
            <td><%= crossing.getTrainSchedule() %></td>
            <td><%= crossing.getPersonInCharge() %></td>
            <td><%= crossing.getStatus() %></td>
            <td>
                <form action="updateCrossing" method="get" style="display:inline;">
                    <input type="hidden" name="id" value="<%= crossing.getId() %>"/>
                    <button class="action-btn" type="submit">Update</button>
                </form>
                <form action="deleteCrossing" method="post" style="display:inline;">
                    <input type="hidden" name="id" value="<%= crossing.getId() %>"/>
                    <button class="action-btn" type="submit" onclick="return confirm('Are you sure you want to delete this crossing?');">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
</div>
</body>
</html>
