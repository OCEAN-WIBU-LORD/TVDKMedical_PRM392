package com.example.tvdkmedical.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tvdkmedical.models.Doctor;
import com.example.tvdkmedical.repositories.callbacks.Callback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorResp {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public void getDoctors(Callback<Doctor> callback) {
        Query query = databaseReference.child("doctors").orderByChild("name");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Doctor> doctors = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Doctor doctor = new Doctor();
                    String doctorId = childSnapshot.getKey();
                    String bio = childSnapshot.child("bio").getValue(String.class);
                    String diseaseId = childSnapshot.child("diseaseId").getValue(String.class);
                    String email = childSnapshot.child("email").getValue(String.class);
                    String name = childSnapshot.child("name").getValue(String.class);
                    String office = childSnapshot.child("office").getValue(String.class);
                    Long phoneNumber = childSnapshot.child("phoneNumber").getValue(Long.class);
                    String imageUrl = childSnapshot.child("imageurl").getValue(String.class);
                    doctor.setDoctorId(doctorId);
                    doctor.setBio(bio);
                    doctor.setName(name);
                    doctors.add(doctor);
                }

                Log.d("DoctorResp", "Number of doctors fetched: " + doctors.size());
                callback.onCallback(doctors);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DoctorResp", "Error: " + error.getMessage());
            }
        });
    }
}
