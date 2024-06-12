package com.example.tvdkmedical.models;

import java.text.DateFormat;
import java.util.Date;

public class Appointment {
    Users user;
    Doctor doctor;
    Date dateBooking;
    String note;
    Integer diseaseId;
    Date startTime;
    Date endTime;

    public Users getUser() {
        return user;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Date getDateBooking() {
        return dateBooking;
    }

    public String getNote() {
        return note;
    }

    public Integer getDiseaseId() {
        return diseaseId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Appointment(Users userId, Doctor doctorId, Date dateBooking, String note, Integer diseaseId, Date startTime, Date endTime) {
        this.user = userId;
        this.doctor = doctorId;
        this.dateBooking = dateBooking;
        this.note = note;
        this.diseaseId = diseaseId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
