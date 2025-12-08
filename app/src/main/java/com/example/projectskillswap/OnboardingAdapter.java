package com.example.projectskillswap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    // Data untuk onboarding, misalnya gambar-gambar yang mau ditampilkan
    private final int[] onboardingImages = {
            R.drawable.image_onboarding1, // Pastikan Anda punya gambar ini di res/drawable
            R.drawable.image_onboarding2  // Pastikan Anda punya gambar ini di res/drawable
    };

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Membuat view untuk setiap item dari layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_boarding, parent, false);
        return new OnboardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        // Mengatur data (gambar) untuk item di posisi tertentu
        holder.imageView.setImageResource(onboardingImages[position]);
    }

    @Override
    public int getItemCount() {
        // Mengembalikan jumlah total item
        return onboardingImages.length;
    }

    // ViewHolder untuk menyimpan referensi view dari setiap item
    static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_onboarding_image); // Pastikan ID ini ada di item_onboarding.xml
        }
    }
}
