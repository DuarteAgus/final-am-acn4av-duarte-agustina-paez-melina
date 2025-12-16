package com.example.prueba;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView tvAdminWelcome;
    private TextView tvAdminHighlight;
    private TextView tvAdminHint;

    private EditText etPlanNombre;
    private EditText etPlanPrecio;
    private Button btnCambiarDestacado;
    private Button btnAgregarPlan;
    private LinearLayout llListaPlanes;

    private EditText etUsuarioNombre;
    private Button btnAgregarUsuario;
    private Button btnEliminarUltimoUsuario;
    private LinearLayout llListaUsuarios;

    private Button btnAdminLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Vincular vistas
        tvAdminWelcome   = findViewById(R.id.tvAdminWelcome);
        tvAdminHighlight = findViewById(R.id.tvAdminHighlight);
        tvAdminHint      = findViewById(R.id.tvAdminHint);

        etPlanNombre        = findViewById(R.id.etPlanNombre);
        etPlanPrecio        = findViewById(R.id.etPlanPrecio);
        btnCambiarDestacado = findViewById(R.id.btnCambiarDestacado);
        btnAgregarPlan      = findViewById(R.id.btnAgregarPlan);
        llListaPlanes       = findViewById(R.id.llListaPlanes);

        etUsuarioNombre        = findViewById(R.id.etUsuarioNombre);
        btnAgregarUsuario      = findViewById(R.id.btnAgregarUsuario);
        btnEliminarUltimoUsuario = findViewById(R.id.btnEliminarUltimoUsuario);
        llListaUsuarios        = findViewById(R.id.llListaUsuarios);

        btnAdminLogout = findViewById(R.id.btnAdminLogout);

        tvAdminWelcome.setText("Panel de administración NuCloud");
        tvAdminHighlight.setText("Plan destacado hoy: Quantum -20% OFF");

        agregarPlanEnLista("Plan Nebula - $5.999 / mes");
        agregarPlanEnLista("Plan Quantum - $8.999 / mes");
        agregarPlanEnLista("Plan Eclipse - $11.999 / mes");

        agregarUsuarioEnLista("agustina@nucloud.com");
        agregarUsuarioEnLista("cliente.demo@nucloud.com");

        btnCambiarDestacado.setOnClickListener(v -> {
            tvAdminHighlight.setText("Plan destacado hoy: Eclipse - Promo 3 meses -20%");
            Toast.makeText(this, "Plan destacado actualizado", Toast.LENGTH_SHORT).show();
        });

        btnAgregarPlan.setOnClickListener(v -> {
            String nombre = etPlanNombre.getText().toString().trim();
            String precio = etPlanPrecio.getText().toString().trim();

            if (TextUtils.isEmpty(nombre)) {
                etPlanNombre.setError("Ingresá el nombre del plan");
                return;
            }
            if (TextUtils.isEmpty(precio)) {
                etPlanPrecio.setError("Ingresá el precio del plan");
                return;
            }

            String texto = nombre + " - " + precio;
            agregarPlanEnLista(texto);
            etPlanNombre.setText("");
            etPlanPrecio.setText("");

            Toast.makeText(this, "Plan agregado", Toast.LENGTH_SHORT).show();
        });

        btnAgregarUsuario.setOnClickListener(v -> {
            String usuario = etUsuarioNombre.getText().toString().trim();
            if (TextUtils.isEmpty(usuario)) {
                etUsuarioNombre.setError("Ingresá un usuario");
                return;
            }

            agregarUsuarioEnLista(usuario);
            etUsuarioNombre.setText("");

            Toast.makeText(this, "Usuario agregado", Toast.LENGTH_SHORT).show();
        });

        btnEliminarUltimoUsuario.setOnClickListener(v -> {
            int count = llListaUsuarios.getChildCount();
            if (count > 0) {
                llListaUsuarios.removeViewAt(count - 1);
                Toast.makeText(this, "Se eliminó el último usuario", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No hay usuarios para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        btnAdminLogout.setOnClickListener(v -> cerrarSesionAdmin());
    }

    private void agregarPlanEnLista(String texto) {
        TextView tv = new TextView(this);
        tv.setText("• " + texto);
        tv.setTextSize(14f);
        tv.setTextColor(Color.WHITE);
        tv.setPadding(0, 4, 0, 4);
        llListaPlanes.addView(tv);
    }

    private void agregarUsuarioEnLista(String texto) {
        TextView tv = new TextView(this);
        tv.setText("• " + texto);
        tv.setTextSize(14f);
        tv.setTextColor(Color.WHITE);
        tv.setPadding(0, 4, 0, 4);
        llListaUsuarios.addView(tv);
    }

    private void cerrarSesionAdmin() {
        Toast.makeText(this, "Sesión de administrador cerrada", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
