package com.example.test1.ui.paiement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.test1.databinding.FragmentPaiementBinding;

public class PaiementFragment extends Fragment {

    private FragmentPaiementBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PaiementViewModel dashboardViewModel =
                new ViewModelProvider(this).get(PaiementViewModel.class);

        binding = FragmentPaiementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPaiement;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}