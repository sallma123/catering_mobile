package com.example.test1.ui.commandes;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test1.R;

public class CreerCommandeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_commande);

        String typeClient = getIntent().getStringExtra("typeClient");

        TextView tvTitre = findViewById(R.id.tvTitreTypeClient);
        tvTitre.setText("Création : " + typeClient);
    }
}
