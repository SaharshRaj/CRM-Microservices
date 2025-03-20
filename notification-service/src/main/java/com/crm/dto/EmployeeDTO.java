package com.crm.dto;


/**
 * Data Transfer Object (DTO) representing an employee.
 * This DTO encapsulates employee information such as ID, name, email, and phone number.
 */

public class EmployeeDTO {
    /**
     * The unique identifier for the employee. Defaults to 101L.
     * The name of the employee. Defaults to "Manager".
     * The email address of the employee. Defaults to "sohithkalavakuri71@gmail.com".
     * The phone number of the employee. Defaults to "+917330783299".
     */
    private Long employeeID = 101L;
    private String name = "Manager";
    private String email = "sohithkalavakuri71@gmail.com";
    private String phoneNumber = "+917330783299";
    /**
     * Retrieves the employee ID.
     * @return The employee ID.
     */
    public Long getEmployeeID() {
        return employeeID;
    }

    /**
     * Sets the employee ID.
     * @param employeeID The employee ID to set.
     */
    public void setEmployeeID(Long employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * Retrieves the employee name.
     * @return The employee name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the employee name.
     * @param name The employee name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the employee email address.
     * @return The employee email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the employee email address.
     * @param email The employee email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the employee phone number.
     * @return The employee phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the employee phone number.
     * @param phoneNumber The employee phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
   
}