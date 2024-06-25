package com.example.tvdkmedical.views.appointment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import com.example.tvdkmedical.models.Record;
import com.example.tvdkmedical.models.User;
import com.example.tvdkmedical.repositories.AppointmentResp;
import com.example.tvdkmedical.repositories.DiseaseResp;
import com.example.tvdkmedical.repositories.DoctorResp;
import com.example.tvdkmedical.repositories.RecordResp;
import com.example.tvdkmedical.repositories.UserResp;
import com.example.tvdkmedical.repositories.callbacks.Callback;
import com.example.tvdkmedical.utils.Utils;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppointmentDetailsActivity extends AppCompatActivity {
    private RecyclerView rvDiseases;
    private DiseaseAdapter diseaseAdapter;
    private RecyclerView rvRecords;
    private RecordAdapter recordAdapter;

    // Appointment all layout
    TextView dateBooking, appointmentStartTime, appointmentEndTime, doctorNameAppointment, doctorInforAppointment, noRecords;
    ImageView imgAvatar;
    Button btnReschedule, btnCancel, btnAddRecord;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Check if the user is a doctor or not
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
       new UserResp().getUser(currentUser.getUid(), new Callback<User>() {
            @Override
            public void onCallback(List<User> userList) {
                setContentView(R.layout.activity_appointment_details);
                findViews();

                User user = userList.get(0);
                if (!user.isDoctor()) {
                    initViewsUser();
                } else {
                    initViewsDoctor();
                }

                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });
            }
        });
    }

    private void findViews() {
        dateBooking = findViewById(R.id.dateBooking);
        appointmentStartTime = findViewById(R.id.appointmentStartTime);
        appointmentEndTime = findViewById(R.id.appointmentEndTime);
        doctorNameAppointment = findViewById(R.id.doctorNameAppointment);
        doctorInforAppointment = findViewById(R.id.doctorInforAppointment);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnReschedule = findViewById(R.id.btnReschedule);
        btnCancel = findViewById(R.id.btnCancel);
        btnAddRecord = findViewById(R.id.btnAddRecord);
        rvDiseases = findViewById(R.id.diseaseList);
        rvRecords = findViewById(R.id.recordList);
        noRecords = findViewById(R.id.noRecords);
    }

    @SuppressLint("SetTextI18n")
    private void initViewsDoctor() {
        AppointmentResp appointmentResp = new AppointmentResp();
        DiseaseResp diseaseResp = new DiseaseResp();

        String appointmentId = getIntent().getStringExtra("appointmentId");

        appointmentResp.getAppointmentById(appointmentId, new Callback<Appointment>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCallback(List<Appointment> appointmentList) {
                // Get appointment data
                List<Appointment> appointments = new ArrayList<>(appointmentList);
                Appointment appointment = appointments.get(0);

                // Get patient data
                String patientId = appointment.getUserId();
                new UserResp().getUser(patientId, new Callback<User>() {
                    @Override
                    public void onCallback(List<User> userList) {
                        User user = userList.get(0);

                        dateBooking.setText(Utils.formatTimestampToDate(appointment.getStartTime()));
                        appointmentStartTime.setText(Utils.formatTimestampToTime(appointment.getStartTime()));
                        appointmentEndTime.setText(Utils.formatTimestampToTime(appointment.getEndTime()));
                        doctorNameAppointment.setText(user.getName());
                        doctorInforAppointment.setText(user.getAddress());
                        imgAvatar.setImageResource(R.drawable.avatar_default);
                    }
                });

                // Get disease data
                String diseaseId = appointment.getDiseaseId();
                diseaseResp.getDiseaseById(diseaseId, new Callback<Disease>() {
                    @Override
                    public void onCallback(List<Disease> diseaseList) {
                        // Initialize the RecyclerView inside the disease_list layout
                        diseaseAdapter = new DiseaseAdapter(diseaseList);
                        rvDiseases.setAdapter(diseaseAdapter);
                        rvDiseases.setLayoutManager(new LinearLayoutManager(AppointmentDetailsActivity.this));
                    }
                });

                new RecordResp().getRecordById(appointment.getRecordId(), new Callback<Record>() {
                    @Override
                    public void onCallback(List<Record> recordList) {
                        // Initialize the RecyclerView inside the disease_list layout
                        if (recordList.isEmpty()) {
                            noRecords.setVisibility(View.VISIBLE);
                            rvRecords.setVisibility(View.GONE);
                            btnAddRecord.setText("Publish Result");
                            return;
                        }

                        Record record = recordList.get(0);

                        noRecords.setVisibility(View.GONE);
                        try {
                            recordAdapter = new RecordAdapter(recordList);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        rvRecords.setAdapter(recordAdapter);
                        rvRecords.setLayoutManager(new LinearLayoutManager(AppointmentDetailsActivity.this));

                        // Handle the click event of the Add Record button
                        btnAddRecord.setText("Update Result");
                        btnAddRecord.setOnClickListener(v -> {
                            // Show a popup to update the results
                            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                            View popupView = inflater.inflate(R.layout.publish_record_popup, null);

                            // Create a new PopupWindow instance
                            int width = 1200;
                            int height = 1500;
                            boolean focusable = true;
                            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                            popupWindow.showAtLocation(btnAddRecord, Gravity.CENTER, 0, 0);

                            // Dim the background
                            View container = (View) popupWindow.getContentView().getParent();
                            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                            WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
                            // add flag
                            p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                            p.dimAmount = 0.3f;
                            wm.updateViewLayout(container, p);

                            // Show the popup at the center of the screen
                            popupWindow.showAtLocation(btnAddRecord, Gravity.CENTER, 0, 0);

                            // Fill data if available
                            EditText etDiagnosis = popupView.findViewById(R.id.etDiagnosis);
                            EditText etTreatment = popupView.findViewById(R.id.etTreatment);
                            if (!Objects.equals(record.getRecordId(), "")){
                                etDiagnosis.setText(record.getDiagnosis());
                                etTreatment.setText(record.getTreatment());
                            }

                            // Handle the click event of the Publish Record button
                            handleBtnPublishRecord(
                                    popupView.findViewById(R.id.btnPublishRecord),
                                    record,
                                    etDiagnosis,
                                    etTreatment,
                                    popupWindow
                            );
                        });
                    }
                });
            }
        });
    }

    private void handleBtnPublishRecord(Button btnPublishRecord, Record record, EditText etDiagnosis, EditText etTreatment, PopupWindow popupWindow) {
        btnPublishRecord.setOnClickListener(v1 -> {
            record.setDiagnosis(etDiagnosis.getText().toString());
            record.setTreatment(etTreatment.getText().toString());
            record.setDate(new Timestamp(Timestamp.now().getSeconds(), 0));

            // TODO: Add if new else update
            if (!Objects.equals(record.getRecordId(), "")) {
                // Update
                new RecordResp().updateRecord(record, new Callback<Record>() {
                    @Override
                    public void onCallback(List<Record> recordList) {
                        // Update the record list
                    }
                });
            } else {
                // Create
                new RecordResp().createRecord(record, new Callback<Record>() {
                    @Override
                    public void onCallback(List<Record> recordList) {
                        // Update the record list
                    }
                });
            }

            // Reload the activity
            initViewsDoctor();
            popupWindow.dismiss();
        });
    }

    private void initViewsUser() {
        AppointmentResp appointmentResp = new AppointmentResp();
        DiseaseResp diseaseResp = new DiseaseResp();

        String appointmentId = getIntent().getStringExtra("appointmentId");

        appointmentResp.getAppointmentById(appointmentId, new Callback<Appointment>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCallback(List<Appointment> appointmentList) {
                // Get appointment data
                List<Appointment> appointments = new ArrayList<>(appointmentList);
                Appointment appointment = appointments.get(0);

                // Get doctor data
                String doctorId = appointment.getDoctorId();
                new DoctorResp().getDoctorById(doctorId, new Callback<com.example.tvdkmedical.models.Doctor>() {
                    @Override
                    public void onCallback(List<com.example.tvdkmedical.models.Doctor> doctorList) {
                        // Fill data for appointment all layout
                        dateBooking.setText(Utils.formatTimestampToDate(appointment.getStartTime()));
                        appointmentStartTime.setText(Utils.formatTimestampToTime(appointment.getStartTime()));
                        appointmentEndTime.setText(Utils.formatTimestampToTime(appointment.getEndTime()));
                        doctorNameAppointment.setText(doctorList.get(0).getName());
                        doctorInforAppointment.setText(doctorList.get(0).getBio());
                        imgAvatar.setImageResource(R.drawable.avatar_default);
                    }
                });

                // Get disease data
                String diseaseId = appointment.getDiseaseId();
                diseaseResp.getDiseaseById(diseaseId, new Callback<Disease>() {
                    @Override
                    public void onCallback(List<Disease> diseaseList) {
                        // Initialize the RecyclerView inside the disease_list layout
                        diseaseAdapter = new DiseaseAdapter(diseaseList);
                        rvDiseases.setAdapter(diseaseAdapter);
                        rvDiseases.setLayoutManager(new LinearLayoutManager(AppointmentDetailsActivity.this));
                    }
                });

                // Initialize the RecyclerView inside the disease_list layout
                List<Record> recordList = new ArrayList<>();
                // Demo data for records
                Timestamp timestamp1 = Timestamp.now();
                JSONObject extraData1 = new JSONObject();

                Timestamp timestamp2 = Timestamp.now();
                JSONObject extraData2 = new JSONObject();

                recordList.add(new Record("record1", "diagnosis1", "treatment1", timestamp1, extraData1));
                recordList.add(new Record("record2", "diagnosis2", "treatment2", timestamp2, extraData2));

                // remove all elements in recordList
                recordList.clear();

                if (recordList.isEmpty()) {
                    noRecords.setVisibility(View.VISIBLE);
                    rvRecords.setVisibility(View.GONE);
                    return;
                }

                noRecords.setVisibility(View.GONE);
                try {
                    recordAdapter = new RecordAdapter(recordList);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                rvRecords.setAdapter(recordAdapter);
                rvRecords.setLayoutManager(new LinearLayoutManager(AppointmentDetailsActivity.this));
            }
        });

        // Set visibility for user layout
        btnAddRecord.setVisibility(View.GONE);
    }
}