package com.example.test1.ui.commandes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.test1.databinding.FragmentCommandesBinding;
import com.example.test1.model.Commande;
import com.example.test1.model.CommandeItem;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CommandesFragment extends Fragment {

    private FragmentCommandesBinding binding;
    private CommandesViewModel commandesViewModel;
    private CommandeAdapter commandeAdapter;
    private List<CommandeItem> allItems = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        commandesViewModel = new ViewModelProvider(this).get(CommandesViewModel.class);
        binding = FragmentCommandesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // RecyclerView
        binding.rvCommandes.setLayoutManager(new LinearLayoutManager(getContext()));
        commandeAdapter = new CommandeAdapter(new ArrayList<>());
        binding.rvCommandes.setAdapter(commandeAdapter);

        // Observer LiveData
        commandesViewModel.getCommandes().observe(getViewLifecycleOwner(), new Observer<List<Commande>>() {
            @Override
            public void onChanged(List<Commande> commandes) {
                allItems = regrouperParMois(commandes);
                commandeAdapter.setItems(allItems);
            }
        });

        // 🔍 SearchView : filtrage dynamique
        binding.searchViewCommandes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCommandes(newText);
                return true;
            }
        });

        // ➕ Actions des 3 boutons
        binding.btnParticulier.setOnClickListener(v -> ouvrirCreationCommande("Particulier"));
        binding.btnEntreprise.setOnClickListener(v -> ouvrirCreationCommande("Entreprise"));
        binding.btnPartenaire.setOnClickListener(v -> ouvrirCreationCommande("Partenaire"));

        return root;
    }

    private void ouvrirCreationCommande(String typeClient) {
        Intent intent = new Intent(getContext(), CreerCommandeActivity.class);
        intent.putExtra("typeClient", typeClient);
        startActivity(intent);
    }

    private void filterCommandes(String query) {
        if (query == null || query.trim().isEmpty()) {
            commandeAdapter.setItems(new ArrayList<>(allItems));
            return;
        }

        String searchText = query.toLowerCase().trim();
        List<CommandeItem> filtered = new ArrayList<>();
        String currentHeader = null;

        for (CommandeItem item : allItems) {
            if (item.getType() == CommandeItem.Type.HEADER) {
                currentHeader = item.getMois();
            } else if (item.getType() == CommandeItem.Type.COMMANDE) {
                Commande c = item.getCommande();
                if (c.getNomClient().toLowerCase().contains(searchText) ||
                        c.getTypeCommande().toLowerCase().contains(searchText)) {

                    if (!containsHeader(filtered, currentHeader)) {
                        filtered.add(new CommandeItem(CommandeItem.Type.HEADER, currentHeader, null));
                    }
                    filtered.add(item);
                }
            }
        }

        commandeAdapter.setItems(filtered);
    }

    private boolean containsHeader(List<CommandeItem> list, String mois) {
        for (CommandeItem item : list) {
            if (item.getType() == CommandeItem.Type.HEADER && mois.equals(item.getMois())) {
                return true;
            }
        }
        return false;
    }

    private List<CommandeItem> regrouperParMois(List<Commande> commandes) {
        List<CommandeItem> items = new ArrayList<>();
        HashMap<String, List<Commande>> commandesParMois = new HashMap<>();

        for (Commande commande : commandes) {
            if (commande.getDate() == null) continue;
            String[] parts = commande.getDate().split("-");
            if (parts.length < 2) continue;

            String mois = getNomMois(parts[1]);

            commandesParMois
                    .computeIfAbsent(mois, k -> new ArrayList<>())
                    .add(commande);
        }

        for (String mois : commandesParMois.keySet()) {
            items.add(new CommandeItem(CommandeItem.Type.HEADER, mois, null));
            for (Commande c : commandesParMois.get(mois)) {
                items.add(new CommandeItem(CommandeItem.Type.COMMANDE, null, c));
            }
        }

        return items;
    }

    private String getNomMois(String numeroMois) {
        try {
            int moisIndex = Integer.parseInt(numeroMois) - 1;
            return new DateFormatSymbols(Locale.FRENCH).getMonths()[moisIndex];
        } catch (Exception e) {
            return "Inconnu";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
