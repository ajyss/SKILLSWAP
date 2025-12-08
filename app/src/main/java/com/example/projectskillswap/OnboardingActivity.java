package com.example.projectskillswap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import me.relex.circleindicator.CircleIndicator3;

public class OnboardingActivity extends AppCompatActivity {

    // Deklarasi Views dan Adapter
    private ViewPager2 viewPager;
    private TextView tvNext, tvWelcomeTitle, tvCountryCode;
    private ImageView ivFlag;
    private OnboardingAdapter onboardingAdapter;
    // HAPUS: private String[] slideTitles; // Kita tidak butuh ini lagi

    // ... (kode launcher dan fusedLocationClient tetap sama) ...
    private FusedLocationProviderClient fusedLocationClient;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    fetchDeviceLocation();
                } else {
                    Log.w("Location", "Location permission was denied by the user.");
                    setFlagBasedOnCountryCode("default");
                    updateUIText(0); // Update UI dengan teks awal
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ivFlag = findViewById(R.id.iv_flag);
        tvCountryCode = findViewById(R.id.tv_country_code);
        viewPager = findViewById(R.id.viewPagerOnboarding);
        CardView onboardingCardContainer = findViewById(R.id.onboarding_card_container);
        tvNext = findViewById(R.id.tv_next);
        tvWelcomeTitle = findViewById(R.id.tv_welcome_title);
        CircleIndicator3 indicator = findViewById(R.id.indicator);

        onboardingAdapter = new OnboardingAdapter();
        viewPager.setAdapter(onboardingAdapter);
        indicator.setViewPager(viewPager);

        // -- LOGIKA BARU DAN LEBIH SEDERHANA --
        updateUIText(0); // Atur teks awal untuk posisi 0
        askForLocationPermission();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Panggil fungsi terpusat untuk update semua teks
                updateUIText(position);
            }
        });

        onboardingCardContainer.setOnClickListener(v -> {
            int currentPosition = viewPager.getCurrentItem();
            if (currentPosition < onboardingAdapter.getItemCount() - 1) {
                viewPager.setCurrentItem(currentPosition + 1);
            } else {
                Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Fungsi terpusat untuk memperbarui semua teks di UI berdasarkan posisi slide.
     * Ini memastikan semua teks diambil dari resource yang paling update.
     */
    private void updateUIText(int position) {
        // Atur teks judul secara dinamis
        if (position == 0) {
            tvWelcomeTitle.setText(R.string.onboarding_title_1);
        } else if (position == 1) {
            tvWelcomeTitle.setText(R.string.onboarding_title_2);
        }

        // Atur teks tombol
        if (position == onboardingAdapter.getItemCount() - 1) {
            tvNext.setText(R.string.button_start);
        } else {
            tvNext.setText(R.string.button_next);
        }
    }


    // ... (Semua fungsi lain seperti askForLocationPermission, fetchDeviceLocation, dll, tetap sama persis) ...

    private void askForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchDeviceLocation();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void fetchDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; // Safety check
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        String countryCode = addresses.get(0).getCountryCode(); // "ID", "SG", "JP"
                        Log.i("Location", "Detected Country Code via GPS: " + countryCode);

                        String currentLanguage = getResources().getConfiguration().getLocales().get(0).getLanguage();
                        String targetLanguage = getLanguageForCountry(countryCode);

                        setFlagBasedOnCountryCode(countryCode);

                        if (targetLanguage != null && !currentLanguage.equals(targetLanguage)) {
                            Log.i("LanguageSwitch", "Current: " + currentLanguage + ", Target: " + targetLanguage + ". Switching...");
                            setAppLocaleAndRestart(targetLanguage);
                        }

                    } else {
                        Log.w("Location", "No address found for this location.");
                        setFlagBasedOnCountryCode("default");
                    }
                } catch (IOException e) {
                    Log.e("Location", "Geocoder service not available.", e);
                    setFlagBasedOnCountryCode("default");
                }
            } else {
                Log.w("Location", "Last known location is null.");
                setFlagBasedOnCountryCode("default");
            }
        }).addOnFailureListener(this, e -> {
            Log.e("Location", "Failed to get location.", e);
            setFlagBasedOnCountryCode("default");
        });
    }

    private String getLanguageForCountry(String countryCode) {
        if (countryCode == null) return null;
        switch (countryCode.toUpperCase()) {
            case "ID": return "in";
            case "SG": return "zh";
            case "US": case "GB": case "AU": return "en";
            default: return null;
        }
    }

    private void setAppLocaleAndRestart(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        finish();
        startActivity(getIntent());
    }

    private void setFlagBasedOnCountryCode(String countryCode) {
        if (countryCode == null || countryCode.isEmpty()) {
            countryCode = "default";
        }
        String flagResourceName = "flag_" + countryCode.toLowerCase();
        int flagResId = getResources().getIdentifier(flagResourceName, "drawable", getPackageName());
        if (flagResId == 0) {
            flagResId = R.drawable.flag_default;
            countryCode = "N/A";
        }
        ivFlag.setImageResource(flagResId);
        tvCountryCode.setText(countryCode.toUpperCase());
    }

    // HAPUS FUNGSI LAMA INI
    // private void setupLanguageAndTitles() { ... }
}
