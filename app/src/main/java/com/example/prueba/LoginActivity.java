package com.example.prueba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    public static final String PREFS_USER   = "nucloud_user";
    public static final String KEY_IS_LOGGED = "is_logged_in";
    public static final String KEY_EMAIL     = "user_email";

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    private String planIdFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail    = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin   = findViewById(R.id.btnLogin);

        planIdFromIntent = getIntent().getStringExtra(OfertasActivity.EXTRA_PLAN_ID);

        btnLogin.setOnClickListener(v -> intentarLogin());
    }

    private void intentarLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Ingresá tu email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Ingresá tu contraseña");
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_USER, MODE_PRIVATE);
        prefs.edit()
                .putBoolean(KEY_IS_LOGGED, true)
                .putString(KEY_EMAIL, email)
                .apply();

        Toast.makeText(this, "Sesión iniciada", Toast.LENGTH_SHORT).show();

        if (planIdFromIntent != null) {
            Intent intent = new Intent(this, OrdenActivity.class);
            intent.putExtra(OfertasActivity.EXTRA_PLAN_ID, planIdFromIntent);
            startActivity(intent);
        }

        finish();
    }
}
