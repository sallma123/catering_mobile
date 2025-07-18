package com.example.test1.model;

import java.util.List;

public class SectionProduit {
    private String titre;
    private List<ProduitCommande> produits;

    public SectionProduit(String titre, List<ProduitCommande> produits) {
        this.titre = titre;
        this.produits = produits;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public List<ProduitCommande> getProduits() {
        return produits;
    }

    public void setProduits(List<ProduitCommande> produits) {
        this.produits = produits;
    }
}
