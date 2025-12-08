package com.example.projectskillswap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends BaseActivity {

    private TextView tvWelcomeMessage;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi view dari layout
        tvWelcomeMessage = findViewById(R.id.tv_welcome_message);
        btnLogout = findViewById(R.id.btn_logout);

        // (Opsional) Ambil data dari Intent jika ada
        // Contoh: Mengambil email dari LoginActivity
        // Intent intent = getIntent();
        // String userEmail = intent.getStringExtra("USER_EMAIL");
        // if (userEmail != null) {
        //     tvWelcomeMessage.setText("Selamat Datang,\n" + userEmail);
        // }

        // Atur listener untuk tombol Logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Buat intent untuk kembali ke LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                // Flag ini penting untuk membersihkan histori activity sebelumnya
                // Pengguna tidak akan bisa menekan tombol "Back" untuk kembali ke MainActivity setelah logout
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish(); // Tutup MainActivity
            }
        });
    }
}
