package com.example.tvdkmedical.views.appointment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.R;
import com.example.tvdkmedical.models.Disease;
import com.example.tvdkmedical.views.appointment.DiseaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class DiseaseListActivity extends AppCompatActivity {

    private RecyclerView rvDiseases;
    private DiseaseAdapter diseaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disease_list);

        rvDiseases = findViewById(R.id.diseaseList);

        List<Disease> diseaseList = new ArrayList<>();
        diseaseList.add(new Disease("1", "Disease 1", "Description 1"));
        diseaseList.add(new Disease("2","Disease 2", "Description 2"));

        diseaseAdapter = new DiseaseAdapter(diseaseList);
        rvDiseases.setAdapter(diseaseAdapter);
        rvDiseases.setLayoutManager(new LinearLayoutManager(this));
    }
}