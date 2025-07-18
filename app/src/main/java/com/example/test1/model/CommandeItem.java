package com.example.test1.model;

public class CommandeItem {

    public enum Type {
        HEADER, COMMANDE
    }

    private Type type;
    private String mois; // pour HEADER
    private Commande commande; // pour COMMANDE

    public CommandeItem(Type type, String mois, Commande commande) {
        this.type = type;
        this.mois = mois;
        this.commande = commande;
    }

    public Type getType() {
        return type;
    }

    public String getMois() {
        return mois;
    }

    public Commande getCommande() {
        return commande;
    }
}
