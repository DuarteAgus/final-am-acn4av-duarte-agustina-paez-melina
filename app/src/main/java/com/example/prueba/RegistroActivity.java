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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre;
    private EditText etEmailRegistro;
    private EditText etPasswordRegistro;
    private EditText etPasswordConfirm;
    private Button btnCrearCuenta;

    private String planIdFromIntent;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final String ADMIN_USER = "admin@nucloud.com";

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
        db = FirebaseFirestore.getInstance();

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

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        String msg = (task.getException() != null) ? task.getException().getMessage() : "Error al crear cuenta";
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user == null) {
                        Toast.makeText(this, "Error: usuario no disponible", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String uid = user.getUid();
                    String rol = email.equalsIgnoreCase(ADMIN_USER) ? "admin" : "cliente";

                    Map<String, Object> data = new HashMap<>();
                    data.put("nombre", nombre);
                    data.put("email", email);
                    data.put("rol", rol);
                    data.put("creadoEn", System.currentTimeMillis());

                    db.collection("usuarios").document(uid).set(data)
                            .addOnSuccessListener(aVoid -> {

                                SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_USER, MODE_PRIVATE);
                                prefs.edit()
                                        .putBoolean(LoginActivity.KEY_IS_LOGGED, true)
                                        .putString(LoginActivity.KEY_EMAIL, email)
                                        .apply();

                                Toast.makeText(this, "Cuenta creada (Firestore OK)", Toast.LENGTH_SHORT).show();

                                if (planIdFromIntent != null) {
                                    Intent i = new Intent(this, OrdenActivity.class);
                                    i.putExtra(OfertasActivity.EXTRA_PLAN_ID, planIdFromIntent);
                                    startActivity(i);
                                } else {
                                    startActivity(new Intent(this, DashboardClienteActivity.class));
                                }

                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Cuenta creada, pero falló Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(this, DashboardClienteActivity.class));
                                finish();
                            });
                });
    }
}
