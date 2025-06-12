<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    // Check if user is logged in as admin
    if (session.getAttribute("adminId") == null) {
        response.sendRedirect("adminLogin.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Railway Crossing</title>
    <style>
        body { font-family: 'Times New Roman', Times, serif; }
        .form-container {
            width: 400px;
            margin: 40px auto;
            background: #fff;
            border-radius: 8px;
            padding: 32px 40px 40px 40px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.07);
        }
        h1 { text-align: center; font-size: 2.2em; margin-bottom: 0; }
        .subtitle { text-align: center; color: #888; margin-bottom: 18px; }
        label { font-weight: bold; display: block; margin-top: 18px; }
        input[type=text], textarea {
            width: 100%; padding: 8px; margin-top: 4px; border: 1px solid #aaa; border-radius: 4px;
        }
        textarea { resize: vertical; }
        .status-row { margin-top: 18px; }
        .status-row label { display: inline; font-weight: normal; }
        .submit-btn {
            width: 100%; background: #00d000; color: #fff; border: none; border-radius: 6px;
            padding: 10px 0; font-size: 1.1em; margin-top: 24px; cursor: pointer;
        }
        .submit-btn:hover { background: #00b000; }
        .current-status { float: right; font-size: 0.98em; }
    </style>
</head>
<body>
    <div class="form-container">
        <h1>Railway Crossing</h1>
        <div class="subtitle">Add Railway Crossing Information</div>
        
        <%-- Display success/error messages --%>
        <c:if test="${not empty successMessage}">
            <div style="color: green; margin-bottom: 20px; text-align: center;">
                ${successMessage}
            </div>
            <% session.removeAttribute("successMessage"); %>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div style="color: red; margin-bottom: 20px; text-align: center;">
                ${errorMessage}
            </div>
            <% session.removeAttribute("errorMessage"); %>
        </c:if>
        <form action="<%= request.getContextPath() %>/addCrossing" method="post">
            <label>Enter Name
                <input type="text" name="name" required/>
            </label>
            <label>Address
                <input type="text" name="address" required/>
            </label>
            <label>Landmark
                <textarea name="landmark" rows="2" required></textarea>
            </label>
            <label>Train Schedule
                <input type="text" name="trainSchedule" required/>
            </label>
            <label>Person In-charge
                <input type="text" name="personInCharge" required/>
            </label>
            <div class="status-row">
                <span>Crossing Status</span>
                <span class="current-status">Currently: <b>Open</b></span><br>
                <input type="radio" name="status" value="Open" checked/> Open
                <input type="radio" name="status" value="Closed" style="margin-left:20px;"/> Close
            </div>
            <button class="submit-btn" type="submit">Submit</button>
        </form>
    </div>
</body>
</html>
