package com.example.tvdkmedical.models;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Map;

public class UpdatedUser {
    private String userId;
    private String name;
    private String email;
    private Timestamp dob;
    private String address;
    private String phone;
    private Map<String, Object> idCard;
    private Map<String, Object> healthCard;
    private String role;

    public UpdatedUser() {
    }

    public UpdatedUser(String userId, String name, String email, Timestamp dob, String address, String phone, Map<String, Object> idCard, Map<String, Object> healthCard, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
        this.idCard = idCard;
        this.healthCard = healthCard;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getDob() {
        return dob;
    }

    public void setDob(Timestamp dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Map<String, Object> getIdCard() {
        return idCard;
    }

    public void setIdCard(Map<String, Object> idCard) {
        this.idCard = idCard;
    }

    public Map<String, Object> getHealthCard() {
        return healthCard;
    }

    public void setHealthCard(Map<String, Object> healthCard) {
        this.healthCard = healthCard;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isDoctor() {
        return role.equals("doctor");
    }
}
