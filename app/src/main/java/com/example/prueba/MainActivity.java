package com.example.prueba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    @Nullable private TextView tvBrand;
    @Nullable private TextView tvHeroTitle;
    @Nullable private TextView tvHeroBody;
    @Nullable private TextView tvCompatibility;

    @Nullable private Button btnSeeOffers;
    @Nullable private ImageButton btnMenu;

    @Nullable private LinearLayout llDynamicBanner;

    @Nullable private ScrollView svContent;
    @Nullable private View gridGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBrand         = findViewById(R.id.tvBrand);
        tvHeroTitle     = findViewById(R.id.tvHeroTitle);
        tvHeroBody      = findViewById(R.id.tvHeroBody);
        tvCompatibility = findViewById(R.id.tvCompatibility);

        btnSeeOffers    = findViewById(R.id.btnSeeOffers);
        btnMenu         = findViewById(R.id.btnMenu);

        llDynamicBanner = findViewById(R.id.llDynamicBanner);

        svContent = findViewById(R.id.svContent);
        gridGames = findViewById(R.id.gridGames);

        if (btnSeeOffers != null) {
            btnSeeOffers.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, OfertasActivity.class);
                startActivity(intent);
            });
        }

        if (btnMenu != null) {
            btnMenu.setOnClickListener(this::mostrarMenu);
        }

        addDynamicBanner();
    }

    private void mostrarMenu(View anchor) {
        PopupMenu popup = new PopupMenu(MainActivity.this, anchor);
        popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_inicio) {
                Toast.makeText(MainActivity.this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show();
                return true;

            } else if (id == R.id.action_ofertas) {
                startActivity(new Intent(MainActivity.this, OfertasActivity.class));
                return true;

            } else if (id == R.id.action_dashboard_cliente) {

                SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_USER, MODE_PRIVATE);
                boolean logged = prefs.getBoolean(LoginActivity.KEY_IS_LOGGED, false);

                if (logged) {
                    startActivity(new Intent(MainActivity.this, DashboardClienteActivity.class));
                } else {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);

                    startActivity(i);
                }
                return true;

            } else if (id == R.id.action_login_admin) {
                startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
                return true;
            }

            return false;
        });

        popup.show();
    }

    private void addDynamicBanner() {
        if (llDynamicBanner == null) return;

        TextView banner = new TextView(this);
        banner.setText(" Promo del día: 3 meses -20%");
        banner.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        banner.setTextSize(16f);

        int pad = getResources().getDimensionPixelSize(R.dimen.space_sm);
        banner.setPadding(pad, pad, pad, pad);
        banner.setBackgroundColor(ContextCompat.getColor(this, R.color.card_bg));

        llDynamicBanner.addView(banner);
    }

    private void scrollToGrid() {
        if (svContent == null || gridGames == null) return;

        svContent.post(() -> {
            int y = gridGames.getTop() - dp(12);
            if (y < 0) y = 0;
            svContent.smoothScrollTo(0, y);
        });
    }

    private int dp(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
