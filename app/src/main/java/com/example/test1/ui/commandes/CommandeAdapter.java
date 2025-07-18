package com.example.test1.ui.commandes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.model.Commande;

import java.util.List;

public class CommandeAdapter extends RecyclerView.Adapter<CommandeAdapter.CommandeViewHolder> {

    private List<Commande> commandes;

    public CommandeAdapter(List<Commande> commandes) {
        this.commandes = commandes;
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommandeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commande, parent, false);
        return new CommandeViewHolder(vue);
    }

    @Override
    public void onBindViewHolder(@NonNull CommandeViewHolder holder, int position) {
        Commande commande = commandes.get(position);

        holder.tvTypeCommande.setText(commande.getTypeCommande());

        String details = commande.getNomClient() + " | " + commande.getSalle()
                + " | " + commande.getNombreTables() + " tables";
        holder.tvDetailsCommande.setText(details);

        // Format de date : "2025-08-27" → "27/08"
        if (commande.getDate() != null && commande.getDate().contains("-")) {
            String[] dateParts = commande.getDate().split("-");
            if (dateParts.length == 3) {
                String shortDate = dateParts[2] + "/" + dateParts[1];
                holder.tvDateCommande.setText(shortDate);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (commandes != null) ? commandes.size() : 0;
    }

    public static class CommandeViewHolder extends RecyclerView.ViewHolder {

        TextView tvTypeCommande, tvDetailsCommande, tvDateCommande;

        public CommandeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTypeCommande = itemView.findViewById(R.id.tvTypeCommande);
            tvDetailsCommande = itemView.findViewById(R.id.tvDetailsCommande);
            tvDateCommande = itemView.findViewById(R.id.tvDateCommande);
        }
    }
}
