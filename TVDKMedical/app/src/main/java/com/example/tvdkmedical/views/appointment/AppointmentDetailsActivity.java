package com.example.tvdkmedical.views.appointment;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.R;
import com.example.tvdkmedical.models.Appointment;
import com.example.tvdkmedical.models.Disease;
import com.example.tvdkmedical.repositories.AppointmentResp;
import com.example.tvdkmedical.repositories.DiseaseResp;
import com.example.tvdkmedical.repositories.callbacks.Callback;

import java.util.ArrayList;
import java.util.List;

public class AppointmentDetailsActivity extends AppCompatActivity {
    private RecyclerView rvDiseases;
    private DiseaseAdapter diseaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment_details);

        initViews();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        AppointmentResp appointmentResp = new AppointmentResp();
        DiseaseResp diseaseResp = new DiseaseResp();

        String appointmentId = getIntent().getStringExtra("appointmentId");

        appointmentResp.getAppointmentById(appointmentId, new Callback<Appointment>() {
            @Override
            public void onCallback(List<Appointment> appointmentList) {
                // Get appointment data
                List<Appointment> appointments = new ArrayList<>(appointmentList);
                Appointment appointment = appointments.get(0);

                // Get disease data
                String diseaseId = appointment.getDiseaseId();

                // Fetch diseases by diseaseId
                diseaseResp.getDiseasesByIds(new String[]{diseaseId}, new Callback<Disease>() {
                    @Override
                    public void onCallback(List<Disease> diseaseList) {
                        // Initialize the RecyclerView inside the disease_list layout
                        rvDiseases = findViewById(R.id.diseaseList);

                        diseaseAdapter = new DiseaseAdapter(diseaseList);
                        rvDiseases.setAdapter(diseaseAdapter);
                        rvDiseases.setLayoutManager(new LinearLayoutManager(AppointmentDetailsActivity.this));
                    }
                });
            }
        });
    }
}