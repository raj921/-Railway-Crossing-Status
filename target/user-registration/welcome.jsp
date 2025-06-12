<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<% 
    // Check if user is logged in
    String userName = (String) session.getAttribute("userName");
    if (userName == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .welcome-container {
            text-align: center;
        }
        .welcome-message {
            font-size: 18px;
            margin: 20px 0 30px;
            color: #555;
        }
        .user-info {
            background-color: #f9f9f9;
            padding: 25px;
            border-radius: 10px;
            margin-bottom: 30px;
            text-align: left;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }
        .user-info p {
            margin: 15px 0;
            font-size: 16px;
            color: #555;
        }
        .user-info strong {
            color: #333;
            display: inline-block;
            width: 80px;
        }
        .btn.logout {
            background-color: #f44336;
            margin-top: 20px;
        }
        .btn.logout:hover {
            background-color: #d32f2f;
        }
    </style>
</head>
<body>
    <div class="container welcome-container">
        <h1>Railway Crossing</h1>
        <p class="subtitle">Welcome to Your Dashboard</p>
        
        <div class="welcome-message">
            You have successfully logged in!
        </div>
        
        <div class="user-info">
            <p><strong>Name:</strong> ${sessionScope.userName}</p>
            <p><strong>Email:</strong> ${sessionScope.userEmail}</p>
        </div>
        
        <form action="LogoutServlet" method="post" style="width: 100%;">
            <button type="submit" class="btn logout">Logout</button>
        </form>
    </div>
</body>
</html>
