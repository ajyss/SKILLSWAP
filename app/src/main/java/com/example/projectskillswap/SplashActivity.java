package com.example.projectskillswap;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashActivity extends AppCompatActivity {

    // Jeda dalam milidetik (2000ms = 2 detik)
    private static final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Buat Handler untuk menunda perpindahan ke OnboardingActivity.
        // TIDAK ADA LAGI LOGIKA BAHASA DI SINI.
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Buat Intent untuk pindah ke OnboardingActivity
            Intent intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            startActivity(intent);

            // Tutup SplashActivity agar pengguna tidak bisa kembali ke sini
            finish();
        }, SPLASH_DELAY);
    }
}
