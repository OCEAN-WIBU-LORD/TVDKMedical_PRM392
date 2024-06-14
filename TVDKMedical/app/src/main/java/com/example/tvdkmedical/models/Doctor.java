package com.example.tvdkmedical.models;

import java.util.Date;

public class Doctor {
    String doctorId;
    String name;
    Date dateOfBirth;

    String inforDoctor;
    Integer doctorVoted;
    public String getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getInforDoctor() {
        return inforDoctor;
    }

    public Integer getDoctorVoted() {
        return doctorVoted;
    }

    public Doctor(String doctorId, String name, Date dateOfBirth, String inforDoctor, Integer doctorVoted) {
        this.doctorId = doctorId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.inforDoctor = inforDoctor;
        this.doctorVoted = doctorVoted;
    }


}
