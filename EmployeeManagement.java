package Project;

import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

class Person {
    // Declaring attributes of a person
    String firstName, lastName, fullName, emailId, dateOfBirth, mobileNo, aadhaarNo, address;

    // Default constructor (creates an empty person object)
    Person() {
    }

    // Parameterized constructor to initialize a person's details
    Person(String firstName, String lastName, String emailId, String dateOfBirth, String mobileNo, String aadhaarNo, String address) {
        // Assigning values to instance variables using 'this' keyword
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.emailId = emailId;
        this.mobileNo = mobileNo;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.aadhaarNo = aadhaarNo;
    }

    // Method to display the person's profile details
    void displayProfile() {
        System.out.println("Full Name     :- " + fullName);
        System.out.println("Email Id      :- " + emailId);
        System.out.println("Date Of Birth :- " + dateOfBirth);
        System.out.println("Mobile No     :- " + mobileNo);
        System.out.println("Aadhar No     :- " + aadhaarNo);
        System.out.println("Address       :- " + address);
    }
}

class RegisteredEmployee extends Person {
    // Employee-specific attributes
    String password;
    String status;

    RegisteredEmployee() {
        super();
    }

    RegisteredEmployee(String firstName, String lastName, String emailId, String dateOfBirth,
                       String mobileNo, String aadharNo, String address, String password) {
        super(firstName, lastName, emailId, dateOfBirth, mobileNo, aadharNo, address); // Calls the Person constructor
        this.password = password;
        this.status = "Pending";
    }

    // Display registered employee profile details
    void displayProfile() {
        super.displayProfile(); // Calls Person's displayProfile() method
        System.out.println("Hire status   :- " + status);
    }
}

// Employee class extending Person, inheriting personal details
class Employee extends RegisteredEmployee {

    String employeeId;
    double basicSalary, totalSalary;

    // Static variables for salary calculation
    static double da = 0.5, hra = 0.2, pf = 0.1; // DA: Dearness Allowance, HRA: House Rent Allowance, PF: Provident Fund

    // Array to store leave requests, with a maximum of 10 requests
    LeaveRequest[] leaveRequests = new LeaveRequest[10];

    // Track the number of leave requests and remaining leave days
    int leaveRequestCount = 0, remainLeaveDay;

    // Default constructor
    Employee() {
        super(); // Calls the parent class (Person) constructor
    }

    // Parameterized constructor to initialize employee details
    Employee(String firstName, String lastName, String emailId, String dateOfBirth,
             String mobileNo, String aadharNo, String address, String password,
             String employeeId, double basicSalary) {
        super(firstName, lastName, emailId, dateOfBirth, mobileNo, aadharNo, address, password); // Calls the RegisteredEmployee constructor
        this.employeeId = employeeId;
        this.basicSalary = basicSalary;
        this.totalSalary = calculateTotalSalary(); // Calculate total salary during initialization
        this.remainLeaveDay = 20; // Default leave days set to 20
    }

    // Setters for DA, HRA, and PF (used to update salary calculation percentages)
    void setDa(double da) {
        Employee.da = da;
    }

    void setHra(double hra) {
        Employee.hra = hra;
    }

    void setPf(double pf) {
        Employee.pf = pf;
    }

    // Display employee profile details
    void displayProfile() {
        super.displayProfile(); // Calls registered displayProfile() method
        System.out.println("Employee ID   :- " + employeeId);
        System.out.println("Hire status   :- " + status);
    }

    // Method to calculate total salary based on allowances and deductions
    double calculateTotalSalary() {
        return basicSalary + (basicSalary * Employee.da) + (basicSalary * Employee.hra) - (basicSalary * Employee.pf);
    }

    // Display salary details including basic salary, total salary, DA, HRA, and PF
    void salaryDetails() {
        System.out.println("Basic Salary :- " + basicSalary);
        System.out.println("Total Salary :- " + totalSalary);
        System.out.println("DA (" + da + ")    :- " + (basicSalary * da));
        System.out.println("HRA (" + hra + ")   :- " + (basicSalary * hra));
        System.out.println("PF (" + pf + ")    :- " + (basicSalary * pf));
    }

    // Recalculate and update the total salary
    void setTotalSalary() {
        totalSalary = calculateTotalSalary();
        System.out.println("Total Salary :- " + totalSalary);
    }

    // Method for an employee to apply for leave
    void applyForLeave(Scanner sc) {

        int loginIndex = EmployeePortal.loginIndex;
        if (EmployeeManagement.employee[EmployeePortal.loginIndex].leaveRequestCount == 10) {
            System.out.println("Cannot apply for more leaves. Maximum limit reached.");
            return;
        }
        // Check if the employee has remaining leave days
        if (EmployeeManagement.employee[loginIndex].remainLeaveDay == 0) {
            System.out.println("Cannot apply for more leaves. No remaining leave days.");
            return;
        }

        System.out.println("---> Your remaining leave days: " + EmployeeManagement.employee[loginIndex].remainLeaveDay);
        sc.nextLine(); // Consume the newline character
        System.out.println("\n============ Apply for Leave =============");
        System.out.print("--> Enter leave reason: ");
        String reason = sc.nextLine();
        System.out.print("--> Enter how many days you want leave for: ");
        int leaveDay = sc.nextInt();

        // Check if requested leave days exceed remaining leave days
        if (EmployeeManagement.employee[loginIndex].remainLeaveDay < leaveDay) {
            System.out.println("Cannot apply for more leaves. Exceeds remaining leave days.");
            return;
        }

        // Deduct leave days and store the leave request
        EmployeeManagement.employee[loginIndex].remainLeaveDay -= leaveDay;
        EmployeeManagement.employee[loginIndex].leaveRequests[EmployeeManagement.employee[loginIndex].leaveRequestCount++] = new LeaveRequest(employeeId, reason, leaveDay);
        System.out.println("Your leave application has been submitted successfully, " + EmployeeManagement.employee[loginIndex].fullName + ". Manager will review and confirm shortly.");
    }

    // Check the status of leave requests for an employee
    void checkLeaveStatus() {
        int loginIndex = EmployeePortal.loginIndex;
        System.out.println("\n======== Leave Status ========");

        // If there are no leave requests
        if (EmployeeManagement.employee[loginIndex].leaveRequestCount == 0) {
            System.out.println("No leave requests found.");
            return;
        }

        System.out.println("Hi " + EmployeeManagement.employee[loginIndex].fullName + ", you're currently viewing the status of your leave application.");
        for (int j = 0; j < EmployeeManagement.employee[loginIndex].leaveRequestCount; j++) {
            EmployeeManagement.employee[loginIndex].leaveRequests[j].displayLeaveRequest(); // Call method to display leave request details
            System.out.println("-------------------------------");
        }

    }

    // Cancel a pending leave request
    void cancelLeaveRequest(Scanner sc) {
        System.out.println("\n============ Cancel Leave Request ============");

        int loginIndex = EmployeePortal.loginIndex;
        System.out.println("Leave Requests for Employee ID: " + employeeId);

        boolean hasPendingRequest = false;

        // Display all pending leave requests
        for (int j = 0; j < EmployeeManagement.employee[loginIndex].leaveRequestCount; j++) {
            if (EmployeeManagement.employee[loginIndex].leaveRequests[j].leaveStatus.equalsIgnoreCase("Pending")) {
                System.out.println("[" + (j + 1) + "] Leave Days: " + EmployeeManagement.employee[loginIndex].leaveRequests[j].leaveDays +
                        ", Reason: " + EmployeeManagement.employee[loginIndex].leaveRequests[j].leaveReason +
                        ", Status: " + EmployeeManagement.employee[loginIndex].leaveRequests[j].leaveStatus);
                hasPendingRequest = true;
            }
        }

        // If there are no pending leave requests
        if (!hasPendingRequest) {
            System.out.println("No pending leave requests found for this employee.");
            return;
        }

        System.out.print("--> Enter the leave request number to cancel: ");
        int leaveIndex = sc.nextInt() - 1;

        // Validate input and ensure the leave request is pending
        if (leaveIndex >= 0 && leaveIndex < EmployeeManagement.employee[loginIndex].leaveRequestCount &&
                EmployeeManagement.employee[loginIndex].leaveRequests[leaveIndex].leaveStatus.equalsIgnoreCase("Pending")) {

            // Re-add canceled leave days
            EmployeeManagement.employee[loginIndex].remainLeaveDay += EmployeeManagement.employee[loginIndex].leaveRequests[leaveIndex].leaveDays;

            // Shift leave requests in array to remove the canceled request
            for (int k = leaveIndex; k < EmployeeManagement.employee[loginIndex].leaveRequestCount - 1; k++) {
                EmployeeManagement.employee[loginIndex].leaveRequests[k] = EmployeeManagement.employee[loginIndex].leaveRequests[k + 1];
            }
            EmployeeManagement.employee[loginIndex].leaveRequests[EmployeeManagement.employee[loginIndex].leaveRequestCount - 1] = null;
            EmployeeManagement.employee[loginIndex].leaveRequestCount--;

            System.out.println("Leave request canceled successfully for Employee ID: " + employeeId);
        } else {
            System.out.println("Invalid leave request number or leave is not pending.");
        }
    }
}

class Manager {

    void approveEmployee(RegisteredEmployee[] registeredEmployee, int registeredEmployeeCount, Scanner sc) {
        boolean found = false;

        System.out.println("\n========== Pending Employee Approvals ==========");
        for (int i = 0; i < registeredEmployeeCount; i++) {
            if (registeredEmployee[i].status.equalsIgnoreCase("Pending")) {
                found = true;
                System.out.println("[" + (i + 1) + "] .");
                System.out.println("    Name      : " + registeredEmployee[i].fullName);
                System.out.println("    Email     : " + registeredEmployee[i].emailId);
                System.out.println("    Mobile    : " + registeredEmployee[i].mobileNo);
                System.out.println("-----------------------------------------");
            }
        }

        if (!found) {
            System.out.println("---> No pending approvals.");
            return;
        }

        System.out.print("--> Enter employee number to approve/reject :- ");
        int select = sc.nextInt();

        if (select < 0 || select > registeredEmployeeCount) {
            System.out.println("Error: Unable to find the selected employee. Please try again.");
            return;
        }

        // Find actual index of selected employee
        int pendingIndex = -1, count = 0;
        for (int i = 0; i < registeredEmployeeCount; i++) {
            if (registeredEmployee[i].status.equalsIgnoreCase("Pending")) {
                count++;
                if (count == select) { // Matching user choice with pending list
                    pendingIndex = i;
                    break;
                }
            }
        }

        // If pendingIndex is still -1, it means no valid selection was made
        if (pendingIndex == -1) {
            System.out.println("Error: Unable to find the selected employee. Please try again.");
            return;
        }

        System.out.println("Name: " + registeredEmployee[pendingIndex].fullName);
        System.out.print("--> Approve this employee? (yes/no): ");
        String decision = sc.next();

        if (decision.equalsIgnoreCase("yes")) {
            registeredEmployee[pendingIndex].status = "Approved";

            double basicSalary = -1;
            // Taking and validating basic salary input
            while (basicSalary < 0) {
                System.out.print("--> Enter Your Basic Salary: ");
                if (!sc.hasNextDouble()) {
                    System.out.println(" Please enter a valid number.");
                    sc.next();
                    continue;
                }
                basicSalary = sc.nextDouble();
                if (basicSalary < 0) {
                    System.out.println(" Invalid salary. Must be a positive number.");
                }
            }

            EmployeeManagement.employee[EmployeePortal.employeeCount++] = new Employee(registeredEmployee[pendingIndex].firstName, registeredEmployee[pendingIndex].lastName, registeredEmployee[pendingIndex].emailId, registeredEmployee[pendingIndex].dateOfBirth, registeredEmployee[pendingIndex].mobileNo, registeredEmployee[pendingIndex].aadhaarNo, registeredEmployee[pendingIndex].address, registeredEmployee[pendingIndex].password, new EmployeePortal().createEmployeeId(registeredEmployee[pendingIndex].dateOfBirth), basicSalary);
            System.out.println("---> Employee approved successfully.");
        } else if ((decision.equalsIgnoreCase("no"))) {
            registeredEmployee[pendingIndex].status = "Rejected";
            System.out.println("---> Employee rejected successfully.");
        } else
            System.out.println("---> Enter valid decision.");
    }

    void rejectedEmployee(RegisteredEmployee[] registeredEmployee) {
        boolean found = false;
        for (int i = 0; i < EmployeePortal.registeredEmployeeCount; i++) {
            if (registeredEmployee[i].status.equalsIgnoreCase("Rejected")) {
                found = true;
                System.out.println("[" + (i + 1) + "] .");
                System.out.println("    Name      : " + registeredEmployee[i].fullName);
                System.out.println("    Email     : " + registeredEmployee[i].emailId);
                System.out.println("    Mobile    : " + registeredEmployee[i].mobileNo);
                System.out.println("-----------------------------------------");
            }
        }
        if (!found)
            System.out.println("---> Not found any rejected employees");
    }

    // Method to display the list of all employee
    void viewAllEmployeeList() {
        boolean found = false;
        for (int i = 0; i < EmployeePortal.employeeCount; i++) {
            found = true;
            System.out.println("--> Employee " + (i + 1) + " :-- ");
            System.out.println("--------------------------------------------------------------");
            System.out.println("Employee ID  :- " + EmployeeManagement.employee[i].employeeId);
            System.out.println("Full Name    :- " + EmployeeManagement.employee[i].fullName);
            System.out.println("Email Id     :- " + EmployeeManagement.employee[i].emailId);
            System.out.println("Mobile No    :- " + EmployeeManagement.employee[i].mobileNo);
            // Updating and displaying total salary details
            EmployeeManagement.employee[i].setTotalSalary();
            System.out.println();
        }
        if (!found)
            System.out.println("---> Not found any approved employees");
    }

    // Method to update an employee's salary based on employee ID
    void updateEmployeealary(String id, Scanner sc) {
        int loginIndex = EmployeePortal.loginIndex;
        for (int i = 0; i < EmployeePortal.employeeCount; i++) {
            if (EmployeeManagement.employee[i].employeeId.equals(id)) {
                System.out.print("Enter Employee New Basic Salary :- ");
                // Updating the employee's basic salary
                EmployeeManagement.employee[i].basicSalary = sc.nextDouble();
                // Recalculating the total salary based on the updated basic salary
                EmployeeManagement.employee[i].setTotalSalary();
                System.out.println("The manager has successfully updated the salary details for employee ID "
                        + EmployeeManagement.employee[i].employeeId + " in the system.");
                break;
            }
        }
    }

    // Method to search for an employee based on different attributes (ID, Name, Email, or City)
    void searchEmployee(String type, String value) {
        boolean found = false;
        int loginIndex = EmployeePortal.loginIndex;
        for (int i = 0; i < EmployeePortal.employeeCount; i++) {
            // Matching based on selected search type
            if ((type.equals("id") && EmployeeManagement.employee[i].employeeId.equals(value)) ||
                    (type.equals("name") && EmployeeManagement.employee[i].firstName.equalsIgnoreCase(value)) ||
                    (type.equals("email") && EmployeeManagement.employee[i].emailId.equals(value)) ||
                    (type.equals("city") && EmployeeManagement.employee[i].address.equals(value))) {

                System.out.println("--> Employee " + (i + 1) + " :-- ");
                System.out.println("--------------------------------------------------------------");
                System.out.println("Employee ID  :- " + EmployeeManagement.employee[i].employeeId);
                System.out.println("Full Name    :- " + EmployeeManagement.employee[i].fullName);
                System.out.println("Email Id     :- " + EmployeeManagement.employee[i].emailId);
                System.out.println("Mobile No    :- " + EmployeeManagement.employee[i].mobileNo);
                System.out.println("City         :- " + EmployeeManagement.employee[i].address);
                System.out.println("Total Salary :- " + EmployeeManagement.employee[i].totalSalary);
                System.out.println("--------------------------------------------------------------");

                found = true;
                break;
            }
        }
        // If no employee matches the search criteria
        if (!found) {
            System.out.println("Employee not found...");
        }
    }

    // Method to approve or reject leave requests of employee
    void approveLeave(Scanner sc) {

        System.out.println("\n=========== Approve Leave Requests ==============");
        int no = 0; // Counter for leave requests

        for (int i = 0; i < EmployeePortal.employeeCount; i++) {
            // If an employee has no leave requests
            if (EmployeeManagement.employee[i].leaveRequestCount == 0) {
                System.out.println("No leave requests to approve for Employee ID: " + EmployeeManagement.employee[i].employeeId);
                continue;
            }

            for (int j = 0; j < EmployeeManagement.employee[i].leaveRequestCount; j++) {
                // Skip already approved or rejected leave requests
                if (EmployeeManagement.employee[i].leaveRequests[j].leaveStatus.equalsIgnoreCase("Approved") ||
                        EmployeeManagement.employee[i].leaveRequests[j].leaveStatus.equalsIgnoreCase("Rejected")) {
                    continue;
                }

                // Displaying leave request details
                System.out.println("\n---> " + (++no) + ".\nLeave Request #" + (j + 1) + " for Employee ID: " + EmployeeManagement.employee[i].employeeId);
                EmployeeManagement.employee[i].leaveRequests[j].displayLeaveRequest();

                // Asking manager for approval decision
                while (true) {
                    System.out.print("Approve this leave? (yes/no) (press 0 to keep pending): ");
                    String decision = sc.next();

                    if (decision.equalsIgnoreCase("yes")) {
                        // Approving leave request
                        EmployeeManagement.employee[i].leaveRequests[j].leaveStatus = "Approved";
                        System.out.println("Leave request for employee ID " + EmployeeManagement.employee[i].employeeId + " has been successfully approved by the manager.");
                        break;
                    } else if (decision.equalsIgnoreCase("no")) {
                        // Rejecting leave request and restoring leave days
                        EmployeeManagement.employee[i].leaveRequests[j].leaveStatus = "Rejected";
                        EmployeeManagement.employee[i].remainLeaveDay += EmployeeManagement.employee[i].leaveRequests[j].leaveDays;
                        System.out.println("Leave request for employee ID " + EmployeeManagement.employee[i].employeeId + " has been rejected by the manager.");
                        break;
                    } else if (decision.equals("0")) {
                        // Keeping leave request pending
                        System.out.println("Manager is currently not responding to your requests.");
                        break;
                    } else {
                        System.out.println("Please enter a valid decision (yes/no/0).");
                    }
                    System.out.println("---------------------------------");
                }
            }
        }
    }
}

// Class representing an employee's leave request
class LeaveRequest {
    String employeeId, leaveReason, leaveStatus; // Employee ID, reason for leave, and leave approval status
    int leaveDays; // Number of leave days requested

    // Constructor to initialize a leave request
    LeaveRequest(String employeeId, String leaveReason, int leaveDays) {
        this.employeeId = employeeId;
        this.leaveReason = leaveReason;
        this.leaveDays = leaveDays;
        this.leaveStatus = "Pending"; // Default status is "Pending"
    }

    // Method to display leave request details
    void displayLeaveRequest() {
        System.out.println("Employee ID     : " + employeeId);
        System.out.println("Leave Reason    : " + leaveReason);
        System.out.println("Leave Days      : " + leaveDays);
        System.out.println("Leave Status    : " + leaveStatus);
    }
}

// Class containing various validation methods for employee details
class Validation {

    // Method to validate name (only uppercase alphabets allowed)
    boolean checkName(String name) {
        boolean valid = true;
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (!(ch >= 'A' && ch <= 'Z')) { // Checking if the character is not an uppercase letter
                System.out.println("!Please enter a valid name (only letters, no numbers or special characters).\n");
                valid = false;
                break;
            }
        }
        return valid;
    }

    // Method to validate email format (must contain '@' and domain must be "gmail.com")
    boolean checkEmail(String email) {
        boolean valid = true;
        int atCount = 0;

        // Counting occurrences of '@'
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                atCount++;
            }
        }

        if (atCount != 1) { // Email must contain exactly one '@'
            System.out.println("Enter email in invalid format");
            return false;
        }

        if (email.equals("@gmail.com")) { // (@gmail.com) any user enter this type gmail
            System.out.println("Only '@gmail.com' is not allowed as input.");
            return false;
        }

        String divideEmail[] = email.split("@");

        // Checking local part (before '@') for valid characters
        for (int i = 0; i < divideEmail[0].length(); i++) {
            char ch = divideEmail[0].charAt(i);
            if (!((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9'))) {
                valid = false;
                System.out.println("Enter email in invalid format");
                break;
            }
        }

        // Ensuring domain is "gmail.com"
        if (divideEmail.length < 2 || !divideEmail[1].equals("gmail.com")) {
            System.out.println("Enter email in invalid format");
            valid = false;
        }
        return valid;
    }

    // Method to validate date of birth format (DD/MM/YYYY) and valid range
    boolean checkDateOfBirth(String dateOfBirth) {
        int count = 0;

        // Counting number of '/' characters
        for (int i = 0; i < dateOfBirth.length(); i++) {
            if (dateOfBirth.charAt(i) == '/')
                count++;
        }

        if (count != 2) {
            System.out.println("Enter date in valid format (e.g. 10/10/2001)");
            return false;
        }

        String[] date = dateOfBirth.split("/");

        int dateCheck = Integer.parseInt(date[0]);
        int monthCheck = Integer.parseInt(date[1]);
        int yearCheck = Integer.parseInt(date[2]);

        boolean isLeapYear = (yearCheck % 400 == 0) || (yearCheck % 4 == 0 && yearCheck % 100 != 0);

        // Validating day and month values
        int maxDays = 0;
        switch (monthCheck) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                maxDays = 31;
                break;

            case 4:
            case 6:
            case 9:
            case 11:
                maxDays = 30;
                break;

            case 2:
                if (isLeapYear)
                    maxDays = 29;
                else
                    maxDays = 28;
                break;

            default:
                System.out.println("Enter valid month");
                return false;
        }

        if (dateCheck < 1 || dateCheck > maxDays) {
            System.out.println("enter valid day");
            return false;
        }

        // Get today's date
        LocalDate today = LocalDate.now();

        // Convert to LocalDate
        LocalDate birthDate = LocalDate.of(yearCheck, monthCheck, dateCheck);

        // Calculate exact age
        int age = Period.between(birthDate, today).getYears();

        // Check if age is between 18 and 60
        if (age < 18 || age > 60) {
            System.out.println("! Age must be between 18 and 60 years.");
            return false;
        }
        return true;
    }

    // Method to validate mobile number (must be 10 digits and start with 9, 8, 7, or 6)
    boolean checkMobileNo(String mobile) {
        boolean valid = false;
        if (mobile.length() == 10) {
            if (mobile.startsWith("9") || mobile.startsWith("8") || mobile.startsWith("7") || mobile.startsWith("6")) {
                valid = true;
                for (int i = 0; i < mobile.length(); i++) {
                    char ch = mobile.charAt(i);
                    if (!(ch >= '0' && ch <= '9')) {
                        System.out.println("! Mobile number must contain only digits.\n");
                        valid = false;
                        break;
                    }
                }
            } else {
                System.out.println("The mobile number must start with 9, 8, 7, or 6.");
            }
        } else {
            System.out.println("! Mobile number must be exactly 10 digits.\n");
        }
        return valid;
    }

    // Method to validate Aadhaar number (must be exactly 12 digits)
    boolean checkAadharNo(String aadhar) {
        boolean valid = false;
        if (aadhar.length() == 12) {
            valid = true;
            for (int i = 0; i < aadhar.length(); i++) {
                char ch = aadhar.charAt(i);
                if (!(ch >= '0' && ch <= '9')) {
                    System.out.println("! Aadhaar number must contain only digits.\n");
                    valid = false;
                    break;
                }
            }
        } else {
            System.out.println("! Aadhaar number must be exactly 12 digits.\n");
        }
        return valid;
    }

    // Method to validate address length (between 6 and 100 characters)
    boolean checkAddress(String address) {
        boolean valid = false;
        if (address.length() >= 6 && address.length() <= 100) {
            valid = true;
        } else {
            System.out.println("! Address must be between 6 and 100 characters long.\n");
        }
        return valid;
    }

    // Method to validate password strength
    boolean checkPassword(String password, Scanner sc) {
        boolean valid = false, isUppercase = false, isLowercase = false, isNumber = false, isSpecialCh = false;

        if (password.length() >= 8) {
            for (char ch : password.toCharArray()) {
                if (Character.isUpperCase(ch)) isUppercase = true;
                else if (Character.isLowerCase(ch)) isLowercase = true;
                else if (Character.isDigit(ch)) isNumber = true;
                else isSpecialCh = true;

                if (isUppercase && isLowercase && isNumber && isSpecialCh) {
                    valid = true;
                    break;
                }
            }

            if (!isUppercase) System.out.println("Add at least one uppercase letter.");
            if (!isLowercase) System.out.println("Add at least one lowercase letter.");
            if (!isNumber) System.out.println("Add at least one digit.");
            if (!isSpecialCh) System.out.println("Add at least one special character.");

            // Confirm password
            if (valid) {
                System.out.print("--> Confirm Password :- ");
                String confirmPassword = sc.next();
                if (!confirmPassword.equals(password)) {
                    valid = false;
                    System.out.println("Passwords do not match.");
                }
            }
        }
        return valid;
    }
}

class EmployeePortal extends Validation {

    // Static variable to track the number of employee
    static int employeeCount;
    static int registeredEmployeeCount;
    static int loginIndex;

    // Unique ID counter for generating employee IDs
    static int uniqueIdCounter = 101;

    // Method to generate a unique employee ID based on date of birth
    String createEmployeeId(String dateOfBirth) {
        String date[] = dateOfBirth.split("/"); // Splitting the date into day, month, and year
        return "13310" + date[0] + date[1] + uniqueIdCounter++; // Concatenating to form a unique ID
    }

    // Method for employee registration
    RegisteredEmployee employeeRegistration(Scanner sc) {

        boolean valid = false;
        String fname = null, lname = null, aadhar = null, mobile = null, address = null, email = null, dateOfBirth = null, password = null;

        // Taking and validating first name input
        while (!valid) {
            System.out.print("--> Enter First Name (ABC):- ");
            fname = sc.next();
            valid = checkName(fname.toUpperCase()); // Checking if the name is valid
            if (valid)
                fname = fname.substring(0, 1).toUpperCase() + fname.substring(1).toLowerCase(); // Formatting the name
        }

        // Taking and validating last name input
        valid = false;
        while (!valid) {
            System.out.print("--> Enter Last Name (ABC):- ");
            lname = sc.next();
            valid = checkName(lname.toUpperCase());
            if (valid)
                lname = lname.substring(0, 1).toUpperCase() + lname.substring(1).toLowerCase();
        }

        // Taking and validating email input
        valid = false;
        while (!valid) {
            System.out.print("--> Enter Email-ID (abc12@gmail.com):- ");
            email = sc.next();
            valid = checkEmail(email);
        }

        // Taking and validating date of birth input
        valid = false;
        while (!valid) {
            System.out.print("--> Enter Date Of Birth (DD/MM/YYYY) :- ");
            dateOfBirth = sc.next();
            valid = checkDateOfBirth(dateOfBirth);
            if (valid) {
                String datePart[] = dateOfBirth.split("/");
                if (datePart[1].length() == 1) {
                    dateOfBirth = datePart[0] + "/0" + datePart[1] + "/" + datePart[2]; // Formatting month if needed
                }
                if (datePart[0].length() == 1) {
                    dateOfBirth = "0" + dateOfBirth; // Formatting day if needed
                }
            }
        }

        // Taking and validating mobile number input
        valid = false;
        while (!valid) {
            System.out.print("--> Enter Mobile Number (10 digits) :- ");
            mobile = sc.next();
            valid = checkMobileNo(mobile);
        }

        // Taking and validating Aadhaar number input
        valid = false;
        while (!valid) {
            System.out.print("--> Enter Aadhaar Number (12 digits) :- ");
            aadhar = sc.next();
            valid = checkAadharNo(aadhar);
        }

        // Taking and validating address input
        valid = false;
        while (!valid) {
            System.out.print("--> Enter Address :- ");
            address = sc.next().trim();
            valid = checkAddress(address);
        }

        // Taking and validating password input
        valid = false;
        System.out.println("\n---> Password must be at least 8 characters long, contain one uppercase letter, one lowercase letter, one number, and one special character.");
        while (!valid) {
            System.out.print("\n--> Create Password :- ");
            password = sc.next();
            valid = checkPassword(password, sc);
        }

        return new RegisteredEmployee(fname, lname, email, dateOfBirth, mobile, aadhar, address, password);
    }

    // Method to display employee profile details
    void viewProfile() {
        EmployeeManagement.employee[loginIndex].displayProfile();
    }

    // Method to view salary details of an employee
    void viewSalaryDetails() {
        System.out.println("# Your salary details have been successfully displayed, " + EmployeeManagement.employee[loginIndex].fullName + ".");
        EmployeeManagement.employee[loginIndex].salaryDetails();
    }

    // Method to validate employee login credentials
    boolean validateCredentials(Employee[] employee, int employeeCount, String id, String password) {
        for (int i = 0; i < employeeCount; i++) {
            if (employee[i].employeeId.equals(id) && employee[i].password.equals(password)) {
                if (!employee[i].status.equals("Approved")) {
                    System.out.println("Your account is not approved yet.");
                    return false;
                }
                System.out.println("# Welcome back, " + employee[i].fullName + "!");
                loginIndex = i;
                return true;
            }
        }
        return false;
    }

    // Method to reset employee password
    void passwordReset(Scanner sc) {
        boolean valid, found = false;
        String resetPassword = null;
        System.out.println("# Hello, " + EmployeeManagement.employee[loginIndex].fullName + " it seems you need to reset your password.");
        valid = false;
        while (!valid) {
            System.out.print("--> Enter New Password :- ");
            resetPassword = sc.next();
            valid = checkPassword(resetPassword, sc);
        }
        EmployeeManagement.employee[loginIndex].password = resetPassword;

    }

    // Method to update employee first name
    void updateFirstName(Scanner sc) {
        boolean valid = false;
        String fname = null;
        while (!valid) {
            System.out.print("--> Enter first name :- ");
            fname = sc.next();
            valid = checkName(fname.toUpperCase());
        }
        EmployeeManagement.employee[loginIndex].firstName = fname.substring(0, 1).toUpperCase() + fname.substring(1).toLowerCase();
        System.out.println("Your first name updated successfully");
    }

    // Method to update employee last name
    void updateLastName(Scanner sc) {
        boolean valid = false;
        String lname = null;
        while (!valid) {
            System.out.print("--> Enter last name :- ");
            lname = sc.next();
            valid = checkName(lname.toUpperCase());
        }
        EmployeeManagement.employee[loginIndex].lastName = lname.substring(0, 1).toUpperCase() + lname.substring(1).toLowerCase();
        System.out.println("Your last name updated successfully");
    }

    // Method to update employee mobile number
    void updateMobileNo(Scanner sc) {
        boolean valid = false, found = false;
        String mobileNo = null;
        while (!valid) {
            System.out.print("--> Enter mobile No :- ");
            mobileNo = sc.next();
            valid = checkMobileNo(mobileNo);
        }
        EmployeeManagement.employee[loginIndex].mobileNo = mobileNo;
        System.out.println("Your mobile no. updated successfully");
    }

    // Method to update employee email ID
    void updateEmailId(Scanner sc) {
        boolean valid = false;
        String email = null;
        while (!valid) {
            System.out.print("--> Enter email-id :- ");
            email = sc.next();
            valid = checkEmail(email);
        }
        EmployeeManagement.employee[loginIndex].emailId = email;
        System.out.println("Your email updated successfully");
    }
}

class EmployeeManagement { // start class EmployeeManagement
    static {
        System.out.println("*--------------------------------------------------------------*");
        System.out.println("|            Welcome To Employee Management System             |");
        System.out.println("*--------------------------------------------------------------*");
    }

    static Scanner sc = new Scanner(System.in);
    static Employee employee[];

    public static void main(String[] args) { // start main

        employee = new Employee[30];
        RegisteredEmployee registeredEmployee[] = new RegisteredEmployee[100];

        Employee employees = new Employee();

        EmployeePortal portal = new EmployeePortal();

        Manager manager = new Manager();

        System.out.println("-----------------------------------------------------------------");
        registeredEmployee[EmployeePortal.registeredEmployeeCount++] = new RegisteredEmployee("Ansh", "Patoliya", "ap1331@gmail.com", "14/08/2006", "8160369100", "467620849439", "surat", "Admin@123");
        registeredEmployee[EmployeePortal.registeredEmployeeCount - 1].displayProfile();
        System.out.println("-----------------------------------------------------------------");
        registeredEmployee[EmployeePortal.registeredEmployeeCount++] = new RegisteredEmployee("Priyansh", "Vekariya", "vp1331@gmail.com", "26/08/2006", "6354921104", "902320978264", "ahmedabad", "Admin@123");
        registeredEmployee[EmployeePortal.registeredEmployeeCount - 1].displayProfile();
        System.out.println("----------------------------------------------------------------");
        registeredEmployee[EmployeePortal.registeredEmployeeCount++] = new RegisteredEmployee("Dwarkesh", "Kathiriya", "dk1331@gmail.com", "30/11/2006", "9265060137", "672396271635", "ahmedabad", "Admin@123");
        registeredEmployee[EmployeePortal.registeredEmployeeCount - 1].displayProfile();
        System.out.println("--------------------------------------------------------------");

        int actionChoice = 0, updateChoice;
        while (actionChoice != 4) {// start while starting window
            System.out.println();
            System.out.println("1.Employee Registration");
            System.out.println("2.Employee Login");
            System.out.println("3.Manager Login");
            System.out.println("4.Close application");
            System.out.print("--> Choose Any Action :- ");
            actionChoice = sc.nextInt();
            switch (actionChoice) { // start switch starting window
                case 1://case 1 for Employee registration
                    System.out.println("\n# Hello! We are delighted to have you here. Let's get started with your registration.");
                    registeredEmployee[EmployeePortal.registeredEmployeeCount++] = portal.employeeRegistration(sc);
                    System.out.println("Your registration is pending manager approval.");
                    break;
                case 2://case 2 for Employee login
                    System.out.println("\n# Hello, and welcome back! Please enter your login credentials to continue.");
                    System.out.print("--> Enter Employee Id :- ");
                    String id = sc.next();
                    System.out.print("--> Enter Password :- ");
                    String password = sc.next();

                    if (!portal.validateCredentials(employee, EmployeePortal.employeeCount, id, password)) {//when not successful login
                        System.out.println("Invalid ID or password. Please try again.");
                    } else {//when successful employee login
                        int employeeLoginChoice = 0;
                        while (employeeLoginChoice != 7) {//start while for employee login case
                            System.out.println();
                            System.out.println("1.View Your Profile");
                            System.out.println("2.Check Salary Details");
                            System.out.println("3.Reset Your Password");
                            System.out.println("4.Submit a Leave Application");
                            System.out.println("5.Cancel a Leave Application");
                            System.out.println("6.View Leave Status");
                            System.out.println("7.Log Out");
                            System.out.print("--> Please Enter Your Choice (1-6) :- ");
                            employeeLoginChoice = sc.nextInt();
                            switch (employeeLoginChoice) {//start switch for employee login case
                                case 1://case 1 for view profile
                                    portal.viewProfile();
                                    System.out.println();
                                    System.out.print("--> Can you update your profile ? (yes/no) :- ");
                                    String askUpdate = sc.next();
                                    if (askUpdate.equalsIgnoreCase("yes")) {
                                        updateChoice = 0;
                                        while (updateChoice != 5) {//start while for update employee profile
                                            System.out.println();
                                            System.out.println("1. Update first name");
                                            System.out.println("2. Update last name");
                                            System.out.println("3. Update email-id");
                                            System.out.println("4. Update mobile no");
                                            System.out.println("5. Back");
                                            System.out.print("--> Select option for update :- ");
                                            updateChoice = sc.nextInt();
                                            switch (updateChoice) {//start switch for update employee profile
                                                case 1://update first name
                                                    portal.updateFirstName(sc);
                                                    break;
                                                case 2://update last name
                                                    portal.updateLastName(sc);
                                                    break;
                                                case 3://update email
                                                    portal.updateEmailId(sc);
                                                    break;
                                                case 4://update mobile no
                                                    portal.updateMobileNo(sc);
                                                    break;
                                                default:
                                                    System.out.println("Enter valid choice!");
                                                    break;
                                            }//end switch for update employee profile
                                        }//end while for update employee profile
                                    } else if (askUpdate.equalsIgnoreCase("no")) {
                                    } else {
                                        System.out.println("Enter valid choice!");
                                    }
                                    break;
                                case 2:// case 2 for view Salary details
                                    portal.viewSalaryDetails();
                                    break;
                                case 3://case 3 for reset password
                                    portal.passwordReset(sc);
                                    System.out.println("Password Reset Successfully");
                                    break;
                                case 4://case 4 for submit a leave application
                                    employees.applyForLeave(sc);
                                    break;
                                case 5://case 5 for cancel a leave application
                                    employees.cancelLeaveRequest(sc);
                                    break;
                                case 6://case 6 for check leave status
                                    employees.checkLeaveStatus();
                                    break;
                                case 7://case 7 for log out
                                    System.out.println("# You've successfully logged out. Take care and see you next time!");
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please try again.");
                                    break;
                            }//end switch for employee login case
                        }//end while for employee login case
                    }//(end)when successful employee login
                    break;
                case 3://case 3 for Manager login
                    System.out.println("Welcome back, Manager ! Your login was successful. Let's lead the team to success today!");
                    int managerLoginChoice = 0;

                    System.out.print("--> Enter your id :- ");
                    String managerId = sc.next();
                    System.out.print("--> Enter your password :- ");
                    String managerPassword = sc.next();

                    if (managerId.equals("12345678") && managerPassword.equals("Admin@123")) {//start if (check manger credentials)
                        while (managerLoginChoice != 8) {//start while for manager login case
                            System.out.println();
                            System.out.println("1.Approve employee registration");
                            System.out.println("2.View a list of all employee");
                            System.out.println("3.Update Employee Salary");
                            System.out.println("4.Change Salary Descriptions");
                            System.out.println("5.Search Employee");
                            System.out.println("6.Approve Leave Requests");
                            System.out.println("7.Rejected employee list");
                            System.out.println("8.Log Out");
                            System.out.print("--> Please Enter Your Choice (1-6) :- ");
                            managerLoginChoice = sc.nextInt();
                            switch (managerLoginChoice) {//start switch for manager login case
                                case 1://case 1 for approve employee registration
                                    manager.approveEmployee(registeredEmployee, EmployeePortal.registeredEmployeeCount, sc);
                                    break;
                                case 2://case 2 for view all employee details
                                    System.out.println("You are now viewing all employee details.");
                                    manager.viewAllEmployeeList();
                                    break;
                                case 3://case 3 for update employee salary
                                    System.out.print("--> Enter Employee ID :- ");
                                    id = sc.next();
                                    manager.updateEmployeealary(id, sc);
                                    break;
                                case 4://case 4 for change salary descriptions
                                    System.out.print("--> Enter new DA  :- ");
                                    double da = sc.nextDouble();
                                    System.out.print("--> Enter new HRA :- ");
                                    double hra = sc.nextDouble();
                                    System.out.print("--> Enter new PF  :- ");
                                    double pf = sc.nextDouble();
                                    employees.setDa(da);
                                    employees.setHra(hra);
                                    employees.setPf(pf);
                                    System.out.println("---> The manager has updated the salary description to better reflect the employee’s compensation structure.");
                                    break;
                                case 5://case 5 for search employee
                                    System.out.println("#The manager is searching for an employee's records to review their performance and salary details.");
                                    int searchChoice = 0;
                                    while (searchChoice != 5) {//start while for search employee
                                        System.out.println();
                                        System.out.println("1.Search By Employee ID");
                                        System.out.println("2.Search By Employee Name");
                                        System.out.println("3.Search By Employee Email ID");
                                        System.out.println("4.Search By Address City");
                                        System.out.println("5.Back to main menu");
                                        System.out.print("--> Please Enter Your Choice (1-5) :- ");
                                        searchChoice = sc.nextInt();
                                        System.out.println();
                                        switch (searchChoice) {//start switch for search employee
                                            case 1://case 1 for search by employee id
                                                System.out.println("You selected: Search by Employee ID");
                                                System.out.print("--> Enter Employee ID:- ");
                                                id = sc.next();
                                                manager.searchEmployee("id", id);
                                                break;
                                            case 2://case 2 for search by employee name
                                                System.out.println("You selected: Search by Employee Name");
                                                System.out.print("--> Enter Employee First Name:- ");
                                                sc.nextLine();
                                                String fname = sc.nextLine();
                                                manager.searchEmployee("name", fname);
                                                break;
                                            case 3://case 3 for search by employee email
                                                System.out.println("You selected: Search by Employee Email ID");
                                                System.out.print("--> Enter Employee Email:- ");
                                                sc.nextLine();
                                                String email = sc.nextLine();
                                                manager.searchEmployee("email", email);
                                                break;
                                            case 4://case 4 for search by employee city
                                                System.out.println("You selected: Search by Address (City)");
                                                System.out.print("--> Enter City:- ");
                                                sc.nextLine();
                                                String city = sc.nextLine();
                                                manager.searchEmployee("city", city);
                                                break;
                                            case 5://case 5 for back to main menu
                                                break;
                                            default:
                                                System.out.println("---> Invalid choice. Please try again.");
                                                break;
                                        }//end switch for search employee
                                    }//end while for search employee
                                    break;
                                case 6://case 6 for approve leave
                                    manager.approveLeave(sc);
                                    break;
                                case 7://case 7 for rejected employee list
                                    manager.rejectedEmployee(registeredEmployee);
                                    break;
                                case 8://case 8 for manager log out
                                    System.out.println("---> The manager has successfully logged out of the system.");
                                    break;
                                default:
                                    System.out.println("---> Invalid choice. Please try again.");
                                    break;
                            }//end switch for manager login case
                        }//end while for manager login case
                    }//end if (check manger credentials)
                    break;
                case 4://case 4 for close application
                    System.out.println("-> You have successfully closed the application. Have a great day!");
                    break;
                default:
                    System.out.println("---> Invalid choice. Please try again.");
                    break;
            }// end switch starting window
        }// end while starting window
    } //end main
}// end class EmployeeManagement