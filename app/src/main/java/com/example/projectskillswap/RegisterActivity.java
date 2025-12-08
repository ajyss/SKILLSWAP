package com.example.projectskillswap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends BaseActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hubungkan kelas Java ini dengan file layout XML-nya
        setContentView(R.layout.activity_register);

        // Inisialisasi semua view dari layout
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSignUp = findViewById(R.id.btn_signup);
        tvGoToLogin = findViewById(R.id.tv_go_to_login);

        // 1. Atur listener untuk tombol "Sign Up"
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Di sini Anda akan menambahkan logika validasi pendaftaran
                String fullName = etFullName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                // Contoh validasi sederhana
                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Kata sandi tidak cocok", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Jika pendaftaran berhasil (simulasi)
                Toast.makeText(RegisterActivity.this, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show();

                // Arahkan pengguna kembali ke halaman Login agar mereka bisa masuk
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Tutup RegisterActivity
            }
        });

        // 2. Atur listener untuk teks "Already have an account? Log In"
        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke halaman Login
                finish(); // Cukup tutup activity ini, karena LoginActivity sudah ada di back stack
            }
        });
    }
}
