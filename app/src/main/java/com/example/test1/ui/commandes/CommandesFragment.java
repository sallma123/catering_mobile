package com.example.test1.ui.commandes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.test1.databinding.FragmentCommandesBinding;

public class CommandesFragment extends Fragment {

    private FragmentCommandesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CommandesViewModel commandesViewModel =
                new ViewModelProvider(this).get(CommandesViewModel.class);

        binding = FragmentCommandesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCommandes;
        commandesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
