package com.example.test1.ui.commandes;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test1.R;

import java.util.Calendar;

public class CreerCommandeActivity extends AppCompatActivity {

    private TextView tvTitreTypeClient, tvLabelNombre;
    private Spinner spinnerTypeCommande, spinnerStatut;
    private EditText etNomClient, etSalle, etNombre, etDate;
    private Button btnSuivant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_commande);

        // Récupérer le type client
        String typeClient = getIntent().getStringExtra("typeClient");

        // Liaison des vues
        tvTitreTypeClient = findViewById(R.id.tvTitreTypeClient);
        tvLabelNombre = findViewById(R.id.tvLabelNombre);
        spinnerTypeCommande = findViewById(R.id.spinnerTypeCommande);
        spinnerStatut = findViewById(R.id.spinnerStatut);
        etNomClient = findViewById(R.id.etNomClient);
        etSalle = findViewById(R.id.etSalle);
        etNombre = findViewById(R.id.etNombre);
        etDate = findViewById(R.id.etDate);
        btnSuivant = findViewById(R.id.btnSuivant);

        // Titre dynamique
        tvTitreTypeClient.setText("Création commande : " + typeClient);

        // Adapter le label (tables ou personnes)
        if (typeClient.equalsIgnoreCase("Particulier") || typeClient.equalsIgnoreCase("Partenaire")) {
            tvLabelNombre.setText("Nombre de tables");
        } else {
            tvLabelNombre.setText("Nombre de personnes");
        }

        // Remplir le spinner des types de commande
        String[] typesParticulier = {"Mariage", "Anniversaire", "Baptême"};
        String[] typesPro = {"Buffet de soutenance", "Repas coffret", "Séminaire"};

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                typeClient.equals("Entreprise") ? typesPro : typesParticulier
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeCommande.setAdapter(typeAdapter);

        // Remplir le spinner des statuts
        String[] statuts = {"Payé", "Non payé"};
        ArrayAdapter<String> statutAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuts);
        statutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatut.setAdapter(statutAdapter);

        // Choix de date
        etDate.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CreerCommandeActivity.this,
                    (view1, year, month, dayOfMonth) -> {
                        String dateStr = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        etDate.setText(dateStr);
                    },
                    y, m, d
            );
            datePickerDialog.show();
        });

        btnSuivant.setOnClickListener(v -> {
            Intent intent = new Intent(CreerCommandeActivity.this, SelectionProduitsActivity.class);
            intent.putExtra("typeCommande", spinnerTypeCommande.getSelectedItem().toString());
            intent.putExtra("nombre", Integer.parseInt(etNombre.getText().toString().trim()));
            startActivity(intent);
        });


    }
}


