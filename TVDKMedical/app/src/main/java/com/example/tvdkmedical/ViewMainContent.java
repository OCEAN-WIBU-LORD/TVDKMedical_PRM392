package com.example.tvdkmedical;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.adapters.PostAdapter;
import com.example.tvdkmedical.models.Appointment;
import com.example.tvdkmedical.models.Post;
import com.example.tvdkmedical.views.appointment.AppointmentDetailsActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ViewMainContent extends AppCompatActivity {
    private static final String TAG = "ViewMainContent";
    MaterialButton buttonLogOut;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView logOut, profileId, doctorName, dateInfo, timeInfo;
    ImageView doctorImage;
    FirebaseUser userDetails;
    Button btnProfile;
    Button appointmentDetailsBtn;
    DatabaseReference database, appointmentDatabase, doctorDatabase;
    PostAdapter postAdapter;
    RecyclerView recyclerView;
    List<Post> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_main_content);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        logOut = findViewById(R.id.logOut);
        appointmentDetailsBtn = findViewById(R.id.appointmentDetails);
        doctorImage = findViewById(R.id.doctor_image);
        doctorName = findViewById(R.id.doctor_name);
        dateInfo = findViewById(R.id.date_info);
        timeInfo = findViewById(R.id.time_info);

        userDetails = mAuth.getCurrentUser();
        if (userDetails == null) {
            Log.e(TAG, "User is not authenticated, redirecting to login");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        } else {
            // profileId.setText(userDetails.getEmail());
        }

        recyclerView = findViewById(R.id.postlist);
        database = FirebaseDatabase.getInstance().getReference().child("posts");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        data = new ArrayList<>();
        postAdapter = new PostAdapter(data, this);
        recyclerView.setAdapter(postAdapter);

        loadAppointments();
        loadPosts();

        logOut.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Log Out successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

        Button btnProfile1 = findViewById(R.id.btnProfile);
        btnProfile1.setOnClickListener(v -> {
            Intent intent = new Intent(ViewMainContent.this, FragmentUserProfile.class);
            startActivity(intent);
        });

        appointmentDetailsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ViewMainContent.this, AppointmentDetailsActivity.class);
            startActivity(intent);
        });
    }

    private void loadAppointments() {
        appointmentDatabase = FirebaseDatabase.getInstance().getReference().child("appointments");
        appointmentDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Appointment nearestAppointment = null;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.hasChild("status") && dataSnapshot.hasChild("userId")) {
                            String status = Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString();
                            String userId = Objects.requireNonNull(dataSnapshot.child("userId").getValue()).toString();

                            if ("pending".equals(status) && userId.equals(userDetails.getUid())) {
                                Integer startTime = dataSnapshot.child("startTime").getValue(Integer.class);
                                Integer endTime = dataSnapshot.child("endTime").getValue(Integer.class);

                                if (startTime != null && endTime != null) {
                                    com.google.firebase.Timestamp startTs = new com.google.firebase.Timestamp(startTime, 0);
                                    com.google.firebase.Timestamp endTs = new com.google.firebase.Timestamp(endTime, 0);

                                    if (nearestAppointment == null || nearestAppointment.getStartTime() == null || startTs.compareTo(nearestAppointment.getStartTime()) < 0) {
                                        String appointmentId = dataSnapshot.getKey();
                                        String diseaseId = Objects.requireNonNull(dataSnapshot.child("diseaseId").getValue()).toString();
                                        String doctorId = Objects.requireNonNull(dataSnapshot.child("doctorId").getValue()).toString();
                                        String note = Objects.requireNonNull(dataSnapshot.child("note").getValue()).toString();

                                        nearestAppointment = new Appointment(appointmentId, diseaseId, doctorId, endTs, note, startTs, status, userId);
                                    }
                                }
                            }
                        } else {
                            Log.e(TAG, "Invalid dataSnapshot: " + dataSnapshot);
                        }
                    }

                    if (nearestAppointment != null) {
                        fetchDoctorDetails(nearestAppointment);
                        updateAppointmentDetails(nearestAppointment);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to read data from Firebase.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to read data from Firebase: " + error.getMessage());
            }
        });
    }

    private void fetchDoctorDetails(Appointment nearestAppointment) {
        doctorDatabase = FirebaseDatabase.getInstance().getReference().child("doctors").child(nearestAppointment.getDoctorId());
        doctorDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot doctorSnapshot) {
                if (doctorSnapshot.exists()) {
                    String doctorNameStr = Objects.requireNonNull(doctorSnapshot.child("name").getValue()).toString();
                    doctorName.setText(doctorNameStr);
                } else {
                    Log.e(TAG, "Doctor data not found for ID: " + nearestAppointment.getDoctorId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to read doctor data from Firebase.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to read doctor data from Firebase: " + error.getMessage());
            }
        });
    }

    private void updateAppointmentDetails(Appointment nearestAppointment) {
        Date startDate = nearestAppointment.getStartTime().toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        dateInfo.setText(dateFormat.format(startDate));
        timeInfo.setText(timeFormat.format(startDate));
    }

    private void loadPosts() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.exists() && dataSnapshot.hasChild("title") && dataSnapshot.hasChild("content") && dataSnapshot.hasChild("author") && dataSnapshot.hasChild("date")) {
                        Post post = new Post();
                        post.setPost_id(Objects.requireNonNull(dataSnapshot.getKey()));
                        post.setTitle(Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString());
                        post.setContent(Objects.requireNonNull(dataSnapshot.child("content").getValue()).toString());
                        post.setAuthor(Objects.requireNonNull(dataSnapshot.child("author").getValue()).toString());
                        post.setCategory("abc"); // Static category since it's commented out

                        String date = Objects.requireNonNull(dataSnapshot.child("date").getValue()).toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        try {
                            Date dateformat = dateFormat.parse(date);
                            post.setDate(dateformat);
                        } catch (ParseException e) {
                            Log.e(TAG, "Date parsing error: " + e.getMessage());
                        }
                        data.add(post);
                    } else {
                        Log.e(TAG, "Invalid post dataSnapshot: " + dataSnapshot);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to read data from Firebase.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to read data from Firebase: " + error.getMessage());
            }
        });
    }
}
