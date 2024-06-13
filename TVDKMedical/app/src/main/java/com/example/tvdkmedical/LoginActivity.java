package com.example.tvdkmedical;

import static com.example.tvdkmedical.R.*;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {


    EditText editTextEmail, editTextPassword;
    MaterialButton buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    @Override
    public void onStart(){
        super.onStart();
        //check if user is signed in (non-null) and Update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),ViewMainContent.class);
            startActivity(intent);
            finish();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);


        // Find the return button by its ID
        ImageButton returnButton = findViewById(R.id.btnReturn);

        // Set OnClickListener to the return button
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action to be performed when the button is clicked
                finish(); // This will finish the current activity and go back to the previous one
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String uid = user.getUid();
                                        // Lưu UID vào SharedPreferences
                                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("USER_UID", uid);
                                        editor.apply();
                                        Toast.makeText(getApplicationContext(), "Login Successful",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), ViewMainContent.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }else{
                                    Toast.makeText(LoginActivity.this, "Authentication Failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



    }
    // Function to open Facebook app
    // Method to open Facebook app
    public void openFacebookApp(View view) {
        try {
            // Facebook URL
            String facebookUrl = "https://www.fb.com/";

            // Create Intent with Facebook URL
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            facebookIntent.setData(Uri.parse(facebookUrl));

            // Check if Facebook app is installed, if not, open in browser
            if (facebookIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(facebookIntent);
            } else {
                // If Facebook app is not installed, open in browser
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Function to open Google app
    // Method to open Google app
    public void openGoogleApp(View view) {
        try {
            // Facebook URL
            String googleUrl = "https://www.google.com/";

            // Create Intent with Facebook URL
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            facebookIntent.setData(Uri.parse(googleUrl));

            // Check if Facebook app is installed, if not, open in browser
            if (facebookIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(facebookIntent);
            } else {
                // If Facebook app is not installed, open in browser
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(googleUrl)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Function to open Google app
    // Method to open Google app
    public void openGithubApp(View view) {
        try {
            // Facebook URL
            String googleUrl = "https://www.github.com/";

            // Create Intent with Facebook URL
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            facebookIntent.setData(Uri.parse(googleUrl));

            // Check if Facebook app is installed, if not, open in browser
            if (facebookIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(facebookIntent);
            } else {
                // If Facebook app is not installed, open in browser
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(googleUrl)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Function to open Google app
    // Method to open Google app
    public void openTwitterApp(View view) {
        try {
            // Facebook URL
            String googleUrl = "https://www.x.com/";

            // Create Intent with Facebook URL
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            facebookIntent.setData(Uri.parse(googleUrl));

            // Check if Facebook app is installed, if not, open in browser
            if (facebookIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(facebookIntent);
            } else {
                // If Facebook app is not installed, open in browser
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(googleUrl)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}