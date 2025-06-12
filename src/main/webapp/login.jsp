<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <h1>Railway Crossing</h1>
        <p class="subtitle">User Login</p>
        
        <%-- Display success message if redirected from registration --%>
        <% if (request.getParameter("registered") != null) { %>
            <div class="success">
                Registration successful! Please login with your credentials.
            </div>
        <% } %>
        
        <%-- Display error message if login fails --%>
        <% if (request.getParameter("error") != null) { %>
            <div class="error" style="text-align: center; margin-bottom: 15px;">
                Invalid email or password. Please try again.
            </div>
        <% } %>
        
        <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
            <div class="form-group">
                <label for="email">Enter Email</label>
                <input type="email" id="email" name="email" placeholder="Enter your email" required>
            </div>
            
            <div class="form-group">
                <label for="password">Enter Password</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" required>
            </div>
            
            <button type="submit" class="btn">Login</button>
        </form>
        
        <div class="link">
            Don't have an account? <a href="${pageContext.request.contextPath}/register.jsp">Register here</a>
        </div>
    </div>
</body>
</html>
