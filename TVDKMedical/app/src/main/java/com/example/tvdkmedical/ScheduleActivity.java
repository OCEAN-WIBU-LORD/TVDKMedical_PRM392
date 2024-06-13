package com.example.tvdkmedical;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.google.firebase.Timestamp;

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

    private List<Appointment> appointments;
    private List<Doctor> doctors;
    private DayAdapter adapter;
    private AppointmentTodayAdapter atAdapter;

    private AllAppointmentAdapter allAppointmentAdapter;
    private TextView currentDate;

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
        loadFakeData();
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
                // Chuyển đổi tên tháng thành số tháng (ví dụ: "January" thành 0, "February" thành 1, v.v.)
                int monthIndex = position;
                // Cập nhật ngày trong tháng được chọn
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
        atAdapter = new AppointmentTodayAdapter(this, appointments,doctors);
        rcvAppointmentToday.setAdapter(atAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rcvAppointmentToday.setLayoutManager(layoutManager);
    }

    private void initRecyclerViewAllAppointment() {
        allAppointmentAdapter = new AllAppointmentAdapter(this, appointments,doctors);
        rcvAllAppointment.setAdapter(allAppointmentAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rcvAllAppointment.setLayoutManager(layoutManager);
    }

    private void setCurrentDate() {
        Date currentDateTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String formattedDate = dateFormat.format(currentDateTime);
        currentDate.setText(formattedDate);
    }

    private void updateDaysOfMonth(int selectedMonth) {
        // Xóa dữ liệu cũ
        days.clear();

        // Lấy ngày đầu tiên của tháng được chọn
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, selectedMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Lặp qua từng ngày trong tháng và lấy ngày và thứ trong tuần của mỗi ngày
        int numDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < numDaysInMonth; i++) {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
            String shortDayOfWeek = dayOfWeek.substring(0, 2);
            days.add(new Day(String.valueOf(day), shortDayOfWeek));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Thông báo cho adapter rằng dữ liệu đã thay đổi, nếu adapter không null
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            // Ghi log hoặc xử lý trường hợp adapter null
            Toast.makeText(ScheduleActivity.this, "Adapter chưa được khởi tạo", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFakeData() {
        appointments = new ArrayList<>();
        doctors = new ArrayList<>();

        // Fake data for doctors
        doctors.add(new Doctor(1, "Dr. John Doe", new Date(1980, 1, 1), "Cardiologist", 5));
        doctors.add(new Doctor(2, "Dr. Jane Smith", new Date(1985, 5, 10), "Dermatologist", 4));
        doctors.add(new Doctor(3, "Dr. Richard Roe", new Date(1975, 3, 15), "Neurologist", 3));
        doctors.add(new Doctor(4, "Dr. Emily Davis", new Date(1990, 7, 25), "Pediatrician", 5));
        doctors.add(new Doctor(5, "Dr. Michael Brown", new Date(1983, 11, 30), "Surgeon", 4));

        // Fake data for appointments
        appointments.add(new Appointment("appointment_id_1", "disease_id_1", "1", new Timestamp(new Date().getTime() / 1000, 0), "Regular check-up", new Timestamp(new Date().getTime() / 1000, 0), "pending", "user_id_1"));
        appointments.add(new Appointment("appointment_id_2", "disease_id_2", "2", new Timestamp(new Date().getTime() / 1000, 0), "Follow-up visit", new Timestamp(new Date().getTime() / 1000, 0), "confirmed", "user_id_2"));
        appointments.add(new Appointment("appointment_id_3", "disease_id_3", "3", new Timestamp(new Date().getTime() / 1000, 0), "Initial consultation", new Timestamp(new Date().getTime() / 1000, 0), "completed", "user_id_3"));
        appointments.add(new Appointment("appointment_id_4", "disease_id_4", "4", new Timestamp(new Date().getTime() / 1000, 0), "Routine check-up", new Timestamp(new Date().getTime() / 1000, 0), "cancelled", "user_id_4"));
        appointments.add(new Appointment("appointment_id_5", "disease_id_5", "5", new Timestamp(new Date().getTime() / 1000, 0), "Emergency visit", new Timestamp(new Date().getTime() / 1000, 0), "pending", "user_id_5"));
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
