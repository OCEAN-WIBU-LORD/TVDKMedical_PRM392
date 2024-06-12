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
        atAdapter = new AppointmentTodayAdapter(this, appointments);
        rcvAppointmentToday.setAdapter(atAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rcvAppointmentToday.setLayoutManager(layoutManager);
    }

    private void initRecyclerViewAllAppointment() {
        allAppointmentAdapter = new AllAppointmentAdapter(this, appointments);
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
        Users user1 = new Users(1, "Nguyễn Văn A", "emailA@example.com", "passwordA", "001");
        Users user2 = new Users(2, "Trần Thị B", "emailB@example.com", "passwordB", "002");

        Doctor doctor1 = new Doctor(1, "Bác sĩ Trần Văn C", parseDate("1990-01-01"), "Chuyên khoa Tim mạch", 5);
        Doctor doctor2 = new Doctor(2, "Bác sĩ Lê Thị D", parseDate("1985-05-15"), "Chuyên khoa Da liễu", 4);

        Date dateBooking = parseDate("2024-06-15 10:00:00");
        Date startTime = parseDate("2024-06-15 10:00:00");
        Date endTime = parseDate("2024-06-15 11:00:00");

        appointments.add(new Appointment(user1, doctor1, dateBooking, "Kiểm tra sức khỏe", 101, startTime, endTime));
        appointments.add(new Appointment(user2, doctor2, dateBooking, "Tư vấn", 102, startTime, endTime));
        appointments.add(new Appointment(user1, doctor2, dateBooking, "Tái khám", 103, startTime, endTime));

        if (atAdapter != null) {
            atAdapter.notifyDataSetChanged();
        }
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
