<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.userregistration.model.Crossing" %>
<%
    List<Crossing> crossings = (List<Crossing>) request.getAttribute("crossings");
    List<Integer> favoriteIds = (List<Integer>) request.getAttribute("favoriteIds");
    String tab = request.getParameter("tab");
    if (tab == null) tab = "all";
    String search = request.getParameter("search");
    if (search == null) search = "";
%>
<!DOCTYPE html>
<html>
<head>
    <title>Search Railway Crossing</title>
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
            padding: 30px 40px 10px 40px;
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
        .tabs {
            margin: 0 40px 20px 40px;
        }
        .tab-btn {
            background: #eee;
            border: none;
            border-radius: 8px 8px 0 0;
            padding: 10px 24px;
            font-size: 1em;
            margin-right: 8px;
            cursor: pointer;
            font-weight: 500;
            color: #333;
        }
        .tab-btn.active {
            background: #fff;
            border-bottom: 2px solid #4CAF50;
            color: #4CAF50;
        }
        .search-section {
            max-width: 900px;
            margin: 0 auto 30px auto;
            padding: 0 32px;
        }
        .search-label {
            font-size: 1.1em;
            margin-bottom: 8px;
            display: block;
            color: #222;
        }
        .search-bar {
            display: flex;
            gap: 10px;
            margin-bottom: 18px;
        }
        .search-bar input[type="text"] {
            flex: 1;
            padding: 10px 15px;
            border: 1px solid #bbb;
            border-radius: 6px;
            font-size: 1.1em;
        }
        .search-bar button {
            background: #eee;
            border: none;
            border-radius: 6px;
            padding: 10px 32px;
            font-size: 1.1em;
            color: #888;
            cursor: pointer;
            font-weight: 500;
        }
        .crossings-list {
            max-width: 900px;
            margin: 0 auto 40px auto;
        }
        .crossing-card {
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.07);
            margin-bottom: 28px;
            padding: 24px 32px 18px 32px;
            border: 1px solid #eee;
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
            cursor: pointer;
            font-weight: 500;
        }
        .favorite-btn:disabled {
            color: #bbb;
            background: #f5f5f5;
            border: 1px solid #ddd;
            cursor: not-allowed;
        }
    </style>
</head>
<body>
    <div class="main-header">
        <h1>Search Railway Crossing</h1>
        <form action="LogoutServlet" method="post" style="margin:0;">
            <button type="submit" class="logout-btn">Logout</button>
        </form>
    </div>
    <div class="tabs">
        <a href="searchCrossings.jsp?tab=all"><button class="tab-btn<%= ("all".equals(tab)) ? " active" : "" %>">All</button></a>
        <a href="searchCrossings.jsp?tab=favorite"><button class="tab-btn<%= ("favorite".equals(tab)) ? " active" : "" %>">Favorite Crossing</button></a>
    </div>
    <div class="search-section">
        <form class="search-bar" method="get" action="searchCrossings.jsp">
            <input type="hidden" name="tab" value="<%= tab %>" />
            <label for="searchInput" class="search-label">Search Railway Crossing</label>
            <input id="searchInput" name="search" type="text" value="<%= search %>" placeholder="Type to search..." autocomplete="off" />
            <button type="submit">Search</button>
        </form>
    </div>
    <div class="crossings-list">
        <% 
        boolean found = false;
        if (crossings != null && !crossings.isEmpty()) {
            for (Crossing crossing : crossings) {
                boolean isFavorite = favoriteIds != null && favoriteIds.contains(crossing.getId());
                if ("favorite".equals(tab) && !isFavorite) continue;
                if (search.length() > 0 && (crossing.getName() == null || !crossing.getName().toLowerCase().contains(search.toLowerCase()))) continue;
                found = true;
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
            <form action="FavoriteServlet" method="post" style="display:inline;">
                <input type="hidden" name="crossingId" value="<%= crossing.getId() %>" />
                <button class="favorite-btn" type="submit" <%= isFavorite ? "disabled" : "" %>>ADD TO FAVORITE</button>
            </form>
        </div>
        <%   }
        }
        if (!found) { %>
        <div style="text-align:center; color:#888; margin-top:40px;">No railway crossings found.</div>
        <% } %>
    </div>
</body>
</html> 