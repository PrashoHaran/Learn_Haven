package com.example.learnhaven;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private List<Integer> bannerImages;  // List of image resources (IDs)

    // Constructor to pass the list of images
    public BannerAdapter(List<Integer> bannerImages) {
        this.bannerImages = bannerImages;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the banner_item.xml layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_item, parent, false);
        return new BannerViewHolder(itemView);  // Return a new ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        // Set the image for each item in the ViewPager2
        holder.imageView.setImageResource(bannerImages.get(position));  // Set image for current position
    }

    @Override
    public int getItemCount() {
        return bannerImages.size();  // Return the number of items in the list
    }

    // ViewHolder class to hold references to the views
    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.banner_image);  // Find the ImageView by ID
        }
    }
}
