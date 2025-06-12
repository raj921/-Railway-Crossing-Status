# User Registration System

A simple JSP/Servlet-based user registration and login system with MySQL database integration.

## Prerequisites

- Java Development Kit (JDK) 8 or later
- Apache Tomcat 9.x or later
- MySQL Server 5.7 or later
- Maven (for building the project)

## Setup Instructions

### 1. Database Setup

1. Create a new MySQL database:
   ```sql
   CREATE DATABASE userdb;
   ```

2. The application will automatically create the `users` table on first run.

### 2. Database Configuration

Update the database connection details in `src/main/webapp/WEB-INF/classes/DatabaseUtil.java` if needed:
- `URL`: JDBC connection URL (default: `jdbc:mysql://localhost:3306/userdb`)
- `USER`: MySQL username (default: `root`)
- `PASSWORD`: MySQL password (default: empty)

### 3. Build the Project

1. Navigate to the project root directory
2. Run the following command to build the project:
   ```bash
   mvn clean package
   ```
   This will create a WAR file in the `target` directory.

### 4. Deploy to Tomcat

1. Copy the generated WAR file to Tomcat's `webapps` directory
2. Start Tomcat server

### 5. Access the Application

Open a web browser and navigate to:
```
http://localhost:8080/your-app-name/
```

## Features

- User registration with name, email, and password
- Input validation
- Password hashing for security
- Session management
- Responsive design
- Error handling

## Project Structure

```
src/main/webapp/
├── WEB-INF/
│   ├── classes/
│   │   ├── DatabaseUtil.java    # Database connection utility
│   │   ├── User.java            # User model
│   │   ├── UserDAO.java         # Data Access Object for users
│   │   ├── RegisterServlet.java # Handles user registration
│   │   ├── LoginServlet.java    # Handles user login
│   │   └── LogoutServlet.java   # Handles user logout
│   └── web.xml                  # Web application configuration
├── css/
│   └── style.css             # Global styles
├── register.jsp                # Registration page
├── login.jsp                   # Login page
├── welcome.jsp                 # Welcome page after login
├── error404.jsp                # 404 error page
└── error500.jsp                # 500 error page
```

## Security Notes

- Passwords are hashed using SHA-256 before storing in the database
- Session management is implemented for authenticated users
- Input validation is performed on both client and server sides
- Error messages are generic to avoid information leakage

## Dependencies

- Java Servlet API 4.0+
- JSTL 1.2
- MySQL Connector/J 8.0+
- JSP API 2.3+

## Troubleshooting

1. **Database Connection Issues**
   - Ensure MySQL server is running
   - Verify database credentials in `DatabaseUtil.java`
   - Check if the MySQL connector is in the classpath

2. **Deployment Issues**
   - Ensure Tomcat is running
   - Check Tomcat logs for errors
   - Verify the WAR file is deployed correctly

3. **Application Issues**
   - Clear browser cache if you experience any display issues
   - Check server logs for detailed error messages

## License

This project is open source and available under the MIT License.
# -Railway-Crossing-Status
