package com.example.prueba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre;
    private EditText etEmailRegistro;
    private EditText etPasswordRegistro;
    private EditText etPasswordConfirm;
    private Button btnCrearCuenta;

    private String planIdFromIntent;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre           = findViewById(R.id.etNombre);
        etEmailRegistro    = findViewById(R.id.etEmailRegistro);
        etPasswordRegistro = findViewById(R.id.etPasswordRegistro);
        etPasswordConfirm  = findViewById(R.id.etPasswordConfirm);
        btnCrearCuenta     = findViewById(R.id.btnCrearCuenta);

        planIdFromIntent = getIntent().getStringExtra(OfertasActivity.EXTRA_PLAN_ID);

        mAuth = FirebaseAuth.getInstance();

        btnCrearCuenta.setOnClickListener(v -> registrar());
    }

    private void registrar() {
        String nombre = etNombre.getText().toString().trim();
        String email  = etEmailRegistro.getText().toString().trim();
        String pass   = etPasswordRegistro.getText().toString().trim();
        String pass2  = etPasswordConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)) {
            etNombre.setError("Ingresá tu nombre");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmailRegistro.setError("Ingresá tu email");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            etPasswordRegistro.setError("Ingresá una contraseña");
            return;
        }
        if (!pass.equals(pass2)) {
            etPasswordConfirm.setError("Las contraseñas no coinciden");
            return;
        }

        // Crear usuario en Firebase
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        // (Opcional) mantener tu SharedPreferences para el flujo actual
                        SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_USER, MODE_PRIVATE);
                        prefs.edit()
                                .putBoolean(LoginActivity.KEY_IS_LOGGED, true)
                                .putString(LoginActivity.KEY_EMAIL, email)
                                .apply();

                        Toast.makeText(this, "Cuenta creada y sesión iniciada", Toast.LENGTH_SHORT).show();

                        if (planIdFromIntent != null) {
                            Intent i = new Intent(this, OrdenActivity.class);
                            i.putExtra(OfertasActivity.EXTRA_PLAN_ID, planIdFromIntent);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(this, DashboardClienteActivity.class);
                            startActivity(i);
                        }

                        finish();

                    } else {
                        String msg = (task.getException() != null) ? task.getException().getMessage() : "Error al crear cuenta";
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
