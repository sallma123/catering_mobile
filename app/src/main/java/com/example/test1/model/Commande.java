package com.example.test1.model;

import java.util.List;

public class Commande {

    private Long id;
    private String typeClient;      // PARTICULIER, ENTREPRISE, PARTENAIRE
    private String typeCommande;    // MARIAGE, BUFFET, etc.
    private String statut;          // PAYEE, NON_PAYEE, etc.
    private String nomClient;
    private String salle;
    private int nombreTables;
    private double prixParTable;
    private double total;
    private String date;            // format ISO : "2025-08-27"
    private List<ProduitCommande> produits;

    // --- Getters et Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(String typeClient) {
        this.typeClient = typeClient;
    }

    public String getTypeCommande() {
        return typeCommande;
    }

    public void setTypeCommande(String typeCommande) {
        this.typeCommande = typeCommande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public int getNombreTables() {
        return nombreTables;
    }

    public void setNombreTables(int nombreTables) {
        this.nombreTables = nombreTables;
    }

    public double getPrixParTable() {
        return prixParTable;
    }

    public void setPrixParTable(double prixParTable) {
        this.prixParTable = prixParTable;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ProduitCommande> getProduits() {
        return produits;
    }

    public void setProduits(List<ProduitCommande> produits) {
        this.produits = produits;
    }
}
