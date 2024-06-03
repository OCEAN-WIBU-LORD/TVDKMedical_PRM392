package com.example.tvdkmedical;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ViewMainContent extends AppCompatActivity {
    MaterialButton buttonLogOut;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView logOut, profileId;
    FirebaseUser userDetails;
    Button btnProfile;


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

        mAuth =FirebaseAuth.getInstance();
        logOut = findViewById(R.id.logOut);
        profileId = findViewById(R.id.profileId);
        userDetails = mAuth.getCurrentUser();
        if(userDetails == null){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            profileId.setText(userDetails.getEmail());
        }


        logOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Log Out successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        Button btnProfile1 = findViewById(R.id.btnProfile);

        // Set OnClickListener to the button
        btnProfile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start LoginActivity
                Intent intent = new Intent(ViewMainContent.this, UserProfileActivity.class);

                // Start the LoginActivity
                startActivity(intent);
            }
        });
    }
}