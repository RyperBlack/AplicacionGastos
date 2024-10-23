package com.example.tfg_aplicaciongastos.ui.graph;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tfg_aplicaciongastos.databinding.FragmentGraphBinding;


public class GraphFragment extends Fragment {

    private FragmentGraphBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GraphViewModel graphViewModel =
                new ViewModelProvider(this).get(GraphViewModel.class);

        binding = FragmentGraphBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGraph;
        graphViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}