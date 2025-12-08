// File: BaseActivity.java
package com.example.projectskillswap;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        // "Mencegat" konteks sebelum activity dibuat, lalu menerapkan Locale baru
        super.attachBaseContext(updateResources(newBase, "in")); // "in" untuk Indonesia
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        return context;
    }
}
    