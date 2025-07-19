package com.example.test1.ui.commandes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.api.ApiService;
import com.example.test1.api.RetrofitClient;
import com.example.test1.model.Commande;
import com.example.test1.model.CommandeDTO;
import com.example.test1.model.ProduitCommande;
import com.example.test1.model.SectionProduit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectionProduitsActivity extends AppCompatActivity {

    private TextView tvTitreProduits, tvTotal;
    private EditText etPrixTable;
    private Button btnValider;
    private RecyclerView rvProduits;
    private SectionProduitAdapter adapter;

    private List<SectionProduit> sections = new ArrayList<>();
    private double prixParTable = 0;
    private int nombreTables = 5;

    private String typeCommande, nomClient, salle, date, statut, typeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_produits);

        // Initialisation des vues
        tvTitreProduits = findViewById(R.id.tvTitreProduits);
        tvTotal = findViewById(R.id.tvTotal);
        etPrixTable = findViewById(R.id.etPrixTable);
        btnValider = findViewById(R.id.btnValider);
        rvProduits = findViewById(R.id.rvProduits);

        // Récupération des extras de l'écran précédent
        typeCommande = getIntent().getStringExtra("typeCommande");
        nomClient = getIntent().getStringExtra("nomClient");
        salle = getIntent().getStringExtra("salle");
        date = getIntent().getStringExtra("date");
        statut = getIntent().getStringExtra("statut");
        typeClient = getIntent().getStringExtra("typeClient");
        nombreTables = getIntent().getIntExtra("nombre", 1);

        tvTitreProduits.setText("Produits pour " + typeCommande);

        // Création des sections
        List<ProduitCommande> reception = Arrays.asList(
                new ProduitCommande("Dattes et lait", "Réception", 0),
                new ProduitCommande("Amuses bouche", "Réception", 0),
                new ProduitCommande("Petits fours salés", "Réception", 0)
        );
        List<ProduitCommande> dessert = Arrays.asList(
                new ProduitCommande("Gâteaux prestige", "Dessert", 0)
        );

        sections.add(new SectionProduit("Réception", new ArrayList<>(reception)));
        sections.add(new SectionProduit("Dessert", new ArrayList<>(dessert)));
        sections.add(new SectionProduit("Supplément", new ArrayList<>()));

        // Setup RecyclerView
        adapter = new SectionProduitAdapter(sections, this::recalculerTotal);
        rvProduits.setLayoutManager(new LinearLayoutManager(this));
        rvProduits.setAdapter(adapter);

        // Recalcul du total
        etPrixTable.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                recalculerTotal();
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });

        etPrixTable.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) recalculerTotal();
        });

        btnValider.setOnClickListener(v -> {
            String prixText = etPrixTable.getText().toString().trim();
            if (prixText.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer le prix par table", Toast.LENGTH_SHORT).show();
                return;
            }

            prixParTable = Double.parseDouble(prixText);

            // Récupération des produits sélectionnés
            List<ProduitCommande> produitsSelectionnes = new ArrayList<>();
            for (SectionProduit section : sections) {
                for (ProduitCommande produit : section.getProduits()) {
                    if (produit.isSelectionne()) {
                        produitsSelectionnes.add(produit);
                    }
                }
            }

            if (produitsSelectionnes.isEmpty()) {
                Toast.makeText(this, "Veuillez sélectionner au moins un produit", Toast.LENGTH_SHORT).show();
                return;
            }

            // Construction de CommandeDTO
            CommandeDTO commandeDTO = new CommandeDTO();
            commandeDTO.setNomClient(nomClient);
            commandeDTO.setSalle(salle);
            commandeDTO.setDate(date);
            commandeDTO.setStatut(statut);
            commandeDTO.setTypeClient(typeClient);
            commandeDTO.setTypeCommande(typeCommande);
            commandeDTO.setNombreTables(nombreTables);
            commandeDTO.setPrixParTable(prixParTable);
            commandeDTO.setProduits(produitsSelectionnes);

            // Envoi de la commande au backend
            ApiService apiService = RetrofitClient.getInstance().getApi();
            apiService.creerCommande(commandeDTO).enqueue(new Callback<Commande>() {
                @Override
                public void onResponse(Call<Commande> call, Response<Commande> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Long id = response.body().getId();
                        Intent intent = new Intent(SelectionProduitsActivity.this, FicheCommandeActivity.class);
                        intent.putExtra("commandeId", id);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SelectionProduitsActivity.this, "Échec de création de la commande", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Commande> call, Throwable t) {
                    Toast.makeText(SelectionProduitsActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private SectionProduit getOrCreateSection(String titre) {
        for (SectionProduit section : sections) {
            if (section.getTitre().equalsIgnoreCase(titre)) {
                return section;
            }
        }
        SectionProduit nouvelle = new SectionProduit(titre, new ArrayList<>());
        sections.add(nouvelle);
        return nouvelle;
    }

    private void recalculerTotal() {
        double total = 0;
        try {
            prixParTable = Double.parseDouble(etPrixTable.getText().toString());
        } catch (NumberFormatException e) {
            prixParTable = 0;
        }

        total += prixParTable * nombreTables;

        for (SectionProduit section : sections) {
            for (ProduitCommande p : section.getProduits()) {
                if (p.isSelectionne() && p.getPrix() > 0) {
                    total += p.getPrix();
                }
            }
        }

        tvTotal.setText("Total : " + String.format("%.2f", total) + " DH");
    }
}
