package com.example.tvdkmedical.repositories;

import androidx.annotation.NonNull;

import com.example.tvdkmedical.models.Appointment;
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

    public void getAppointments(AppointmentCallback callback) {
        Query query = databaseReference.child("appointments").orderByChild("date");
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
}
