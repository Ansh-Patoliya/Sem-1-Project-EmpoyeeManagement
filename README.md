# Sem-1-project

# Employee Management System

## Overview
The **Employee Management System** is a Java-based application designed to manage employee details, salaries, leave requests, and approvals efficiently. It includes role-based access for employees and managers, providing features such as profile management, salary details, leave applications, and manager approvals.

## Features
### Employee Features:
- Employee Registration and Profile Management
- Login Authentication
- View Personal Profile and Salary Details
- Apply for Leave & Check Leave Status
- Cancel Leave Requests
- Update Personal Information (Name, Email, Mobile Number, Password)

### Manager Features:
- Approve or Reject Employee Registrations
- View All Employees
- Search Employees by ID, Name, Email, or City
- Approve or Reject Leave Applications
- Update Employee Salary Details
- Modify Salary Components (DA, HRA, PF)
- View Rejected Employees List

## Technologies Used
- **Programming Language:** Java
- **Framework:** Java SE
- **IDE:** IntelliJ IDEA
- **Data Structures:** Arrays
- **Libraries:** Java Time API (for date operations), Scanner (for input handling)

## Installation & Setup
1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/employee-management-system.git
   ```
2. Open the project in **IntelliJ IDEA** or any Java IDE.
3. Compile and run the `EmployeeManagement.java` file.
4. Follow the on-screen prompts to register and log in as an employee or manager.

## How to Use
### Employee Registration:
1. Run the program and select **Employee Registration**.
2. Enter personal details including name, email, date of birth, Aadhaar number, and password.
3. The registration status remains **Pending** until approved by the manager.

### Employee Login:
1. Log in using the provided **Employee ID** and **Password**.
2. Access various features like salary details, leave applications, and profile updates.

### Manager Login:
1. Use **Manager Credentials** (e.g., Default ID: `12345678`, Password: `Admin@123`).
2. Approve or reject employee registrations and leave requests.
3. Manage salary details and view employee records.

## Future Enhancements
- Implement a database for persistent storage (MySQL or PostgreSQL).
- Develop a graphical UI using **JavaFX**.
- Integrate Email/SMS notifications for approvals and updates.
- Implement role-based authentication with a secure login system.

## Contributors
- **Ansh Patoliya** (Full Stack Developer)

## License
This project is open-source and available under the [MIT License](LICENSE).

