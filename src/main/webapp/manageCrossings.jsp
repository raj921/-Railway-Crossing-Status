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
    <title>Manage Railway Crossings - Admin Panel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css">
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
        .status-badge {
            font-size: 0.75rem;
            padding: 0.25em 0.5em;
        }
        .action-btns .btn {
            padding: 0.25rem 0.5rem;
            font-size: 0.875rem;
            margin-right: 0.25rem;
        }
        .dataTables_wrapper .dataTables_length select {
            padding: 0.25rem 0.5rem;
        }
        .dataTables_wrapper .dataTables_filter input {
            margin-left: 0.5rem;
            padding: 0.25rem 0.5rem;
            border-radius: 0.25rem;
            border: 1px solid #ced4da;
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
                            <a href="manageCrossings.jsp" class="nav-link active">
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
                    <h1 class="h2">Manage Railway Crossings</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="addCrossing.jsp" class="btn btn-primary">
                            <i class="bi bi-plus-circle"></i> Add New Crossing
                        </a>
                    </div>
                </div>

                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <div class="card">
                    <div class="card-body">
                        <div class="table-responsive">
                            <table id="crossingsTable" class="table table-striped table-hover" style="width:100%">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Address</th>
                                        <th>Status</th>
                                        <th>Person In-Charge</th>
                                        <th>Last Updated</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>1</td>
                                        <td>Sample Crossing 1</td>
                                        <td>123 Railway St, City A</td>
                                        <td><span class="badge bg-success status-badge">Active</span></td>
                                        <td>John Doe</td>
                                        <td>2023-06-10 14:30:22</td>
                                        <td class="action-btns">
                                            <a href="view-crossing?id=1" class="btn btn-sm btn-info" title="View">
                                                <i class="bi bi-eye"></i>
                                            </a>
                                            <a href="edit-crossing?id=1" class="btn btn-sm btn-warning" title="Edit">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <button class="btn btn-sm btn-danger" title="Delete" onclick="confirmDelete(1, 'Sample Crossing 1')">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>2</td>
                                        <td>Sample Crossing 2</td>
                                        <td>456 Train Ave, City B</td>
                                        <td><span class="badge bg-warning text-dark status-badge">Under Maintenance</span></td>
                                        <td>Jane Smith</td>
                                        <td>2023-06-09 11:15:45</td>
                                        <td class="action-btns">
                                            <a href="view-crossing?id=2" class="btn btn-sm btn-info" title="View">
                                                <i class="bi bi-eye"></i>
                                            </a>
                                            <a href="edit-crossing?id=2" class="btn btn-sm btn-warning" title="Edit">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <button class="btn btn-sm btn-danger" title="Delete" onclick="confirmDelete(2, 'Sample Crossing 2')">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>3</td>
                                        <td>Sample Crossing 3</td>
                                        <td>789 Track Rd, City C</td>
                                        <td><span class="badge bg-danger status-badge">Inactive</span></td>
                                        <td>Mike Johnson</td>
                                        <td>2023-06-08 16:45:12</td>
                                        <td class="action-btns">
                                            <a href="view-crossing?id=3" class="btn btn-sm btn-info" title="View">
                                                <i class="bi bi-eye"></i>
                                            </a>
                                            <a href="edit-crossing?id=3" class="btn btn-sm btn-warning" title="Edit">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <button class="btn btn-sm btn-danger" title="Delete" onclick="confirmDelete(3, 'Sample Crossing 3')">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="deleteModalLabel">Confirm Delete</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    Are you sure you want to delete <strong id="crossingName"></strong>? This action cannot be undone.
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-danger" id="confirmDeleteBtn">Delete</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
    
    <script>
        $(document).ready(function() {
            // Initialize DataTable
            $('#crossingsTable').DataTable({
                responsive: true,
                order: [[0, 'desc']], // Sort by ID descending by default
                pageLength: 10,
                lengthMenu: [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
            });
            
            // Handle delete confirmation
            let crossingToDelete = null;
            
            window.confirmDelete = function(id, name) {
                crossingToDelete = id;
                $('#crossingName').text(name);
                $('#deleteModal').modal('show');
            };
            
            $('#confirmDeleteBtn').click(function() {
                if (crossingToDelete) {
                    // In a real application, you would make an AJAX call to delete the crossing
                    console.log('Deleting crossing with ID:', crossingToDelete);
                    // For now, just show an alert and reload the page
                    alert('Crossing deleted successfully!');
                    $('#deleteModal').modal('hide');
                    // In a real app, you would remove the row from the table or reload the page
                    // window.location.reload();
                }
            });
        });
    </script>
</body>
</html>
