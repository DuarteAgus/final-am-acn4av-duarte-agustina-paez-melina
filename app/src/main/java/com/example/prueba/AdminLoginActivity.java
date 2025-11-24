package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private static final String ADMIN_USER = "admin@nucloud.com";
    private static final String ADMIN_PASS = "admin123";

    private EditText etAdminUser;
    private EditText etAdminPass;
    private Button btnAdminLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        etAdminUser  = findViewById(R.id.etAdminUser);
        etAdminPass  = findViewById(R.id.etAdminPass);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);

        btnAdminLogin.setOnClickListener(v -> intentarLoginAdmin());
    }

    private void intentarLoginAdmin() {
        String user = etAdminUser.getText().toString().trim();
        String pass = etAdminPass.getText().toString().trim();

        if (TextUtils.isEmpty(user)) {
            etAdminUser.setError("Ingresá el usuario admin");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            etAdminPass.setError("Ingresá la contraseña");
            return;
        }

        if (ADMIN_USER.equals(user) && ADMIN_PASS.equals(pass)) {
            Toast.makeText(this, "Bienvenido administrador", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AdminDashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Credenciales de administrador incorrectas", Toast.LENGTH_SHORT).show();
        }
    }
}
