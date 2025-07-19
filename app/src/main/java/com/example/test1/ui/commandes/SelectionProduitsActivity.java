package com.example.test1.ui.commandes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.model.ProduitCommande;
import com.example.test1.model.SectionProduit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectionProduitsActivity extends AppCompatActivity {

    private TextView tvTitreProduits, tvTotal;
    private EditText etPrixTable, etProduitPerso, etPrixPerso;
    private Button btnValider, btnAjouterProduit;
    private RecyclerView rvProduits;
    private SectionProduitAdapter adapter;

    private List<SectionProduit> sections = new ArrayList<>();
    private double prixParTable = 0;
    private int nombreTables = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_produits);

        // Initialisation des vues
        tvTitreProduits = findViewById(R.id.tvTitreProduits);
        tvTotal = findViewById(R.id.tvTotal);
        etPrixTable = findViewById(R.id.etPrixTable);
        etProduitPerso = findViewById(R.id.etProduitPerso);
        etPrixPerso = findViewById(R.id.etPrixPerso);
        btnValider = findViewById(R.id.btnValider);
        btnAjouterProduit = findViewById(R.id.btnAjouterProduit);
        rvProduits = findViewById(R.id.rvProduits);

        // Récupération des extras si présents
        String typeCommande = getIntent().getStringExtra("typeCommande");
        nombreTables = getIntent().getIntExtra("nombre", 1);

        tvTitreProduits.setText("Produits pour " + typeCommande);

        // Créer les sections
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

        // Setup RecyclerView avec SectionProduitAdapter
        adapter = new SectionProduitAdapter(sections, this::recalculerTotal);
        rvProduits.setLayoutManager(new LinearLayoutManager(this));
        rvProduits.setAdapter(adapter);

        // Ajouter un produit personnalisé indépendant (hors section)
        btnAjouterProduit.setOnClickListener(v -> {
            String nom = etProduitPerso.getText().toString().trim();
            String prixStr = etPrixPerso.getText().toString().trim();

            if (!nom.isEmpty() && !prixStr.isEmpty()) {
                try {
                    double prix = Double.parseDouble(prixStr);
                    ProduitCommande p = new ProduitCommande(nom, "Supplément", prix);
                    p.setSelectionne(true);
                    getOrCreateSection("Supplément").getProduits().add(p);

                    adapter = new SectionProduitAdapter(sections, this::recalculerTotal);
                    rvProduits.setAdapter(adapter);

                    etProduitPerso.setText("");
                    etPrixPerso.setText("");
                    recalculerTotal();
                } catch (NumberFormatException e) {
                    etPrixPerso.setError("Prix invalide");
                }
            }
        });

        etPrixTable.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) recalculerTotal();
        });

        btnValider.setOnClickListener(v -> {
            // TODO : générer la fiche PDF ou résumé final
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
