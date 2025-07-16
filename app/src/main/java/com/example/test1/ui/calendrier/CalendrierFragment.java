package com.example.test1.ui.calendrier;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.test1.databinding.FragmentCalendrierBinding;

public class CalendrierFragment extends Fragment {

    private FragmentCalendrierBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CalendrierViewModel calendrierViewModel =
                new ViewModelProvider(this).get(CalendrierViewModel.class);

        binding = FragmentCalendrierBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCalendrier;
        calendrierViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}