package com.example.hospital_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.hospital_management_system.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PatientController {

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/hospital";
        String username = "root";
        String password = "anand9791";
        return DriverManager.getConnection(url, username, password);
    }

    @GetMapping("/")
    public String index(Model model) throws SQLException {
        List<Patient> patients = getAllPatients();
        model.addAttribute("patients", patients);
        return "index";
    }

    private List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getInt("id"));
                patient.setName(rs.getString("name"));
                patient.setMobileNo(rs.getString("mobile_no")); // Updated
                patient.setMedicalIssue(rs.getString("medical_issue")); // Updated
                patient.setRegistrationDate(rs.getDate("registration_date")); // Updated
                patients.add(patient);
            }
        }
        return patients;
    }

    @GetMapping("/add")
    public String addForm() {
        return "add-patient";
    }

    @PostMapping("/add")
    public String addPatient(@RequestParam String name, @RequestParam String mobileNo,
            @RequestParam String medicalIssue, @RequestParam String registrationDate) throws SQLException {
        Patient patient = new Patient();
        patient.setName(name);
        patient.setMobileNo(mobileNo); // Updated
        patient.setMedicalIssue(medicalIssue); // Updated
        patient.setRegistrationDate(Date.valueOf(registrationDate)); // Updated (convert String to Date)
        insertPatient(patient);
        return "redirect:/";
    }

    private void insertPatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients (name, mobile_no, medical_issue, registration_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient.getName());
            pstmt.setString(2, patient.getMobileNo()); // Updated
            pstmt.setString(3, patient.getMedicalIssue()); // Updated
            pstmt.setDate(4, patient.getRegistrationDate()); // Updated
            pstmt.executeUpdate();
        }
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable int id) throws SQLException {
        deletePatientById(id);
        return "redirect:/";
    }

    private void deletePatientById(int id) throws SQLException {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable int id, Model model) throws SQLException {
        Patient patient = getPatientById(id);
        if (patient != null) {
            model.addAttribute("patient", patient);
            return "update-patient";
        }
        return "redirect:/";
    }

    private Patient getPatientById(int id) throws SQLException {
        Patient patient = null;
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    patient = new Patient();
                    patient.setId(rs.getInt("id"));
                    patient.setName(rs.getString("name"));
                    patient.setMobileNo(rs.getString("mobile_no")); // Updated
                    patient.setMedicalIssue(rs.getString("medical_issue")); // Updated
                    patient.setRegistrationDate(rs.getDate("registration_date")); // Updated
                }
            }
        }
        return patient;
    }

    @PostMapping("/update")
    public String updatePatient(@RequestParam int id, @RequestParam String name,
            @RequestParam String mobileNo, @RequestParam String medicalIssue,
            @RequestParam String registrationDate) throws SQLException {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setName(name);
        patient.setMobileNo(mobileNo); // Updated
        patient.setMedicalIssue(medicalIssue); // Updated
        patient.setRegistrationDate(Date.valueOf(registrationDate)); // Updated
        updatePatient(patient);
        return "redirect:/";
    }

    private void updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE patients SET name = ?, mobile_no = ?, medical_issue = ?, registration_date = ? WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient.getName());
            pstmt.setString(2, patient.getMobileNo()); // Updated
            pstmt.setString(3, patient.getMedicalIssue()); // Updated
            pstmt.setDate(4, patient.getRegistrationDate()); // Updated
            pstmt.setInt(5, patient.getId());
            pstmt.executeUpdate();
        }
    }

    @GetMapping("/search")
    public String searchPatient(@RequestParam(required = false) Integer id, Model model) throws SQLException {
        if (id == null) {
            return "redirect:/";
        }
        Patient patient = getPatientById(id);
        if (patient != null) {
            model.addAttribute("patient", patient);
        } else {
            model.addAttribute("message", "Patient not found");
        }
        return "search-result";
    }
}