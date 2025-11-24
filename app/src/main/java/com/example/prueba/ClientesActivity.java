package com.example.prueba;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ClientesActivity extends AppCompatActivity {

    private TextView tvTituloClientes;
    private TextView tvSubtituloClientes;

    private LinearLayout llListaClientes;

    private EditText etNombreCliente;
    private EditText etEmailCliente;
    private Spinner spPlanCliente;
    private Button btnAgregarCliente;
    private Button btnEliminarUltimoCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        tvTituloClientes    = findViewById(R.id.tvTituloClientes);
        tvSubtituloClientes = findViewById(R.id.tvSubtituloClientes);

        llListaClientes     = findViewById(R.id.llListaClientes);

        etNombreCliente     = findViewById(R.id.etNombreCliente);
        etEmailCliente      = findViewById(R.id.etEmailCliente);
        spPlanCliente       = findViewById(R.id.spPlanCliente);
        btnAgregarCliente   = findViewById(R.id.btnAgregarCliente);
        btnEliminarUltimoCliente = findViewById(R.id.btnEliminarUltimoCliente);

        tvTituloClientes.setText("Clientes y suscripciones");
        tvSubtituloClientes.setText(
                "Consultá el estado básico de los clientes y agregá nuevos desde la app. " +
                        "La gestión avanzada (vencimientos, deuda, filtros) se realiza en la versión web."
        );

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.planes_cliente_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPlanCliente.setAdapter(adapter);


        agregarClienteEnLista("Doris Duarte", "doris.duarte@gmail.com", "Plan Eclipse");
        agregarClienteEnLista("Camila López", "camila.lopez@gmail.com", "Plan Quantum");
        agregarClienteEnLista("María Gómez", "maria.gomez@gmail.com", "Plan Nebula");
        agregarClienteEnLista("Invitado sin plan", "invitado@gmail.com", "Sin plan");

        btnAgregarCliente.setOnClickListener(v -> agregarCliente());

        btnEliminarUltimoCliente.setOnClickListener(v -> eliminarUltimoCliente());
    }

    private void agregarCliente() {
        String nombre = etNombreCliente.getText().toString().trim();
        String email  = etEmailCliente.getText().toString().trim();
        String plan   = spPlanCliente.getSelectedItem().toString();

        if (TextUtils.isEmpty(nombre)) {
            etNombreCliente.setError("Ingresá un nombre");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmailCliente.setError("Ingresá un email");
            return;
        }

        agregarClienteEnLista(nombre, email, plan);

        etNombreCliente.setText("");
        etEmailCliente.setText("");
        spPlanCliente.setSelection(0);

        Toast.makeText(this, "Cliente agregado", Toast.LENGTH_SHORT).show();
    }

    private void eliminarUltimoCliente() {
        int count = llListaClientes.getChildCount();
        if (count > 0) {
            llListaClientes.removeViewAt(count - 1);
            Toast.makeText(this, "Último cliente eliminado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No hay clientes para eliminar", Toast.LENGTH_SHORT).show();
        }
    }

    private void agregarClienteEnLista(String nombre, String email, String plan) {
        TextView tv = new TextView(this);
        tv.setText("• " + nombre + "  |  " + email + "  |  " + plan);
        tv.setTextSize(14f);
        tv.setTextColor(0xFFFFFFFF);
        tv.setPadding(0, 6, 0, 6);

        llListaClientes.addView(tv);
    }
}
