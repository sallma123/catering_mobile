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

import java.util.ArrayList;
import java.util.List;

public class CommandesFragment extends Fragment {

    private FragmentCommandesBinding binding;
    private CommandesViewModel commandesViewModel;
    private CommandeAdapter commandeAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // ViewModel
        commandesViewModel = new ViewModelProvider(this).get(CommandesViewModel.class);

        // Binding
        binding = FragmentCommandesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // RecyclerView setup
        binding.rvCommandes.setLayoutManager(new LinearLayoutManager(getContext()));
        commandeAdapter = new CommandeAdapter(new ArrayList<>());
        binding.rvCommandes.setAdapter(commandeAdapter);

        // Observer → LiveData from ViewModel
        commandesViewModel.getCommandes().observe(getViewLifecycleOwner(), new Observer<List<Commande>>() {
            @Override
            public void onChanged(List<Commande> commandes) {
                commandeAdapter.setCommandes(commandes);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
