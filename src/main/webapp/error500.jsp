<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>500 - Internal Server Error</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            text-align: center;
        }
        .error-container {
            background: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-width: 600px;
            width: 100%;
        }
        h1 {
            color: #d32f2f;
            font-size: 48px;
            margin-bottom: 20px;
        }
        p {
            font-size: 18px;
            color: #555;
            margin-bottom: 30px;
        }
        .home-link {
            display: inline-block;
            background-color: #4CAF50;
            color: white;
            padding: 12px 25px;
            border-radius: 4px;
            text-decoration: none;
            font-size: 16px;
            transition: background-color 0.3s;
            margin-top: 20px;
        }
        .home-link:hover {
            background-color: #45a049;
            text-decoration: none;
        }
        .error-details {
            margin-top: 30px;
            padding: 15px;
            background-color: #f8f8f8;
            border-left: 4px solid #d32f2f;
            text-align: left;
            font-family: monospace;
            font-size: 14px;
            color: #333;
            overflow-x: auto;
            display: none; /* Hidden by default, can be shown with JavaScript */
        }
        .toggle-error {
            background: none;
            border: none;
            color: #1976d2;
            text-decoration: underline;
            cursor: pointer;
            font-size: 14px;
            margin-top: 15px;
            display: block;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>500</h1>
        <h2>Internal Server Error</h2>
        <p>Oops! Something went wrong on our end. Please try again later or contact support if the problem persists.</p>
        
        <button class="toggle-error" onclick="toggleErrorDetails()">Show Error Details</button>
        
        <div id="errorDetails" class="error-details">
            <p><strong>Error:</strong> ${pageContext.exception}</p>
            <p><strong>Message:</strong> ${pageContext.exception.message}</p>
            <p><strong>Stack Trace:</strong><br>
                <pre>${pageContext.out.flush(); pageContext.exception.printStackTrace(pageContext.response.writer)}</pre>
            </p>
        </div>
        
        <a href="${pageContext.request.contextPath}/login.jsp" class="home-link">Go to Home Page</a>
    </div>
    
    <script>
        function toggleErrorDetails() {
            const details = document.getElementById('errorDetails');
            const button = document.querySelector('.toggle-error');
            
            if (details.style.display === 'none' || !details.style.display) {
                details.style.display = 'block';
                button.textContent = 'Hide Error Details';
            } else {
                details.style.display = 'none';
                button.textContent = 'Show Error Details';
            }
        }
    </script>
</body>
</html>
