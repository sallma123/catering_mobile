package com.example.test1.model;

import java.util.List;

public class CommandeDTO {
    private String nomClient;
    private String salle;
    private int nombreTables;
    private double prixParTable;
    private String typeClient;
    private String typeCommande;
    private String statut;
    private List<ProduitCommande> produits;

    // Getters et Setters
    public String getNomClient() { return nomClient; }
    public void setNomClient(String nomClient) { this.nomClient = nomClient; }

    public String getSalle() { return salle; }
    public void setSalle(String salle) { this.salle = salle; }

    public int getNombreTables() { return nombreTables; }
    public void setNombreTables(int nombreTables) { this.nombreTables = nombreTables; }

    public double getPrixParTable() { return prixParTable; }
    public void setPrixParTable(double prixParTable) { this.prixParTable = prixParTable; }

    public String getTypeClient() { return typeClient; }
    public void setTypeClient(String typeClient) { this.typeClient = typeClient; }

    public String getTypeCommande() { return typeCommande; }
    public void setTypeCommande(String typeCommande) { this.typeCommande = typeCommande; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public List<ProduitCommande> getProduits() { return produits; }
    public void setProduits(List<ProduitCommande> produits) { this.produits = produits; }
}
