package com.example.tvdkmedical;

import java.text.DateFormat;

public class Appointment {
    Integer userId;
    Integer doctorId;
    DateFormat dateBooking;
    String note;

    Integer diseaseId;

    public Integer getUserId() {
        return userId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public DateFormat getDateBooking() {
        return dateBooking;
    }

    public String getNote() {
        return note;
    }

    public Integer getDiseaseId() {
        return diseaseId;
    }

    public Appointment(Integer userId, Integer doctorId, DateFormat dateBooking, String note, Integer diseaseId) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.dateBooking = dateBooking;
        this.note = note;
        this.diseaseId = diseaseId;
    }
}
