package com.niall.electronicsstore.entities;

import android.widget.Toast;

public class AdminDetails {

    private String employeeID;
    private String jobTitle;


    public AdminDetails(){

    }

    private AdminDetails(AdminBuilder adminBuilder){
        this.employeeID = adminBuilder.employeeID;
        this.jobTitle = adminBuilder.jobTitle;
    }


    public static class AdminBuilder{

        private String employeeID;
        private String jobTitle;

        public AdminBuilder(String employeeID, String jobTitle){
            this.employeeID = employeeID;
            this.jobTitle = jobTitle;
        }

        private boolean validate(){
            return employeeID.equals("123456") || employeeID.equals("016166");
        }

        public AdminDetails build(){
            validate();
            return new AdminDetails(this);
        }


    }


    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return "AdminDetails{" +
                "employeeID='" + employeeID + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                '}';
    }
}
