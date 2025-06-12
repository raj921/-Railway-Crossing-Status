<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    // Check if user is logged in as admin
    if (session.getAttribute("adminId") == null) {
        response.sendRedirect("adminLogin.jsp");
        return;
    }
    
    // Get the crossing from request attribute
    com.example.userregistration.RailwayCrossing crossing = (com.example.userregistration.RailwayCrossing) request.getAttribute("crossing");
    if (crossing == null) {
        response.sendRedirect("manageCrossings.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Railway Crossing - Admin Panel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .sidebar {
            min-height: 100vh;
            background: #343a40;
            color: white;
        }
        .sidebar .nav-link {
            color: rgba(255, 255, 255, 0.8);
            padding: 0.75rem 1rem;
            margin: 0.25rem 0;
            border-radius: 0.25rem;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background: rgba(255, 255, 255, 0.1);
            color: white;
        }
        .sidebar .nav-link i {
            margin-right: 10px;
            width: 20px;
            text-align: center;
        }
        .main-content {
            padding: 2rem;
        }
        .page-header {
            margin-bottom: 2rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #eee;
        }
        .card {
            margin-bottom: 1.5rem;
            border: none;
            box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
        }
        .card-header {
            background-color: #f8f9fa;
            border-bottom: 1px solid rgba(0, 0, 0, 0.05);
            font-weight: 600;
            padding: 1rem 1.25rem;
        }
        .card-body {
            padding: 1.5rem;
        }
        .info-label {
            font-weight: 600;
            color: #6c757d;
            margin-bottom: 0.25rem;
        }
        .info-value {
            margin-bottom: 1rem;
            word-break: break-word;
        }
        .map-container {
            height: 300px;
            background: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 0.375rem;
            margin-bottom: 1rem;
            position: relative;
            overflow: hidden;
        }
        .map-placeholder {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100%;
            color: #6c757d;
        }
        .photo-container {
            width: 100%;
            height: 200px;
            background: #f8f9fa;
            border: 1px dashed #dee2e6;
            border-radius: 0.375rem;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 1rem;
        }
        .status-badge {
            font-size: 0.875rem;
            padding: 0.35em 0.65em;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2 px-0 sidebar">
                <div class="d-flex flex-column p-3">
                    <a href="#" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
                        <span class="fs-4">Admin Panel</span>
                    </a>
                    <hr>
                    <ul class="nav nav-pills flex-column mb-auto">
                        <li class="nav-item">
                            <a href="adminDashboard.jsp" class="nav-link">
                                <i class="bi bi-speedometer2"></i>
                                Dashboard
                            </a>
                        </li>
                        <li>
                            <a href="manageCrossings.jsp" class="nav-link">
                                <i class="bi bi-signpost-split"></i>
                                Manage Crossings
                            </a>
                        </li>
                        <li>
                            <a href="addCrossing.jsp" class="nav-link">
                                <i class="bi bi-plus-circle"></i>
                                Add Crossing
                            </a>
                        </li>
                        <li>
                            <a href="manageUsers.jsp" class="nav-link">
                                <i class="bi bi-people"></i>
                                Manage Users
                            </a>
                        </li>
                    </ul>
                    <hr>
                    <div class="dropdown">
                        <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="bi bi-person-circle me-2"></i>
                            <strong>${adminName}</strong>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-dark text-small shadow" aria-labelledby="dropdownUser1">
                            <li><a class="dropdown-item" href="profile.jsp">Profile</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="admin/logout">Sign out</a></li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Railway Crossing Details</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="manageCrossings.jsp" class="btn btn-outline-secondary me-2">
                            <i class="bi bi-arrow-left"></i> Back to List
                        </a>
                        <a href="edit-crossing?id=${crossing.id}" class="btn btn-primary me-2">
                            <i class="bi bi-pencil"></i> Edit
                        </a>
                    </div>
                </div>

                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <div class="row">
                    <div class="col-md-8">
                        <div class="card mb-4">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <span><i class="bi bi-info-circle me-2"></i>Basic Information</span>
                                <span class="badge ${crossing.status == 'Active' ? 'bg-success' : 'bg-danger'} status-badge">
                                    ${crossing.status}
                                </span>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="info-label">Crossing Name</div>
                                        <div class="info-value">${crossing.name}</div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="info-label">Status</div>
                                        <div class="info-value">
                                            <span class="badge ${crossing.status == 'Active' ? 'bg-success' : crossing.status == 'Inactive' ? 'bg-danger' : 'bg-warning text-dark'} status-badge">
                                                ${crossing.status}
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="info-label">Address</div>
                                <div class="info-value">${crossing.address}</div>
                                
                                <c:if test="${not empty crossing.landmark}">
                                    <div class="info-label">Landmark</div>
                                    <div class="info-value">${crossing.landmark}</div>
                                </c:if>
                                
                                <div class="row">
                                    <c:if test="${not empty crossing.latitude && crossing.latitude != 0}">
                                        <div class="col-md-6">
                                            <div class="info-label">Latitude</div>
                                            <div class="info-value">${crossing.latitude}</div>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty crossing.longitude && crossing.longitude != 0}">
                                        <div class="col-md-6">
                                            <div class="info-label">Longitude</div>
                                            <div class="info-value">${crossing.longitude}</div>
                                        </div>
                                    </c:if>
                                </div>
                                
                                <div class="info-label">Person In-Charge</div>
                                <div class="info-value">${crossing.personInCharge}</div>
                            </div>
                        </div>
                        
                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="bi bi-train-front me-2"></i>Train Schedule
                            </div>
                            <div class="card-body">
                                <div class="info-value">${crossing.trainSchedule}</div>
                            </div>
                        </div>
                        
                        <c:if test="${not empty crossing.notes}">
                            <div class="card mb-4">
                                <div class="card-header">
                                    <i class="bi bi-journal-text me-2"></i>Additional Notes
                                </div>
                                <div class="card-body">
                                    <div class="info-value">${crossing.notes}</div>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    
                    <div class="col-md-4">
                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="bi bi-geo-alt me-2"></i>Location Map
                            </div>
                            <div class="card-body p-0">
                                <div class="map-container">
                                    <div class="map-placeholder">
                                        <div class="text-center">
                                            <i class="bi bi-map" style="font-size: 2rem; margin-bottom: 0.5rem;"></i>
                                            <p class="mb-0">Map will appear here</p>
                                            <small class="text-muted">(Map integration coming soon)</small>
                                        </div>
                                    </div>
                                </div>
                                <c:if test="${not empty crossing.latitude && crossing.latitude != 0 && not empty crossing.longitude && crossing.longitude != 0}">
                                    <div class="p-3">
                                        <div class="d-grid">
                                            <a href="https://www.google.com/maps?q=${crossing.latitude},${crossing.longitude}" target="_blank" class="btn btn-outline-primary btn-sm">
                                                <i class="bi bi-arrow-up-right-square"></i> Open in Google Maps
                                            </a>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        
                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="bi bi-image me-2"></i>Photo
                            </div>
                            <div class="card-body text-center">
                                <div class="photo-container">
                                    <div class="text-center">
                                        <i class="bi bi-image" style="font-size: 3rem; color: #6c757d;"></i>
                                        <p class="mb-0 mt-2">No photo available</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="card">
                            <div class="card-header">
                                <i class="bi bi-info-circle me-2"></i>Metadata
                            </div>
                            <div class="card-body">
                                <div class="info-label">Created By</div>
                                <div class="info-value">${not empty crossing.adminName ? crossing.adminName : 'System'}</div>
                                
                                <div class="info-label">Created At</div>
                                <div class="info-value">${crossing.createdAt}</div>
                                
                                <c:if test="${not empty crossing.updatedAt}">
                                    <div class="info-label">Last Updated</div>
                                    <div class="info-value">${crossing.updatedAt}</div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
