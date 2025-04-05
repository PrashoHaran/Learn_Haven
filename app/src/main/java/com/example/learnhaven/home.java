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

        // Navigate to profile management
        ImageView profileImage = findViewById(R.id.profile);
        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, profile.class);
            startActivity(intent);
        });

        // Navigate to Course Categories
        Button btnCategories = findViewById(R.id.btnCategories);
        btnCategories.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, course.class);
            startActivity(intent);
        });

        // Navigate to Basic Education
        View basicButton = findViewById(R.id.basic);
        basicButton.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, basicEducation.class); // Ensure this class exists
            startActivity(intent);
        });

        // Navigate to E-Book Section
        View ebookButton = findViewById(R.id.ebook);
        ebookButton.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, ebook.class); // Ensure this class exists
            startActivity(intent);
        });

        // Navigate to Technology Section
        View techButton = findViewById(R.id.tech);
        techButton.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, technology.class); // Ensure this class exists
            startActivity(intent);
        });

        // Logout Button Functionality
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, login.class); // Replace 'login.class' with your actual login activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears back stack
            startActivity(intent);
        });

    }
}
