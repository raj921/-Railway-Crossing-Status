<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.userregistration.model.Crossing" %>
<%
    List<Crossing> favoriteCrossings = (List<Crossing>) request.getAttribute("favoriteCrossings");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Favourite Railway Crossing</title>
    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background: #fafafa;
            margin: 0;
        }
        .main-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 30px 60px 10px 60px;
        }
        .main-header h1 {
            margin: 0;
            font-size: 2.2em;
        }
        .logout-btn {
            background: #eee;
            border: none;
            border-radius: 8px;
            padding: 8px 18px;
            font-size: 1em;
            cursor: pointer;
        }
        .main-content {
            max-width: 900px;
            margin-left: 60px;
        }
        .tabs {
            margin: 0 0 20px 0;
            display: flex;
        }
        .tab-btn {
            background: #eee;
            border: none;
            border-radius: 16px 16px 0 0;
            padding: 18px 40px 18px 40px;
            font-size: 1.2em;
            margin-right: 8px;
            cursor: pointer;
            font-weight: 500;
            color: #333;
        }
        .tab-btn.active {
            background: #fff;
            border-bottom: 3px solid #43d13a;
            color: #43d13a;
        }
        .crossings-list {
            margin-top: 0;
        }
        .crossing-card {
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.07);
            margin-bottom: 28px;
            padding: 24px 32px 18px 32px;
            border: 1px solid #eee;
            max-width: 95%;
        }
        .crossing-title {
            font-size: 1.25em;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .status-badge {
            display: inline-block;
            padding: 2px 14px;
            border-radius: 12px;
            font-size: 0.95em;
            font-weight: 600;
            color: #fff;
            background: #43d13a;
            margin-left: 10px;
            vertical-align: middle;
        }
        .status-badge.closed {
            background: #e74c3c;
        }
        .crossing-info {
            margin-bottom: 6px;
        }
        .crossing-info span.label {
            font-weight: normal;
            color: #444;
        }
        .crossing-info span.value {
            font-weight: bold;
            margin-left: 4px;
        }
        .favorite-btn {
            margin-top: 10px;
            background: #f5f5f5;
            border: 1px solid #bbb;
            border-radius: 8px;
            padding: 7px 18px;
            font-size: 1em;
            color: #888;
            cursor: not-allowed;
            font-weight: 500;
        }
        .no-crossings {
            color: #999;
            margin-top: 40px;
            font-size: 1.2em;
            font-weight: 500;
        }
    </style>
</head>
<body>
    <div class="main-header">
        <h1>Favourite Railway Crossing</h1>
        <form action="LogoutServlet" method="post" style="margin:0;">
            <button type="submit" class="logout-btn">Logout</button>
        </form>
    </div>
    <div class="main-content">
        <div class="tabs">
            <a href="dashboard.jsp"><button class="tab-btn">Home</button></a>
            <a href="searchCrossings.jsp"><button class="tab-btn">Search Crossing</button></a>
        </div>
        <div class="crossings-list">
            <% 
            if (favoriteCrossings != null && !favoriteCrossings.isEmpty()) {
                for (Crossing crossing : favoriteCrossings) {
            %>
            <div class="crossing-card">
                <div class="crossing-title">
                    <%= crossing.getName() %>
                    <span class="status-badge<%= "Open".equalsIgnoreCase(crossing.getStatus()) ? "" : " closed" %>">
                        <%= crossing.getStatus() != null ? crossing.getStatus().toUpperCase() : "UNKNOWN" %>
                    </span>
                </div>
                <div class="crossing-info"><span class="label">Person In-charge:</span> <span class="value"><%= crossing.getPersonInCharge() %></span></div>
                <div class="crossing-info"><span class="label">Train Schedule:</span> <span class="value"><%= crossing.getTrainSchedule() %></span></div>
                <div class="crossing-info"><span class="label">Landmark:</span> <span class="value"><%= crossing.getLandmark() %></span></div>
                <div class="crossing-info"><span class="label">Address:</span> <span class="value"><%= crossing.getAddress() %></span></div>
                <button class="favorite-btn" type="button" disabled>REMOVE FROM FAVORITE</button>
            </div>
            <%   }
            } else { %>
            <div class="no-crossings">No favourite railway crossings found.</div>
            <% } %>
        </div>
    </div>
</body>
</html> 