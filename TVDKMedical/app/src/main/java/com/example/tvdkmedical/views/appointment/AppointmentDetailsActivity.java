package com.example.tvdkmedical.views.appointment;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tvdkmedical.R;
import com.example.tvdkmedical.models.Appointment;
import com.example.tvdkmedical.models.Users;
import com.example.tvdkmedical.repositories.AppointmentCallback;
import com.example.tvdkmedical.repositories.AppointmentResp;

import java.util.ArrayList;
import java.util.List;

public class AppointmentDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppointmentResp appointmentResp = new AppointmentResp();

        appointmentResp.getAppointments(new AppointmentCallback() {
            @Override
            public void onCallback(List<Appointment> appointmentList) {
                List<Appointment> appointments = new ArrayList<>(appointmentList);
                System.out.println(appointments.size());

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