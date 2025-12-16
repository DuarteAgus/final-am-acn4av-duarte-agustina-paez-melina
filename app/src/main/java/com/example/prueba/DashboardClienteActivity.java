package com.example.prueba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardClienteActivity extends AppCompatActivity {

    private TextView tvUsuario;
    private TextView tvEstadoPlan;
    private TextView tvDetallePlan;

    private EditText etDispositivo;
    private Button btnAgregarDispositivo;
    private Button btnCatalogoOnline;
    private Button btnLogout;
    private LinearLayout llDispositivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_cliente);

        tvUsuario         = findViewById(R.id.tvUsuario);
        tvEstadoPlan      = findViewById(R.id.tvEstadoPlan);
        tvDetallePlan     = findViewById(R.id.tvDetallePlan);
        etDispositivo     = findViewById(R.id.etDispositivo);
        btnAgregarDispositivo = findViewById(R.id.btnAgregarDispositivo);
        btnCatalogoOnline = findViewById(R.id.btnCatalogoOnline);
        btnLogout         = findViewById(R.id.btnLogout);
        llDispositivos    = findViewById(R.id.llDispositivos);

        // ✅ Firebase manda: si no hay sesión, no debería entrar al dashboard
        if (!haySesionFirebase()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
            return;
        }

        cargarUsuario();

        cargarPlanDesdePrefs();

        btnAgregarDispositivo.setOnClickListener(v -> agregarDispositivo());
        btnCatalogoOnline.setOnClickListener(v -> abrirCatalogoOnline());
        btnLogout.setOnClickListener(v -> cerrarSesion());
    }

    private boolean haySesionFirebase() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private void cargarUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && user.getEmail() != null) {
            tvUsuario.setText("Usuario: " + user.getEmail());
        } else {
            tvUsuario.setText("Usuario: invitado");
        }
    }

    private void cargarPlanDesdePrefs() {
        SharedPreferences prefs = getSharedPreferences("nucloud_prefs", MODE_PRIVATE);

        String nombre = prefs.getString("plan_nombre", null);
        String precio = prefs.getString("plan_precio", null);
        String metodo = prefs.getString("plan_metodo", null);
        String alias  = prefs.getString("plan_alias", null);

        if (nombre == null) {
            tvEstadoPlan.setText("No tenés un plan contratado");
            tvDetallePlan.setText("Contratá un plan desde la sección Ofertas para ver el estado acá.");
        } else {
            tvEstadoPlan.setText("Plan contratado: " + nombre);

            String detalle = "Precio: " + precio;
            if (metodo != null) {
                detalle += "\nMétodo de pago: " + metodo;
            }
            if (alias != null) {
                detalle += "\nAlias / tarjeta: " + alias;
            }

            tvDetallePlan.setText(detalle);
        }
    }

    private void agregarDispositivo() {
        String nombreDispositivo = etDispositivo.getText().toString().trim();
        if (TextUtils.isEmpty(nombreDispositivo)) {
            etDispositivo.setError("Ingresá un dispositivo (ej: TV del living)");
            return;
        }

        TextView tv = new TextView(this);
        tv.setText("• " + nombreDispositivo);
        tv.setTextSize(14f);
        tv.setTextColor(0xFFFFFFFF);
        tv.setPadding(0, 4, 0, 4);

        llDispositivos.addView(tv);
        etDispositivo.setText("");
    }

    private void abrirCatalogoOnline() {
        if (!hayConexion()) {
            Toast.makeText(this, "Sin conexión a Internet", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = Uri.parse("https://rawg.io/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private boolean hayConexion() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        Network network = cm.getActiveNetwork();
        if (network == null) return false;

        NetworkCapabilities caps = cm.getNetworkCapabilities(network);
        if (caps == null) return false;

        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
    }

    private void cerrarSesion() {
        // ✅ Logout real Firebase
        FirebaseAuth.getInstance().signOut();

        // (Opcional) limpiar tu prefs vieja para que no quede "logueado" fake
        SharedPreferences prefsUser = getSharedPreferences(LoginActivity.PREFS_USER, MODE_PRIVATE);
        prefsUser.edit().clear().apply();

        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
