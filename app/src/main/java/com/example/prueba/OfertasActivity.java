package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class OfertasActivity extends AppCompatActivity {

    public static final String EXTRA_PLAN_ID = "PLAN_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas);

        Button btnPlanNebula  = findViewById(R.id.btnPlanNebula);
        Button btnPlanQuantum = findViewById(R.id.btnPlanQuantum);
        Button btnPlanEclipse = findViewById(R.id.btnPlanEclipse);

        if (btnPlanNebula != null) {
            btnPlanNebula.setOnClickListener(v -> openOrderWithLoginCheck("nebula"));
        }

        if (btnPlanQuantum != null) {
            btnPlanQuantum.setOnClickListener(v -> openOrderWithLoginCheck("quantum"));
        }

        if (btnPlanEclipse != null) {
            btnPlanEclipse.setOnClickListener(v -> openOrderWithLoginCheck("eclipse"));
        }
    }

    private void openOrderWithLoginCheck(String planId) {
        boolean logged = FirebaseAuth.getInstance().getCurrentUser() != null;

        if (logged) {
            Intent intent = new Intent(this, OrdenActivity.class);
            intent.putExtra(EXTRA_PLAN_ID, planId);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(EXTRA_PLAN_ID, planId); // para saber qué plan quería
            startActivity(intent);
        }
    }
}
