package com.example.learnhaven;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class science extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_science);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        String[] facts = {
                "Did you know? The sun is 400 times larger than the moon!",
                "Did you know? Honey never spoils, even after thousands of years!",
                "Did you know? Water can boil and freeze at the same time!",
                "Did you know? A day on Venus is longer than a year on Venus!"
        };

        TextView txtFact = findViewById(R.id.txt_fact);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;
            @Override
            public void run() {
                txtFact.setText(facts[i]);
                i = (i + 1) % facts.length;
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(runnable);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(science.this, course.class);
                startActivity(intent);
            }
        });


    }
}