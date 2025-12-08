package com.example.projectskillswap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.BackEventCompat;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends BaseActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hubungkan kelas Java ini dengan file layout XML-nya
        setContentView(R.layout.activity_login);

        // Inisialisasi semua view dari layout
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        // 1. Atur listener untuk tombol "Masuk"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Di sini Anda akan menambahkan logika validasi login
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                // Contoh validasi sederhana
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email dan kata sandi tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    // Jika berhasil, pindah ke halaman utama (misal: MainActivity)
                    // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // startActivity(intent);
                    // finish();
                    // Jika berhasil, pindah ke halaman utama (MainActivity)
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    startActivity(intent);
                    finish(); // Tutup LoginActivity agar tidak bisa kembali

                }
            }
        });

        // 2. Atur listener untuk tombol "Daftar"
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke halaman RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 3. Atur listener untuk "Lupa Kata Sandi" (opsional)
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logika untuk halaman lupa kata sandi
                Toast.makeText(LoginActivity.this, "Fitur Lupa Kata Sandi diklik", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
