package com.example.prueba;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class ClientesActivity extends AppCompatActivity {

    private TextView tvTituloClientes;
    private TextView tvSubtituloClientes;

    private LinearLayout llListaClientes;

    private EditText etNombreCliente;
    private EditText etEmailCliente;
    private Spinner spPlanCliente;
    private Button btnAgregarCliente;
    private Button btnEliminarUltimoCliente;

    private FirebaseFirestore db;

    private DocumentSnapshot ultimoDocMostrado = null;

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

        db = FirebaseFirestore.getInstance();

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

        cargarClientesFirestore();

        btnAgregarCliente.setOnClickListener(v -> agregarClienteFirestore());

        btnEliminarUltimoCliente.setOnClickListener(v -> eliminarUltimoClienteFirestore());
    }

    private void cargarClientesFirestore() {
        llListaClientes.removeAllViews();
        ultimoDocMostrado = null;

        db.collection("clientes")
                .orderBy("creadoEn", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(query -> {
                    if (query.isEmpty()) {
                        Toast.makeText(this, "No hay clientes en Firestore todavía", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (DocumentSnapshot doc : query.getDocuments()) {
                        String nombre = doc.getString("nombre");
                        String email  = doc.getString("email");
                        String plan   = doc.getString("plan");

                        if (nombre == null) nombre = "(sin nombre)";
                        if (email == null) email = "(sin email)";
                        if (plan == null) plan = "Sin plan";

                        agregarClienteEnLista(nombre, email, plan);

                        ultimoDocMostrado = doc;
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al cargar clientes: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void agregarClienteFirestore() {
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

        Map<String, Object> data = new HashMap<>();
        data.put("nombre", nombre);
        data.put("email", email);
        data.put("plan", plan);
        data.put("creadoEn", System.currentTimeMillis());

        db.collection("clientes")
                .add(data)
                .addOnSuccessListener(ref -> {
                    Toast.makeText(this, "Cliente agregado (Firestore)", Toast.LENGTH_SHORT).show();

                    etNombreCliente.setText("");
                    etEmailCliente.setText("");
                    spPlanCliente.setSelection(0);

                    cargarClientesFirestore();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al agregar: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void eliminarUltimoClienteFirestore() {
        if (ultimoDocMostrado == null) {
            Toast.makeText(this, "No hay clientes para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("clientes")
                .document(ultimoDocMostrado.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Último cliente eliminado (Firestore)", Toast.LENGTH_SHORT).show();
                    cargarClientesFirestore();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
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
