package com.example.tvdkmedical;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.models.Appointment;
import com.example.tvdkmedical.models.Doctor;
import com.example.tvdkmedical.models.Users;
import com.example.tvdkmedical.repositories.AppointmentCallback;
import com.example.tvdkmedical.repositories.AppointmentResp;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    private Spinner spinnerMonths;
    private RecyclerView rcv;
    private RecyclerView rcvAppointmentToday;
    private RecyclerView rcvAllAppointment;
    private List<Day> days;

    ArrayList<Appointment> appointments;
    private List<Doctor> doctors;
    private DayAdapter adapter;
    private AppointmentTodayAdapter atAdapter;
    private AllAppointmentAdapter allAppointmentAdapter;
    private TextView currentDate;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule);
        spinnerMonths = findViewById(R.id.spinnerMonth);
        rcv = findViewById(R.id.recyclerView);
        rcvAppointmentToday = findViewById(R.id.rcvAppointmentToday);
        rcvAllAppointment = findViewById(R.id.rcvAllAppointment);
        currentDate = findViewById(R.id.currentDate);
        days = new ArrayList<>();
        appointments = new ArrayList<>();
        setCurrentDate();

        initSpinner();

        initRecyclerView();
        initRecyclerViewAppointmentToday();
        initRecyclerViewAllAppointment();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initSpinner() {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, months);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMonths.setAdapter(adapter);

        spinnerMonths.setSelection(0);

        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = parent.getItemAtPosition(position).toString();
                int monthIndex = position;
                updateDaysOfMonth(monthIndex);
                Toast.makeText(ScheduleActivity.this, "Selected: " + selectedMonth, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecyclerView() {
        adapter = new DayAdapter(this, days);
        rcv.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        rcv.setLayoutManager(layoutManager);
    }

    private void initRecyclerViewAppointmentToday() {
        loadFakeData();
    }

    private void initRecyclerViewAllAppointment() {
        loadFakeData();
    }

    private void setCurrentDate() {
        Date currentDateTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String formattedDate = dateFormat.format(currentDateTime);
        currentDate.setText(formattedDate);
    }

    private void updateDaysOfMonth(int selectedMonth) {
        days.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, selectedMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int numDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < numDaysInMonth; i++) {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
            String shortDayOfWeek = dayOfWeek.substring(0, 2);
            days.add(new Day(String.valueOf(day), shortDayOfWeek));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(ScheduleActivity.this, "Adapter chưa được khởi tạo", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFakeData() {
        doctors = new ArrayList<>();

        doctors.add(new Doctor("doctor_id_1", "Dr. John Doe", new Date(1980, 1, 1), "Cardiologist", 5));
        doctors.add(new Doctor("doctor_id_2", "Dr. Jane Smith", new Date(1985, 5, 10), "Dermatologist", 4));
        doctors.add(new Doctor("doctor_id_3", "Dr. Richard Roe", new Date(1975, 3, 15), "Neurologist", 3));
        doctors.add(new Doctor("doctor_id_4", "Dr. Emily Davis", new Date(1990, 7, 25), "Pediatrician", 5));
        doctors.add(new Doctor("doctor_id_5", "Dr. Michael Brown", new Date(1983, 11, 30), "Surgeon", 4));

        AppointmentResp appointmentResp = new AppointmentResp();

        appointmentResp.getAppointments(new AppointmentCallback() {
            @Override
            public void onCallback(List<Appointment> appointmentList) {
                appointments = new ArrayList<>(appointmentList);
                System.out.println(appointments.size());

                atAdapter = new AppointmentTodayAdapter(ScheduleActivity.this, appointments, doctors);
                rcvAppointmentToday.setAdapter(atAdapter);
                GridLayoutManager layoutManagerToday = new GridLayoutManager(ScheduleActivity.this, 1, GridLayoutManager.VERTICAL, false);
                rcvAppointmentToday.setLayoutManager(layoutManagerToday);

                allAppointmentAdapter = new AllAppointmentAdapter(ScheduleActivity.this, appointments, doctors);
                rcvAllAppointment.setAdapter(allAppointmentAdapter);
                GridLayoutManager layoutManagerAll = new GridLayoutManager(ScheduleActivity.this, 1, GridLayoutManager.VERTICAL, false);
                rcvAllAppointment.setLayoutManager(layoutManagerAll);
            }
        });
    }

    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
