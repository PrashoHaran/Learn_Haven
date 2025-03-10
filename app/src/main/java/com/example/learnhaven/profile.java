package com.example.learnhaven;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class profile extends AppCompatActivity {

    private EditText birth;
    private ImageView calendarIcon, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        birth = findViewById(R.id.birth);
        calendarIcon = findViewById(R.id.calendar_icon);
        backButton = findViewById(R.id.back);  // Move inside onCreate

        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {  // Now it's initialized
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this, home.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showDatePicker() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                profile.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date (e.g., 05/08/2025)
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    birth.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}
