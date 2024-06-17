package com.example.tvdkmedical.models;

import java.util.Date;

public class Doctor {
    private String doctorId;
    private String bio;
    private String diseaseId;
    private String email;
    private String imageUrl;
    private String name;
    private String office;
    private String phone;

    public Doctor() {
    }

    public Doctor(String doctorId, String bio, String diseaseId, String email, String imageUrl, String name, String office, String phone) {
        this.doctorId = doctorId;
        this.bio = bio;
        this.diseaseId = diseaseId;
        this.email = email;
        this.imageUrl = imageUrl;
        this.name = name;
        this.office = office;
        this.phone = phone;
    }

    // Getters and Setters
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

