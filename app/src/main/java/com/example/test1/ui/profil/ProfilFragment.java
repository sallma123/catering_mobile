package com.example.test1.ui.profil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.test1.databinding.FragmentProfilBinding;
import com.example.test1.ui.login.LoginActivity;

public class ProfilFragment extends Fragment {

    private FragmentProfilBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ProfilViewModel profilViewModel =
                new ViewModelProvider(this).get(ProfilViewModel.class);

        binding = FragmentProfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 📨 Récupérer l'email depuis SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "Utilisateur non connecté");

        // 🖼 Afficher l'email dans le TextView
        binding.textProfil.setText("Connecté en tant que :\n" + email);

        // 🔴 Déconnexion
        binding.btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply(); // Efface la session

            // Redirige vers LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish(); // ferme l'activité courante
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
