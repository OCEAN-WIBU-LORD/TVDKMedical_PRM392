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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.adapters.PostAdapter;
import com.example.tvdkmedical.databinding.ActivityMainBinding;
import com.example.tvdkmedical.databinding.ActivityViewMainContentBinding;
import com.example.tvdkmedical.fragments.AppointmentFragment;
import com.example.tvdkmedical.fragments.HomeFragment;
import  com.example.tvdkmedical.R;
import com.example.tvdkmedical.fragments.UserProfileFragment;
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

      ActivityViewMainContentBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewMainContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomnavigation.setOnItemSelectedListener(item -> {

            switch (item.getTitle().toString()) {
                case "Home":
                    replaceFragment(new HomeFragment());
                    break;
                case "Profile":
                    replaceFragment(new FragmentUserProfile());
                    break;

                case "Appointment":
                    replaceFragment(new AppointmentFragment());
                    break;
                case "Search":
                    break;
                default:
                    break;
            }
            return true;
        });

    }
    private void replaceFragment(Fragment Fragment) {
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,Fragment);
        fragmentTransaction.commit();


    }
}
