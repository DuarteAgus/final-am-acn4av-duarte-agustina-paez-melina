package com.example.prueba;

import android.content.Intent;
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
    private Button btnCambiarDestacado;
    private Button btnIrClientes;

    private EditText etPlanNombre;
    private EditText etPlanPrecio;
    private Button btnAgregarPlan;
    private Button btnEliminarUltimoPlan;
    private LinearLayout llListaPlanes;

    private EditText etUsuarioNombre;
    private Button btnAgregarUsuario;
    private Button btnEliminarUltimoUsuario;
    private LinearLayout llListaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        tvAdminWelcome   = findViewById(R.id.tvAdminWelcome);
        tvAdminHighlight = findViewById(R.id.tvAdminHighlight);
        tvAdminHint      = findViewById(R.id.tvAdminHint);
        btnCambiarDestacado = findViewById(R.id.btnCambiarDestacado);
        btnIrClientes       = findViewById(R.id.btnIrClientes);

        etPlanNombre        = findViewById(R.id.etPlanNombre);
        etPlanPrecio        = findViewById(R.id.etPlanPrecio);
        btnAgregarPlan      = findViewById(R.id.btnAgregarPlan);
        btnEliminarUltimoPlan = findViewById(R.id.btnEliminarUltimoPlan);
        llListaPlanes       = findViewById(R.id.llListaPlanes);

        etUsuarioNombre         = findViewById(R.id.etUsuarioNombre);
        btnAgregarUsuario       = findViewById(R.id.btnAgregarUsuario);
        btnEliminarUltimoUsuario = findViewById(R.id.btnEliminarUltimoUsuario);
        llListaUsuarios         = findViewById(R.id.llListaUsuarios);

        tvAdminWelcome.setText("Panel de administración NuCloud");
        tvAdminHighlight.setText("Plan destacado hoy: Nebula");
        tvAdminHint.setText("(Desde este panel el administrador gestiona planes y clientes. El catálogo de juegos se administra en la versión web.)");

        agregarPlanEnLista("Plan Nebula", "$5.999 / mes");
        agregarPlanEnLista("Plan Quantum", "$8.999 / mes");
        agregarPlanEnLista("Plan Eclipse", "$11.999 / mes");

        agregarUsuarioEnLista("agustina@nucloud.com");
        agregarUsuarioEnLista("cliente.demo@nucloud.com");

        btnCambiarDestacado.setOnClickListener(v -> {
            tvAdminHighlight.setText("Plan destacado hoy: Quantum -20% OFF");
            Toast.makeText(this, "Plan destacado actualizado", Toast.LENGTH_SHORT).show();
        });

        btnIrClientes.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ClientesActivity.class);
            startActivity(intent);
        });

        btnAgregarPlan.setOnClickListener(v -> {
            String nombre = etPlanNombre.getText().toString().trim();
            String precio = etPlanPrecio.getText().toString().trim();

            if (TextUtils.isEmpty(nombre)) {
                etPlanNombre.setError("Ingresá un nombre de plan");
                return;
            }
            if (TextUtils.isEmpty(precio)) {
                etPlanPrecio.setError("Ingresá un precio");
                return;
            }

            agregarPlanEnLista(nombre, precio);

            etPlanNombre.setText("");
            etPlanPrecio.setText("");
            Toast.makeText(this, "Plan agregado", Toast.LENGTH_SHORT).show();
        });

        btnEliminarUltimoPlan.setOnClickListener(v -> {
            int count = llListaPlanes.getChildCount();
            if (count > 0) {
                llListaPlanes.removeViewAt(count - 1);
                Toast.makeText(this, "Último plan eliminado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No hay planes para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        btnAgregarUsuario.setOnClickListener(v -> {
            String usuario = etUsuarioNombre.getText().toString().trim();
            if (TextUtils.isEmpty(usuario)) {
                etUsuarioNombre.setError("Ingresá un usuario (email o nombre)");
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
                Toast.makeText(this, "Último usuario eliminado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No hay usuarios para eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarPlanEnLista(String nombre, String precio) {
        TextView tv = new TextView(this);
        tv.setText("• " + nombre + " - " + precio);
        tv.setTextSize(14f);
        tv.setTextColor(0xFFFFFFFF);
        tv.setPadding(0, 4, 0, 4);

        llListaPlanes.addView(tv);
    }

    private void agregarUsuarioEnLista(String usuario) {
        TextView tv = new TextView(this);
        tv.setText("• " + usuario);
        tv.setTextSize(14f);
        tv.setTextColor(0xFFFFFFFF);
        tv.setPadding(0, 4, 0, 4);

        llListaUsuarios.addView(tv);
    }
}
