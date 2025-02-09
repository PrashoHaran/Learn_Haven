package com.example.learnhaven;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

public class basicEducation extends AppCompatActivity {

    private ViewPager2 bannerSlider;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_education);

        // Initialize the banner slider
        bannerSlider = findViewById(R.id.bannerSlider);

        // Add images to the banner list
        List<Integer> bannerImages = Arrays.asList(
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3
        );

        // Set up the adapter for ViewPager2
        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        bannerSlider.setAdapter(bannerAdapter);

        // Auto-slide functionality
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == bannerImages.size()) {
                    currentPage = 0; // Reset to first image
                }
                bannerSlider.setCurrentItem(currentPage++, true);  // Move to the next image
                sliderHandler.postDelayed(this, 3000);  // Slide every 3 seconds
            }
        };

        // Start the auto-slide
        sliderHandler.postDelayed(sliderRunnable, 3000);

        // Pause auto-slide when user interacts
        bannerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position + 1;
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);  // Restart the slider delay
            }
        });

        // Set up the back button functionality
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the course activity
                Intent intent = new Intent(basicEducation.this, course.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sliderHandler.removeCallbacks(sliderRunnable);  // Prevent memory leaks
    }
}
