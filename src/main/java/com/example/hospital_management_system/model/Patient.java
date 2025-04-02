package com.example.hospital_management_system.model;

import java.sql.Date;

public class Patient {
    private int id;
    private String name;
    private String mobileNo; // Changed from age to mobile_no
    private String medicalIssue; // Changed from gender to medical_issue
    private Date registrationDate; // New attribute

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMedicalIssue() {
        return medicalIssue;
    }

    public void setMedicalIssue(String medicalIssue) {
        this.medicalIssue = medicalIssue;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}