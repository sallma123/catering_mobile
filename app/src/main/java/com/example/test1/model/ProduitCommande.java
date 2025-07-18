package com.example.test1.model;

public class ProduitCommande {

    private Long id;
    private String nom;
    private String categorie; // Exemple : "Réception", "Dîner", etc.
    private double prix;
    private boolean selectionne;

    // ✅ Constructeur pratique
    public ProduitCommande(String nom, String categorie, double prix) {
        this.nom = nom;
        this.categorie = categorie;
        this.prix = prix;
        this.selectionne = false; // par défaut
    }

    // --- Getters et Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public boolean isSelectionne() {
        return selectionne;
    }

    public void setSelectionne(boolean selectionne) {
        this.selectionne = selectionne;
    }
    @Override
    public String toString() {
        return nom + (prix > 0 ? " - " + prix + " DH" : "");
    }

}
