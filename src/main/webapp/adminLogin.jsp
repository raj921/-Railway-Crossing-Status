<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Login - Railway Crossing</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            height: 100vh;
            display: flex;
            align-items: center;
        }
        .login-container {
            max-width: 400px;
            margin: 0 auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
        }
        .login-logo {
            text-align: center;
            margin-bottom: 30px;
        }
        .login-logo img {
            max-width: 150px;
        }
        .form-floating {
            margin-bottom: 1rem;
        }
        .btn-login {
            background-color: #0d6efd;
            border: none;
            padding: 12px;
            font-weight: 600;
        }
        .btn-login:hover {
            background-color: #0b5ed7;
        }
        .error-message {
            color: #dc3545;
            margin-bottom: 1rem;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-container">
            <div class="login-logo">
                <h2>Railway Crossing</h2>
                <p class="text-muted">Admin Portal</p>
            </div>
            
            <%-- Display error message if any --%>
            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <form action="${pageContext.request.contextPath}/admin/login" method="POST">
                <div class="form-floating mb-3">
                    <input type="text" class="form-control" id="username" name="username" 
                           placeholder="Username" required>
                    <label for="username">Username</label>
                </div>
                <div class="form-floating mb-4">
                    <input type="password" class="form-control" id="password" 
                           name="password" placeholder="Password" required>
                    <label for="password">Password</label>
                </div>
                <button class="w-100 btn btn-lg btn-primary btn-login" type="submit">
                    Sign in
                </button>
            </form>
            
            <div class="text-center mt-3">
                <a href="index.jsp" class="text-decoration-none">← Back to Home</a>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
