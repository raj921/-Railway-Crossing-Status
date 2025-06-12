<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.example.userregistration.RailwayCrossing" %>
<%
    // Check if user is logged in as admin
    if (session.getAttribute("adminId") == null) {
        response.sendRedirect("adminLogin.jsp");
        return;
    }
    
    RailwayCrossing crossing = (RailwayCrossing) request.getAttribute("crossing");
    if (crossing == null) {
        response.sendRedirect("adminDashboard.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Railway Crossing - Admin Panel</title>
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
        .form-container {
            background: white;
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
        }
        .photo-preview {
            width: 200px;
            height: 150px;
            object-fit: cover;
            border: 1px solid #dee2e6;
            border-radius: 0.375rem;
            margin-bottom: 1rem;
        }
        .photo-upload {
            cursor: pointer;
            border: 1px dashed #dee2e6;
            border-radius: 0.375rem;
            padding: 1rem;
            text-align: center;
            margin-bottom: 1rem;
        }
        .photo-upload:hover {
            border-color: #6c757d;
            background-color: #f8f9fa;
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
                            <a href="adminDashboard.jsp" class="nav-link active">
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
                            <a href="#" class="nav-link">
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
                            <li><a class="dropdown-item" href="#">Profile</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="admin/logout">Sign out</a></li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Edit Railway Crossing</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="adminDashboard.jsp" class="btn btn-outline-secondary me-2">
                            <i class="bi bi-arrow-left"></i> Back to List
                        </a>
                    </div>
                </div>

                <!-- Error Message -->
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <c:remove var="errorMessage" scope="session" />
                </c:if>

                <div class="form-container">
                    <form id="editCrossingForm" action="edit-crossing" method="POST" enctype="multipart/form-data" novalidate>
                        <input type="hidden" name="id" value="${crossing.id}">
                        
                        <div class="row">
                            <div class="col-md-8">
                                <div class="mb-3">
                                    <label for="name" class="form-label">Crossing Name <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="name" name="name" value="${crossing.name}" required>
                                    <div class="invalid-feedback">Please provide a name for the crossing.</div>
                                </div>

                                <div class="mb-3">
                                    <label for="address" class="form-label">Address <span class="text-danger">*</span></label>
                                    <textarea class="form-control" id="address" name="address" rows="2" required>${crossing.address}</textarea>
                                    <div class="invalid-feedback">Please provide the address.</div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="landmark" class="form-label">Landmark</label>
                                        <input type="text" class="form-control" id="landmark" name="landmark" value="${crossing.landmark}">
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="personInCharge" class="form-label">Person In-Charge <span class="text-danger">*</span></label>
                                        <input type="text" class="form-control" id="personInCharge" name="personInCharge" value="${crossing.personInCharge}" required>
                                        <div class="invalid-feedback">Please provide the name of the person in charge.</div>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label for="schedule" class="form-label">Train Schedule <span class="text-danger">*</span></label>
                                    <textarea class="form-control" id="schedule" name="schedule" rows="3" required>${crossing.trainSchedule}</textarea>
                                    <div class="form-text">Enter train timings and frequency (e.g., "Every 30 minutes, 6 AM to 10 PM")</div>
                                    <div class="invalid-feedback">Please provide the train schedule.</div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="status" class="form-label">Status <span class="text-danger">*</span></label>
                                        <select class="form-select" id="status" name="status" required>
                                            <option value="Active" ${crossing.status == 'Active' ? 'selected' : ''}>Active</option>
                                            <option value="Inactive" ${crossing.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                                            <option value="Under Maintenance" ${crossing.status == 'Under Maintenance' ? 'selected' : ''}>Under Maintenance</option>
                                        </select>
                                        <div class="invalid-feedback">Please select a status.</div>
                                    </div>
                                </div>

                                <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                                    <a href="adminDashboard.jsp" class="btn btn-outline-secondary me-md-2">
                                        <i class="bi bi-x-circle"></i> Cancel
                                    </a>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="bi bi-save"></i> Save Changes
                                    </button>
                                </div>
                            </div>

                            <div class="col-md-4">
                                <div class="card mb-4">
                                    <div class="card-header">
                                        <i class="bi bi-image"></i> Crossing Photo
                                    </div>
                                    <div class="card-body text-center">
                                        <c:choose>
                                            <c:when test="${not empty crossing.photo}">
                                                <img id="photoPreview" src="data:image/jpeg;base64,${crossing.photoBase64}" alt="Crossing Photo" class="photo-preview">
                                            </c:when>
                                            <c:otherwise>
                                                <img id="photoPreview" src="https://via.placeholder.com/200x150?text=No+Photo" alt="No Photo" class="photo-preview">
                                            </c:otherwise>
                                        </c:choose>
                                        
                                        <div class="photo-upload" onclick="document.getElementById('photo').click()">
                                            <i class="bi bi-camera" style="font-size: 2rem; color: #6c757d;"></i>
                                            <p class="mb-0 mt-2">Click to change photo</p>
                                            <input type="file" id="photo" name="photo" accept="image/*" class="d-none" onchange="previewPhoto(this)">
                                        </div>
                                        <div class="small text-muted">Max file size: 5MB (JPG, PNG)</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Form validation
        (function () {
            'use strict'
            
            // Fetch the form we want to apply custom Bootstrap validation styles to
            var form = document.getElementById('editCrossingForm')
            
            // Add event listener for form submission
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }
                
                form.classList.add('was-validated')
            }, false)
        })()
        
        // Photo preview
        function previewPhoto(input) {
            if (input.files && input.files[0]) {
                const reader = new FileReader();
                
                reader.onload = function(e) {
                    document.getElementById('photoPreview').src = e.target.result;
                }
                
                reader.readAsDataURL(input.files[0]);
            }
        }
    </script>
</body>
</html>
