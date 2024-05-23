package com.example.tvdkmedical;

import java.util.Date;

public class Doctor {
    Integer doctorId;
    String name;
    Date dateOfBirth;

    String inforDoctor;
    Integer doctorVoted;
    public Integer getDoctorId() {
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

    public Doctor(Integer doctorId, String name, Date dateOfBirth, String inforDoctor, Integer doctorVoted) {
        this.doctorId = doctorId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.inforDoctor = inforDoctor;
        this.doctorVoted = doctorVoted;
    }


}
