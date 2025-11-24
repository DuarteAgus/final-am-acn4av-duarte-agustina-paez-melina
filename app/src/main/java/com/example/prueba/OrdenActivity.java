package com.example.prueba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrdenActivity extends AppCompatActivity {

    private TextView tvTituloOrden;
    private TextView tvResumenPlan;
    private TextView tvPrecioPlan;
    private RadioGroup rgMetodoPago;
    private EditText etAlias;
    private EditText etCupon;
    private Button btnConfirmarOrden;

    private String planId;
    private String planNombre;
    private String planPrecio;
    private String planDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden);

        tvTituloOrden   = findViewById(R.id.tvTituloOrden);
        tvResumenPlan   = findViewById(R.id.tvResumenPlan);
        tvPrecioPlan    = findViewById(R.id.tvPrecioPlan);
        rgMetodoPago    = findViewById(R.id.rgMetodoPago);
        etAlias         = findViewById(R.id.etAlias);
        etCupon         = findViewById(R.id.etCupon);
        btnConfirmarOrden = findViewById(R.id.btnConfirmarOrden);

        planId = getIntent().getStringExtra(OfertasActivity.EXTRA_PLAN_ID);
        configurarPlan(planId);

        btnConfirmarOrden.setOnClickListener(v -> confirmarOrden());
    }

    private void configurarPlan(String id) {
        if ("nebula".equals(id)) {
            planNombre = "Plan Nebula";
            planPrecio = "10.000 ARS / mes";
            planDescripcion =
                    "Ideal para jugadores casuales: 60 fps estables en 1080p.\n" +
                            "Incluye catálogo base y soporte estándar.";
        } else if ("quantum".equals(id)) {
            planNombre = "Plan Quantum";
            planPrecio = "15.000 ARS / mes";
            planDescripcion =
                    "Pensado para gamers exigentes: 120 fps en 1080p / 60 fps en 1440p.\n" +
                            "Catálogo completo y soporte prioritario.";
        } else if ("eclipse".equals(id)) {
            planNombre = "Plan Eclipse";
            planPrecio = "20.000 ARS / mes";
            planDescripcion =
                    "Lo máximo para streamers: 4K HDR, servidores premium.\n" +
                            "Catálogo + DLCs selectos y soporte dedicado 24/7.";
        } else {
            planNombre = "Plan desconocido";
            planPrecio = "0 ARS";
            planDescripcion = "No se pudo identificar el plan.";
        }

        tvTituloOrden.setText("NuCloud Gaming - " + planNombre);
        tvResumenPlan.setText(planDescripcion);
        tvPrecioPlan.setText("Precio: " + planPrecio);
    }

    private void confirmarOrden() {
        int checkedId = rgMetodoPago.getCheckedRadioButtonId();
        if (checkedId == -1) {
            Toast.makeText(this, "Seleccioná un método de pago", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton rbSeleccionado = findViewById(checkedId);
        String metodoPago = rbSeleccionado.getText().toString();

        String alias = etAlias.getText().toString().trim();
        if (TextUtils.isEmpty(alias)) {
            etAlias.setError("Ingresá un alias o número de tarjeta");
            return;
        }

        String cupon = etCupon.getText().toString().trim();

        SharedPreferences prefs = getSharedPreferences("nucloud_prefs", MODE_PRIVATE);
        prefs.edit()
                .putString("plan_id", planId)
                .putString("plan_nombre", planNombre)
                .putString("plan_precio", planPrecio)
                .putString("plan_metodo", metodoPago)
                .putString("plan_alias", alias)
                .putString("plan_cupon", cupon)
                .apply();

        Toast.makeText(this, "Plan contratado correctamente", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, DashboardClienteActivity.class);
        startActivity(intent);
        finish();
    }
}
