<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.userregistration.DashboardServlet.Crossing" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Favorite Railway Crossings</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body {
            background-color: #f5f5f5;
        }

        .navbar {
            background-color: #4CAF50;
            padding: 15px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            color: white;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }

        .logo {
            font-size: 24px;
            font-weight: bold;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .nav-link {
            color: white;
            text-decoration: none;
            margin-right: 15px;
            display: inline-flex;
            align-items: center;
        }

        .nav-link i {
            margin-right: 5px;
        }

        .nav-link:hover, .user-info a:hover {
            text-decoration: underline;
        }

        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .title {
            font-size: 24px;
            color: #333;
        }

        .card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            overflow: hidden;
            margin-bottom: 20px;
        }

        .card-header {
            background-color: #f8f9fa;
            padding: 15px 20px;
            border-bottom: 1px solid #eee;
            font-weight: bold;
            color: #333;
        }

        .card-body {
            padding: 0;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 15px 20px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }

        th {
            background-color: #f8f9fa;
            font-weight: 600;
            color: #555;
        }

        tr:hover {
            background-color: #f5f5f5;
        }

        .status {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
            text-transform: capitalize;
        }

        .status.active {
            background-color: #e6f7e6;
            color: #2e7d32;
        }

        .status.inactive {
            background-color: #ffebee;
            color: #c62828;
        }

        .favorite-btn {
            background: none;
            border: none;
            cursor: pointer;
            font-size: 1.2em;
            padding: 5px;
            color: #ffc107;
            transition: color 0.2s;
        }

        .favorite-btn:focus {
            outline: none;
        }

        .no-favorites {
            text-align: center;
            padding: 40px 20px;
            color: #666;
        }

        .no-favorites i {
            font-size: 48px;
            color: #ddd;
            margin-bottom: 15px;
        }

        .btn {
            display: inline-block;
            padding: 8px 16px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            font-size: 14px;
        }

        .btn i {
            margin-right: 5px;
        }

        .btn:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="navbar">
        <div class="logo">Railway Crossing</div>
        <div class="nav-right">
            <a href="dashboard.jsp" class="nav-link">
                <i class="fas fa-home"></i> Dashboard
            </a>
            <a href="favorites.jsp" class="nav-link active">
                <i class="fas fa-heart"></i> My Favorites
            </a>
            <a href="profile.jsp" class="nav-link">
                <i class="fas fa-user"></i> Profile
            </a>
            <a href="adminLogin.jsp" class="nav-link" style="color: #ffc107;">
                <i class="fas fa-user-shield"></i> Admin
            </a>
            <a href="LogoutServlet" class="nav-link">
                <i class="fas fa-sign-out-alt"></i> Logout
            </a>
        </div>
    </div>

    <div class="container">
        <div class="header">
            <h1 class="title">My Favorite Railway Crossings</h1>
            <a href="dashboard.jsp" class="btn"><i class="fas fa-arrow-left"></i> Back to Dashboard</a>
        </div>

        <div class="card">
            <div class="card-body">
                <table>
                    <thead>
                        <tr>
                            <th>Sr. No</th>
                            <th>Name</th>
                            <th>Address</th>
                            <th>Landmark</th>
                            <th>Schedule</th>
                            <th>Person In-Charge</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="favoritesTable">
                        <tr>
                            <td colspan="8" class="no-favorites">
                                <i class="far fa-star"></i>
                                <p>You haven't added any crossings to your favorites yet.</p>
                                <a href="dashboard.jsp" class="btn" style="margin-top: 15px;">
                                    <i class="fas fa-search"></i> Browse Crossings
                                </a>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Fetch and display favorite crossings
            fetchFavoriteCrossings();
            
            // Handle remove from favorites
            document.addEventListener('click', function(e) {
                if (e.target.closest('.remove-favorite')) {
                    e.preventDefault();
                    const button = e.target.closest('.remove-favorite');
                    const crossingId = button.getAttribute('data-crossing-id');
                    if (crossingId) {
                        removeFromFavorites(button, crossingId);
                    }
                }
            });
            
            // Fetch favorite crossings from server
            function fetchFavoriteCrossings() {
                fetch('/user-registration/favorites')
                    .then(response => response.json())
                    .then(data => {
                        updateFavoritesTable(data);
                    })
                    .catch(error => {
                        console.error('Error fetching favorites:', error);
                        showNotification('Error loading favorites', 'error');
                    });
            }
            
            // Update the favorites table with data
            function updateFavoritesTable(crossings) {
                const tbody = document.getElementById('favoritesTable');
                
                if (!crossings || crossings.length === 0) {
                    tbody.innerHTML = `
                        <tr>
                            <td colspan="8" class="no-favorites">
                                <i class="far fa-star"></i>
                                <p>You haven't added any crossings to your favorites yet.</p>
                                <a href="dashboard.jsp" class="btn" style="margin-top: 15px;">
                                    <i class="fas fa-search"></i> Browse Crossings
                                </a>
                            </td>
                        </tr>`;
                    return;
                }
                
                let html = '';
                crossings.forEach((crossing, index) => {
                    // Format status display
                    let statusText = '-';
                    let statusClass = 'inactive';
                    
                    if (crossing.status) {
                        if (crossing.status.toLowerCase() === 'active') {
                            statusText = 'OPEN';
                            statusClass = 'active';
                        } else if (crossing.status.toLowerCase() === 'inactive') {
                            statusText = 'CLOSED';
                            statusClass = 'inactive';
                        } else {
                            statusText = crossing.status.toUpperCase();
                        }
                    }
                    
                    html += `
                        <tr>
                            <td>${index + 1}</td>
                            <td>${escapeHtml(crossing.name || '')}</td>
                            <td>${escapeHtml(crossing.address || '')}</td>
                            <td>${escapeHtml(crossing.landmark || '-')}</td>
                            <td>${escapeHtml(crossing.trainSchedule || '-')}</td>
                            <td>${escapeHtml(crossing.platformInCharge || '-')}</td>
                            <td>
                                <span class="status ${statusClass}">
                                    ${statusText}
                                </span>
                            </td>
                            <td>
                                <button class="favorite-btn remove-favorite" data-crossing-id="${crossing.id}" title="Remove from favorites">
                                    <i class="fas fa-star"></i>
                                </button>
                            </td>
                        </tr>`;
                });
                
                tbody.innerHTML = html;
            }
            
            // Remove from favorites
            function removeFromFavorites(button, crossingId) {
                fetch('/user-registration/favorite', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `action=remove&crossingId=${crossingId}`
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        showNotification('Removed from favorites');
                        // Refresh the list
                        fetchFavoriteCrossings();
                    } else {
                        showNotification(data.error || 'Failed to remove from favorites', 'error');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    showNotification('An error occurred', 'error');
                });
            }
            
            // Helper function to escape HTML
            function escapeHtml(unsafe) {
                if (unsafe === null || unsafe === undefined) return '';
                return unsafe
                    .toString()
                    .replace(/&/g, '&amp;')
                    .replace(/</g, '&lt;')
                    .replace(/>/g, '&gt;')
                    .replace(/"/g, '&quot;')
                    .replace(/'/g, '&#039;');
            }
            
            // Show notification
            function showNotification(message, type = 'success') {
                // Remove any existing notifications
                const existing = document.querySelector('.notification');
                if (existing) existing.remove();
                
                const notification = document.createElement('div');
                notification.className = `notification ${type}`;
                notification.textContent = message;
                document.body.appendChild(notification);
                
                // Auto-remove notification after 3 seconds
                setTimeout(() => {
                    notification.style.opacity = '0';
                    setTimeout(() => notification.remove(), 300);
                }, 3000);
            }
            
            // Add notification styles if not already present
            if (!document.getElementById('notification-styles')) {
                const style = document.createElement('style');
                style.id = 'notification-styles';
                style.textContent = `
                    .notification {
                        position: fixed;
                        bottom: 20px;
                        right: 20px;
                        padding: 15px 25px;
                        border-radius: 4px;
                        color: white;
                        background-color: #4CAF50;
                        box-shadow: 0 2px 10px rgba(0,0,0,0.2);
                        z-index: 1000;
                        opacity: 0;
                        transform: translateY(20px);
                        transition: opacity 0.3s, transform 0.3s;
                    }
                    .notification.error {
                        background-color: #f44336;
                    }
                    .notification.show {
                        opacity: 1;
                        transform: translateY(0);
                    }
                `;
                document.head.appendChild(style);
                
                // Trigger reflow to enable animation
                setTimeout(() => {
                    const notifications = document.querySelectorAll('.notification');
                    notifications.forEach(notif => {
                        notif.classList.add('show');
                    });
                }, 10);
            } else {
                // If styles are already added, just show the notification
                setTimeout(() => {
                    const notification = document.querySelector('.notification');
                    if (notification) notification.classList.add('show');
                }, 10);
            }
        });
    </script>
</body>
</html>
