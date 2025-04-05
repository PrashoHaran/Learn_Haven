package com.example.learnhaven;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    EditText username, password;
    MaterialButton loginButton;
    TextView signUpText;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth & Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI Components
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signUpText = findViewById(R.id.signText);

        loginButton.setOnClickListener(v -> {
            String enteredUsername = username.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if (TextUtils.isEmpty(enteredUsername) || TextUtils.isEmpty(userPassword)) {
                Toast.makeText(login.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("users").whereEqualTo("username", enteredUsername)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String userEmail = queryDocumentSnapshots.getDocuments().get(0).getString("email");

                            mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                            Log.d("LOGIN_DEBUG", "Navigating to home.java");

                                            // Redirect to home activity with stack clear
                                            Intent intent = new Intent(login.this, home.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(login.this, "Login Failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(login.this, "Username not found!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(login.this, "Error retrieving username: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        // Navigate to Signup Page when "Sign Up" text is clicked
        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, signup.class);
            startActivity(intent);
        });
    }
}
