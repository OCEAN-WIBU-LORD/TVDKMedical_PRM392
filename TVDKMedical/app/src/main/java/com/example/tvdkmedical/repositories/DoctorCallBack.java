package com.example.tvdkmedical.repositories;

import com.example.tvdkmedical.models.Doctor;

import java.util.List;

public interface DoctorCallBack {
    void onCallback(List<Doctor> doctors);
}
