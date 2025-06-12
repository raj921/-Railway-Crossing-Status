<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.http.*" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Registration</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <h1>Railway Crossing</h1>
        <p class="subtitle">User Register</p>
        
        <%-- Display error message if any --%>
        <% if (request.getParameter("error") != null) { %>
            <div class="error">
                <% if ("email_exists".equals(request.getParameter("error"))) { %>
                    Email already exists. Please use a different email.
                <% } else { %>
                    An error occurred during registration. Please try again.
                <% } %>
            </div>
        <% } %>
        
        <form action="${pageContext.request.contextPath}/RegisterServlet" method="post" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="name">Enter Name</label>
                <input type="text" id="name" name="name" placeholder="Enter your name" required>
            </div>
            
            <div class="form-group">
                <label for="email">Enter Email</label>
                <input type="email" id="email" name="email" placeholder="Enter your email" required>
            </div>
            
            <div class="form-group">
                <label for="password">Enter Password</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" required>
                <div id="passwordError" class="error"></div>
            </div>
            
            <button type="submit" class="btn">Register</button>
        </form>
        
        <div class="link">
            Already have an account? <a href="${pageContext.request.contextPath}/login.jsp">Sign In Instead</a>
        </div>
    </div>
    
    <script>
        function validateForm() {
            const password = document.getElementById('password').value;
            const errorElement = document.getElementById('passwordError');
            
            if (password.length < 6) {
                errorElement.textContent = 'Password must be at least 6 characters long';
                return false;
            }
            
            errorElement.textContent = '';
            return true;
        }
    </script>
</body>
</html>
