package com.example.learnhaven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Navigate to Forum
        ImageView forumImage = findViewById(R.id.forum);
        forumImage.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, forum.class);
            startActivity(intent);
        });

        // Navigate to Course
        Button btnCategories = findViewById(R.id.btnCategories);
        btnCategories.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, course.class);
            startActivity(intent);
        });

        View basicButton = findViewById(R.id.basic);
        basicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, basicEducation.class); // Ensure 'Gaming' is the correct class name
                startActivity(intent);
            }
        });

        View ebookButton = findViewById(R.id.ebook);
        ebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, ebook.class); // Ensure 'Gaming' is the correct class name
                startActivity(intent);
            }
        });

        View techButton = findViewById(R.id.tech);
        techButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, technology.class); // Ensure 'Gaming' is the correct class name
                startActivity(intent);
            }
        });


    }
}
