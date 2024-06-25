package com.example.tvdkmedical;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.adapters.DayAdapter;
import com.example.tvdkmedical.models.Appointment;
import com.example.tvdkmedical.models.Day;
import com.example.tvdkmedical.models.Doctor;
import com.example.tvdkmedical.repositories.AppointmentResp;
import com.example.tvdkmedical.repositories.DoctorResp;
import com.example.tvdkmedical.repositories.callbacks.Callback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddAppointment extends AppCompatActivity implements DayAdapter.OnAddDayClickListener {

    private Spinner spinnerMonths;
    private RecyclerView rcv;
    private RecyclerView rcvTime;
    private ArrayList<Day> days;
    private List<String> times;
    private ArrayList<Appointment> appointments;
    private DayAdapter adapter;
    private TimeAdapter timeAdapter;
    private int selectedMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_appointment);

        spinnerMonths = findViewById(R.id.monthSpinner);
        rcv = findViewById(R.id.rcvDay);
        rcvTime = findViewById(R.id.rcvTime);
        days = new ArrayList<>();
        assignData();
        initSpinner();
        initRecyclerView();
        initTimeRecyclerView();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void assignData() {
        times = new ArrayList<>(Arrays.asList("20:26", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"));
    }

    private void initSpinner() {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonths.setAdapter(adapter);
        spinnerMonths.setSelection(0);

        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = position;
                updateDaysOfMonth(selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecyclerView() {
        adapter = new DayAdapter(this, days, this);
        rcv.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        rcv.setLayoutManager(layoutManager);
    }

    private void initTimeRecyclerView() {
        timeAdapter = new TimeAdapter(this, times);
        rcvTime.setAdapter(timeAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        rcvTime.setLayoutManager(layoutManager);
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAddDayClick(int position, Day day) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = spinnerMonths.getSelectedItemPosition();
        int dayOfMonth = Integer.parseInt(day.getDay());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long timestamp = calendar.getTimeInMillis() / 1000;

        AppointmentResp appointmentResp = new AppointmentResp();
        appointmentResp.getAppointmentsByDoctorAndTime("doctor_id_1", timestamp, new Callback<Appointment>() {
            @Override
            public void onCallback(List<Appointment> appointmentList) {
                appointments = new ArrayList<>(appointmentList);

                // Tạo một danh sách mới để lưu trữ giờ không thể chọn
                List<String> disabledTimes = new ArrayList<>();

                // Lặp qua danh sách các cuộc hẹn
                for (Appointment appointment : appointments) {
                    // Lấy giờ bắt đầu từ startTime của cuộc hẹn
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTimeInMillis(appointment.getStartTime().getSeconds() * 1000);
                    int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
                    int startMinute = startCalendar.get(Calendar.MINUTE);

                    // Chuyển giờ bắt đầu thành định dạng "HH:mm"
                    String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);

                    // Thêm vào danh sách giờ không thể chọn
                    disabledTimes.add(startTime);
                }

                // Cập nhật lại danh sách disabledTimes trong adapter
                timeAdapter.setDisabledTimes(disabledTimes);
            }
        });
    }



}
