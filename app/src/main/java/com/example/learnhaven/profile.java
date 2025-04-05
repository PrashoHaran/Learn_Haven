package com.example.learnhaven;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;

public class profile extends AppCompatActivity {

    private EditText firstName, lastName, username, email, phone, birth;
    private ImageView calendarIcon, backButton;
    private Button btnInsert, btnUpdate, btnDelete, btnLogout;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            userId = user.getUid(); // Get authenticated user ID
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(profile.this, login.class));
            finish();
            return;
        }

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize UI components
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone_number);
        birth = findViewById(R.id.birth);
        calendarIcon = findViewById(R.id.calendar_icon);
        backButton = findViewById(R.id.back);
        btnInsert = findViewById(R.id.btn_insert);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        btnLogout = findViewById(R.id.btn_logout);

        // Open date picker on click
        birth.setOnClickListener(v -> showDatePicker());

        // Navigate back to home
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(profile.this, home.class);
            startActivity(intent);
            finish();
        });

        // Insert Data
        btnInsert.setOnClickListener(v -> insertUserData());

        // Retrieve Data
        retrieveUserData();

        // Update Data
        btnUpdate.setOnClickListener(v -> updateUserData());

        // Delete Data
        btnDelete.setOnClickListener(v -> deleteUserData());

        // Logout Button
        btnLogout.setOnClickListener(v -> {
            auth.signOut(); // Sign out the user
            Intent intent = new Intent(profile.this, login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    // Show Date Picker
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                profile.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    birth.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    // Insert User Data (CREATE)
    private void insertUserData() {
        String fName = firstName.getText().toString().trim();
        String lName = lastName.getText().toString().trim();
        String user = username.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String phoneNumber = phone.getText().toString().trim();
        String birthDate = birth.getText().toString().trim();

        if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(user) ||
                TextUtils.isEmpty(mail) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(birthDate)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfile userProfile = new UserProfile(fName, lName, user, mail, phoneNumber, birthDate);
        databaseReference.child(userId).setValue(userProfile)
                .addOnSuccessListener(aVoid -> Toast.makeText(profile.this, "Profile Saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(profile.this, "Failed to Save", Toast.LENGTH_SHORT).show());
    }

    // Retrieve User Data (READ)
    private void retrieveUserData() {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserProfile userProfile = snapshot.getValue(UserProfile.class);
                    if (userProfile != null) {
                        firstName.setText(userProfile.getFirstName());
                        lastName.setText(userProfile.getLastName());
                        username.setText(userProfile.getUsername());
                        email.setText(userProfile.getEmail());
                        phone.setText(userProfile.getPhone());
                        birth.setText(userProfile.getBirthDate());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profile.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update User Data (UPDATE)
    private void updateUserData() {
        String fName = firstName.getText().toString().trim();
        String lName = lastName.getText().toString().trim();
        String user = username.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String phoneNumber = phone.getText().toString().trim();
        String birthDate = birth.getText().toString().trim();

        if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(user) ||
                TextUtils.isEmpty(mail) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(birthDate)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfile userProfile = new UserProfile(fName, lName, user, mail, phoneNumber, birthDate);
        databaseReference.child(userId).setValue(userProfile)
                .addOnSuccessListener(aVoid -> Toast.makeText(profile.this, "Profile Updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(profile.this, "Update Failed", Toast.LENGTH_SHORT).show());
    }

    // Delete User Data (DELETE)
    private void deleteUserData() {
        databaseReference.child(userId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(profile.this, "Profile Deleted", Toast.LENGTH_SHORT).show();
                    firstName.setText("");
                    lastName.setText("");
                    username.setText("");
                    email.setText("");
                    phone.setText("");
                    birth.setText("");
                })
                .addOnFailureListener(e -> Toast.makeText(profile.this, "Failed to Delete", Toast.LENGTH_SHORT).show());
    }
}
