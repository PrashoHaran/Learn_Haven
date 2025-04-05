package com.example.learnhaven;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    EditText username, email, password, repassword;
    MaterialButton signUpButton;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI Components
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        signUpButton = findViewById(R.id.signupbtn);

        // Sign Up Button Click Listener
        signUpButton.setOnClickListener(v -> {
            String userName = username.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();
            String confirmPassword = repassword.getText().toString().trim();

            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(signup.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!userPassword.equals(confirmPassword)) {
                Toast.makeText(signup.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if username already exists in Firestore
            db.collection("users").whereEqualTo("username", userName)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Toast.makeText(signup.this, "Username already exists! Try another one.", Toast.LENGTH_SHORT).show();
                        } else {
                            registerUser(userName, userEmail, userPassword);
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(signup.this, "Error checking username: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });
    }

    private void registerUser(String userName, String userEmail, String userPassword) {
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Create user data
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", userName);
                        user.put("email", userEmail);

                        // Save user data with Auto-ID
                        db.collection("users").add(user)
                                .addOnSuccessListener(documentReference -> {
                                    Log.d("Firestore", "User added successfully with ID: " + documentReference.getId());
                                    Toast.makeText(signup.this, "Signup Successful!", Toast.LENGTH_SHORT).show();

                                    // Redirect to login page
                                    startActivity(new Intent(signup.this, login.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error adding user", e);
                                    Toast.makeText(signup.this, "Error saving user data!", Toast.LENGTH_SHORT).show();
                                });

                    } else {
                        Toast.makeText(signup.this, "Signup Failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
