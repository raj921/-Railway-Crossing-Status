<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.userregistration.RailwayCrossing" %>
<%
    RailwayCrossing crossing = (RailwayCrossing) request.getAttribute("crossing");
    if (crossing == null) {
        out.println("<h2>No crossing found.</h2>");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
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
        h1 { text-align: center; }
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
    <div style="text-align:center; margin-bottom:18px;">Update Railway Crossing Information</div>
    <form action="updateCrossing" method="post">
        <input type="hidden" name="id" value="<%= crossing.getId() %>"/>
        <label>Enter Name
            <input type="text" name="name" value="<%= crossing.getName() %>" required/>
        </label>
        <label>Address
            <input type="text" name="address" value="<%= crossing.getAddress() %>" required/>
        </label>
        <label>Landmark
            <textarea name="landmark" rows="2" required><%= crossing.getLandmark() %></textarea>
        </label>
        <label>Train Schedule
            <input type="text" name="trainSchedule" value="<%= crossing.getTrainSchedule() %>" required/>
        </label>
        <label>Person In-charge
            <input type="text" name="personInCharge" value="<%= crossing.getPersonInCharge() %>" required/>
        </label>
        <div class="status-row">
            <span>Crossing Status</span>
            <span class="current-status">Currently: <b><%= crossing.getStatus() %></b></span><br>
            <input type="radio" name="status" value="Open" <%= "Open".equalsIgnoreCase(crossing.getStatus()) ? "checked" : "" %>/> Open
            <input type="radio" name="status" value="Closed" <%= "Closed".equalsIgnoreCase(crossing.getStatus()) ? "checked" : "" %> style="margin-left:20px;"/> Close
        </div>
        <button class="submit-btn" type="submit">Submit</button>
    </form>
</div>
</body>
</html> 