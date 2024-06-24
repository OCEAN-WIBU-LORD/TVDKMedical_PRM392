package com.example.tvdkmedical.repositories;

import androidx.annotation.NonNull;

import com.example.tvdkmedical.models.Appointment;
import com.example.tvdkmedical.repositories.callbacks.Callback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppointmentResp {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    // Get all appointments by userId
    public void getAppointments(String userId, Callback<Appointment> callback) {
        Query query = databaseReference.child("appointments").orderByChild("userId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Appointment> appointments = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentId(Objects.requireNonNull(childSnapshot.getKey()));
                    appointment.setDiseaseId(Objects.requireNonNull(childSnapshot.child("diseaseId").getValue()).toString());
                    appointment.setDoctorId(Objects.requireNonNull(childSnapshot.child("doctorId").getValue()).toString());
                    appointment.setNote(Objects.requireNonNull(childSnapshot.child("note").getValue()).toString());
                    appointment.setStatus(Objects.requireNonNull(childSnapshot.child("status").getValue()).toString());
                    appointment.setUserId(Objects.requireNonNull(childSnapshot.child("userId").getValue()).toString());
                    appointment.setRecordId(Objects.requireNonNull(childSnapshot.child("recordId").getValue()).toString());

                    int endTime = Objects.requireNonNull(childSnapshot.child("endTime").getValue(Integer.class));
                    com.google.firebase.Timestamp endTs = new com.google.firebase.Timestamp(endTime, 0);
                    appointment.setEndTime(endTs);

                    int startTime = Objects.requireNonNull(childSnapshot.child("startTime").getValue(Integer.class));
                    com.google.firebase.Timestamp startTs = new com.google.firebase.Timestamp(startTime, 0);
                    appointment.setStartTime(startTs);

                    appointments.add(appointment);
                }

                callback.onCallback(appointments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: " + error.getMessage());
            }
        });
    }

    // Get appointments by appointmentId
    public void getAppointmentById(String appointmentId, Callback<Appointment> callback) {
        Query query = databaseReference.child("appointments").child(appointmentId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Appointment> appointments = new ArrayList<>();
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(Objects.requireNonNull(snapshot.getKey()));
                appointment.setDiseaseId(Objects.requireNonNull(snapshot.child("diseaseId").getValue()).toString());
                appointment.setDoctorId(Objects.requireNonNull(snapshot.child("doctorId").getValue()).toString());
                appointment.setNote(Objects.requireNonNull(snapshot.child("note").getValue()).toString());
                appointment.setStatus(Objects.requireNonNull(snapshot.child("status").getValue()).toString());
                appointment.setUserId(Objects.requireNonNull(snapshot.child("userId").getValue()).toString());
                appointment.setRecordId(Objects.requireNonNull(snapshot.child("recordId").getValue()).toString());

                int endTime = Objects.requireNonNull(snapshot.child("endTime").getValue(Integer.class));
                com.google.firebase.Timestamp endTs = new com.google.firebase.Timestamp(endTime, 0);
                appointment.setEndTime(endTs);

                int startTime = Objects.requireNonNull(snapshot.child("startTime").getValue(Integer.class));
                com.google.firebase.Timestamp startTs = new com.google.firebase.Timestamp(startTime, 0);
                appointment.setStartTime(startTs);

                appointments.add(appointment);

                callback.onCallback(appointments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: " + error.getMessage());
            }
        });
    }

    // Create an appointment
    public void createAppointment(Appointment appointment, Callback<Appointment> callback) {
        DatabaseReference ref = databaseReference.child("appointments").push();
        ref.setValue(appointment).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Appointment> appointments = new ArrayList<>();
                appointments.add(appointment);
                callback.onCallback(appointments);
            } else {
                System.out.println("Error: " + task.getException());
            }
        });
    }

    // Update an appointment
    public void updateAppointment(Appointment appointment, Callback<Appointment> callback) {
        DatabaseReference ref = databaseReference.child("appointments").child(appointment.getAppointmentId());
        ref.setValue(appointment).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Appointment> appointments = new ArrayList<>();
                appointments.add(appointment);
                callback.onCallback(appointments);
            } else {
                System.out.println("Error: " + task.getException());
            }
        });
    }

    // Delete an appointment
    public void deleteAppointment(String appointmentId, Callback<Appointment> callback) {
        DatabaseReference ref = databaseReference.child("appointments").child(appointmentId);
        ref.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Appointment> appointments = new ArrayList<>();
                callback.onCallback(appointments);
            } else {
                System.out.println("Error: " + task.getException());
            }
        });
    }
}
