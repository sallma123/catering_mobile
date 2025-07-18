package com.example.test1.ui.commandes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        commandesViewModel = new ViewModelProvider(this).get(CommandesViewModel.class);
        binding = FragmentCommandesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.rvCommandes.setLayoutManager(new LinearLayoutManager(getContext()));
        commandeAdapter = new CommandeAdapter(new ArrayList<>());
        binding.rvCommandes.setAdapter(commandeAdapter);

        commandesViewModel.getCommandes().observe(getViewLifecycleOwner(), new Observer<List<Commande>>() {
            @Override
            public void onChanged(List<Commande> commandes) {
                List<CommandeItem> items = regrouperParMois(commandes);
                commandeAdapter.setItems(items);
            }
        });

        return root;
    }

    private List<CommandeItem> regrouperParMois(List<Commande> commandes) {
        List<CommandeItem> items = new ArrayList<>();
        HashMap<String, List<Commande>> commandesParMois = new HashMap<>();

        for (Commande commande : commandes) {
            if (commande.getDate() == null) continue;
            String[] parts = commande.getDate().split("-");
            if (parts.length < 2) continue;

            String mois = getNomMois(parts[1]); // "05" → "Mai"

            if (!commandesParMois.containsKey(mois)) {
                commandesParMois.put(mois, new ArrayList<>());
            }
            commandesParMois.get(mois).add(commande);
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
