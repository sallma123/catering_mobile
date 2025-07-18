package com.example.test1.ui.commandes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.model.ProduitCommande;
import com.example.test1.model.SectionProduit;

import java.util.ArrayList;
import java.util.List;

public class SectionProduitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_ADD_INPUT = 2;

    private final List<Object> displayItems = new ArrayList<>();
    private final Runnable onSelectionChanged;

    public SectionProduitAdapter(List<SectionProduit> sections, Runnable onSelectionChanged) {
        this.onSelectionChanged = onSelectionChanged;
        for (SectionProduit section : sections) {
            displayItems.add(section.getTitre());
            displayItems.addAll(section.getProduits());
            displayItems.add(new AddProduitInput(section));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object item = displayItems.get(position);
        if (item instanceof String) return TYPE_HEADER;
        else if (item instanceof AddProduitInput) return TYPE_ADD_INPUT;
        else return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section_header, parent, false);
            return new SectionHeaderViewHolder(view);
        } else if (viewType == TYPE_ADD_INPUT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_produit, parent, false);
            return new AddProduitViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produit, parent, false);
            return new ProduitViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = displayItems.get(position);

        if (holder instanceof SectionHeaderViewHolder) {
            String titre = (String) item;
            ((SectionHeaderViewHolder) holder).tvSectionTitle.setText(titre);
            ((SectionHeaderViewHolder) holder).cbTout.setOnClickListener(v -> toggleSection(titre, ((SectionHeaderViewHolder) holder).cbTout.isChecked()));
        } else if (holder instanceof ProduitViewHolder) {
            ProduitCommande p = (ProduitCommande) item;
            ((ProduitViewHolder) holder).tvNom.setText(p.getNom());
            ((ProduitViewHolder) holder).tvPrix.setVisibility(p.getPrix() > 0 ? View.VISIBLE : View.GONE);
            ((ProduitViewHolder) holder).tvPrix.setText(String.format("%.2f DH", p.getPrix()));
            ((ProduitViewHolder) holder).cbProduit.setOnCheckedChangeListener(null);
            ((ProduitViewHolder) holder).cbProduit.setChecked(p.isSelectionne());
            ((ProduitViewHolder) holder).cbProduit.setOnCheckedChangeListener((buttonView, isChecked) -> {
                p.setSelectionne(isChecked);
                onSelectionChanged.run();
            });
        } else if (holder instanceof AddProduitViewHolder) {
            AddProduitInput addInput = (AddProduitInput) item;
            ((AddProduitViewHolder) holder).btnAjouter.setOnClickListener(v -> {
                String nom = ((AddProduitViewHolder) holder).etNouveau.getText().toString().trim();
                if (!nom.isEmpty()) {
                    ProduitCommande p = new ProduitCommande(nom, addInput.section.getTitre(), 0);
                    p.setSelectionne(true);
                    addInput.section.getProduits().add(p);
                    int index = displayItems.indexOf(item);
                    displayItems.add(index, p);
                    notifyDataSetChanged();
                    onSelectionChanged.run();
                }
            });
        }
    }

    private void toggleSection(String titre, boolean checkAll) {
        for (Object item : displayItems) {
            if (item instanceof ProduitCommande) {
                ProduitCommande p = (ProduitCommande) item;
                if (titre.equalsIgnoreCase(p.getCategorie())) {
                    p.setSelectionne(checkAll);
                }
            }
        }
        notifyDataSetChanged();
        onSelectionChanged.run();
    }

    @Override
    public int getItemCount() {
        return displayItems.size();
    }

    public static class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvSectionTitle;
        CheckBox cbTout;

        public SectionHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSectionTitle = itemView.findViewById(R.id.tvSectionTitle);
            cbTout = itemView.findViewById(R.id.cbTout);
        }
    }

    public static class ProduitViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbProduit;
        TextView tvNom, tvPrix;

        public ProduitViewHolder(@NonNull View itemView) {
            super(itemView);
            cbProduit = itemView.findViewById(R.id.cbProduit);
            tvNom = itemView.findViewById(R.id.tvNomProduit);
            tvPrix = itemView.findViewById(R.id.tvPrixProduit);
        }
    }

    public static class AddProduitViewHolder extends RecyclerView.ViewHolder {
        EditText etNouveau;
        Button btnAjouter;

        public AddProduitViewHolder(@NonNull View itemView) {
            super(itemView);
            etNouveau = itemView.findViewById(R.id.etNouveauProduit);
            btnAjouter = itemView.findViewById(R.id.btnAjouterProduitSection);
        }
    }

    public static class AddProduitInput {
        SectionProduit section;

        public AddProduitInput(SectionProduit section) {
            this.section = section;
        }
    }
}
