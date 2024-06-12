package com.example.tvdkmedical.repositories;

import com.example.tvdkmedical.models.Appointment;

import java.util.List;

public interface AppointmentCallback {
    void onCallback(List<Appointment> appointments);

}
