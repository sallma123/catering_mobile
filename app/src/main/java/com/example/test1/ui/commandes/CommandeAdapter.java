package com.example.test1.ui.commandes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.model.Commande;
import com.example.test1.model.CommandeItem;

import java.util.ArrayList;
import java.util.List;

public class CommandeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_COMMANDE = 1;

    private List<CommandeItem> items;
    private List<CommandeItem> fullList;  // ✅ Liste originale non filtrée

    public CommandeAdapter(List<CommandeItem> items) {
        this.items = items;
        this.fullList = new ArrayList<>(items);
    }

    public void setItems(List<CommandeItem> items) {
        this.items = items;
        this.fullList = new ArrayList<>(items); // ✅ mettre à jour la source originale
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getType() == CommandeItem.Type.HEADER) {
            return TYPE_HEADER;
        } else {
            return TYPE_COMMANDE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_mois, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commande, parent, false);
            return new CommandeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CommandeItem item = items.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).tvMois.setText(item.getMois());
        } else if (holder instanceof CommandeViewHolder) {
            Commande commande = item.getCommande();
            ((CommandeViewHolder) holder).tvTypeCommande.setText(commande.getTypeCommande());

            String details = commande.getNomClient() + " | " + commande.getSalle()
                    + " | " + commande.getNombreTables() + " tables";
            ((CommandeViewHolder) holder).tvDetailsCommande.setText(details);

            if (commande.getDate() != null && commande.getDate().contains("-")) {
                String[] dateParts = commande.getDate().split("-");
                if (dateParts.length == 3) {
                    String shortDate = dateParts[2] + "/" + dateParts[1];
                    ((CommandeViewHolder) holder).tvDateCommande.setText(shortDate);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return (items != null) ? items.size() : 0;
    }

    // ✅ ViewHolder pour entête (mois)
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvMois;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMois = itemView.findViewById(R.id.tvMois);
        }
    }

    // ✅ ViewHolder pour commande
    public static class CommandeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTypeCommande, tvDetailsCommande, tvDateCommande;

        public CommandeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTypeCommande = itemView.findViewById(R.id.tvTypeCommande);
            tvDetailsCommande = itemView.findViewById(R.id.tvDetailsCommande);
            tvDateCommande = itemView.findViewById(R.id.tvDateCommande);
        }
    }

    // ✅ Méthode de filtrage dynamique
    public void filter(String query) {
        if (query == null || query.trim().isEmpty()) {
            items = new ArrayList<>(fullList);
            notifyDataSetChanged();
            return;
        }

        query = query.toLowerCase().trim();
        List<CommandeItem> filtered = new ArrayList<>();
        String currentHeader = null;

        for (CommandeItem item : fullList) {
            if (item.getType() == CommandeItem.Type.HEADER) {
                currentHeader = item.getMois();
            } else if (item.getType() == CommandeItem.Type.COMMANDE) {
                Commande c = item.getCommande();
                if (c.getNomClient().toLowerCase().contains(query)
                        || c.getTypeCommande().toLowerCase().contains(query)) {

                    if (!containsHeader(filtered, currentHeader)) {
                        filtered.add(new CommandeItem(CommandeItem.Type.HEADER, currentHeader, null));
                    }
                    filtered.add(item);
                }
            }
        }

        this.items = filtered;
        notifyDataSetChanged();
    }

    private boolean containsHeader(List<CommandeItem> list, String mois) {
        for (CommandeItem item : list) {
            if (item.getType() == CommandeItem.Type.HEADER && mois.equals(item.getMois())) {
                return true;
            }
        }
        return false;
    }
}
