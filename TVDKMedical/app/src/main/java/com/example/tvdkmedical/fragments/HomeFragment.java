package com.example.tvdkmedical.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvdkmedical.LoginActivity;
import com.example.tvdkmedical.R;
import com.example.tvdkmedical.UserProfileActivity;
import com.example.tvdkmedical.ViewMainContent;
import com.example.tvdkmedical.adapters.PostAdapter;
import com.example.tvdkmedical.databinding.ActivityMainBinding;
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
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class HomeFragment extends Fragment {

    MaterialButton buttonLogOut;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView logOut, profileId, doctorName,dateInfo,timeInfo;
    ImageView doctorImage;
    FirebaseUser userDetails;
    Button btnProfile;
    Button appointmentDetailsBtn;
    DatabaseReference database, appointmentDatabase, doctorDatabase;
    PostAdapter postAdapter;
    RecyclerView recyclerView;
    List<Post> data;

    ActivityMainBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home2, container, false);
        mAuth = FirebaseAuth.getInstance();
        logOut = rootView.findViewById
                (R.id.logOut);
        //profileId = findViewById(R.id.profileId);
        appointmentDetailsBtn = rootView.findViewById(R.id.appointmentDetails);
        doctorImage = rootView.findViewById(R.id.doctor_image);
        doctorName = rootView.findViewById(R.id.doctor_name);
        dateInfo = rootView.findViewById(R.id.date_info);
        timeInfo = rootView.findViewById(R.id.time_info);
        progressBar =  getActivity().findViewById(R.id.progress_bar);

        userDetails = mAuth.getCurrentUser();
        if (userDetails == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            // profileId.setText(userDetails.getEmail());
        }

        recyclerView = rootView.findViewById(R.id.postlist);
        database = FirebaseDatabase.getInstance().getReference().child("posts");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        data = new ArrayList<>();
        postAdapter = new PostAdapter(data, getActivity());
        recyclerView.setAdapter(postAdapter);
        progressBar.setVisibility(View.VISIBLE);
        appointmentDatabase = FirebaseDatabase.getInstance().getReference().child("appointments");
        appointmentDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Appointment nearestAppointment = new Appointment();
                    long now = new Date().getTime();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String status = Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString();
                        String userId = Objects.requireNonNull(dataSnapshot.child("userId").getValue()).toString();

                        if ("pending".equals(status) && userId.equals(userDetails.getUid())) {
                            int startTime = Objects.requireNonNull(dataSnapshot.child("startTime").getValue(Integer.class));
                            com.google.firebase.Timestamp startTs = new com.google.firebase.Timestamp(startTime, 0);
                            int endTime = Objects.requireNonNull(dataSnapshot.child("endTime").getValue(Integer.class));
                            com.google.firebase.Timestamp endTs = new com.google.firebase.Timestamp(endTime, 0);
                            if (nearestAppointment == null || nearestAppointment.getStartTime() == null || startTs.compareTo(nearestAppointment.getStartTime()) < 0) {
                                String appointmentId = Objects.requireNonNull(dataSnapshot.getKey().toString());
                                String diseasedId = Objects.requireNonNull(dataSnapshot.child("diseaseId").getValue()).toString();
                                String doctorId = Objects.requireNonNull(dataSnapshot.child("doctorId").getValue()).toString();
                                String note = Objects.requireNonNull(dataSnapshot.child("note").getValue()).toString();

                                nearestAppointment = new Appointment(appointmentId, diseasedId, doctorId, endTs, note, startTs, status, userId);
                            }
                        }
                    }

                    if (nearestAppointment != null) {
                        DatabaseReference doctorDatabase = FirebaseDatabase.getInstance().getReference().child("doctors").child(nearestAppointment.getDoctorId());
                        doctorDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot doctorSnapshot) {
                                if (doctorSnapshot.exists()) {
                                    String doctorNameStr = Objects.requireNonNull(doctorSnapshot.child("name").getValue()).toString();
                                    doctorName.setText(doctorNameStr);
                                    String doctorImageUrl = Objects.requireNonNull(doctorSnapshot.child("imageurl").getValue().toString());
                                    Picasso.get().load(doctorImageUrl).into(doctorImage);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getActivity(), "Failed to read doctor data from Firebase.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        System.out.println("aa " + nearestAppointment.getStartTime() + " bb " + nearestAppointment.getEndTime());
                        Date startDate = nearestAppointment.getEndTime().toDate();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        dateInfo.setText(dateFormat.format(startDate));
                        timeInfo.setText(timeFormat.format(startDate));
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to read data from Firebase.", Toast.LENGTH_SHORT).show();
            }
        });
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = new Post();
                    //String i =Objects.requireNonNull(dataSnapshot.getKey().toString());
                    post.setPost_id(Objects.requireNonNull(dataSnapshot.getKey()));
                    post.setTitle(Objects.requireNonNull(dataSnapshot.child("title").getValue().toString()));
                    post.setContent(Objects.requireNonNull(dataSnapshot.child("content").getValue().toString()));
                    post.setAuthor(Objects.requireNonNull(dataSnapshot.child("author").getValue().toString()));
                    //   post.setCategory(Objects.requireNonNull(dataSnapshot.child("category").getValue().toString()));
                    post.setCategory("abc");
                    String date = Objects.requireNonNull(dataSnapshot.child("date").getValue().toString());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateformat = null;
                    try {
                        dateformat = dateFormat.parse(date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    post.setDate(dateformat);
                    data.add(post);
                }
                postAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to read data from Firebase.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "Log Out successfully", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
        Button btnProfile1 = rootView.findViewById(R.id.btnProfile);

        // Set OnClickListener to the button
        btnProfile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start LoginActivity
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intent);
            }
        });

        appointmentDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppointmentDetailsActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}