package com.example.tvdkmedical.views.appointment;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tvdkmedical.R;
import com.example.tvdkmedical.models.Appointment;
import com.example.tvdkmedical.repositories.AppointmentResp;
import com.example.tvdkmedical.repositories.callbacks.Callback;

import java.util.ArrayList;
import java.util.List;

public class AppointmentDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String appointmentId = getIntent().getStringExtra("appointmentId");

        AppointmentResp appointmentResp = new AppointmentResp();


        appointmentResp.getAppointmentById(appointmentId, new Callback<Appointment>() {
            @Override
            public void onCallback(List<Appointment> appointmentList) {
                // Get appointment data
                List<Appointment> appointments = new ArrayList<>(appointmentList);
                Appointment appointment = appointments.get(0);

                // Get disease data
//                Disease disease = appointment.getDisease();


            }
        });

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}