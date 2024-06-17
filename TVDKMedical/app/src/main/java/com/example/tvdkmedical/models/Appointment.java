package com.example.tvdkmedical.models;

import com.google.firebase.Timestamp;


public class Appointment {
    private String appointmentId;
    private String diseaseId;
    private String doctorId;
    private Timestamp endTime;
    private String note;
    private Timestamp startTime;
    private String status;
    private String userId;

    public Appointment() {
    }

    public Appointment(String appointmentId, String diseaseId, String doctorId, Timestamp endTime, String note, Timestamp startTime, String status, String userId) {
        this.appointmentId = appointmentId;
        this.diseaseId = diseaseId;
        this.doctorId = doctorId;
        this.endTime = endTime;
        this.note = note;
        this.startTime = startTime;
        this.status = status;
        this.userId = userId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public String getNote() {
        return note;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public String getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}