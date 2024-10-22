package com.example.tfg_aplicaciongastos.ui.habitualPayments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tfg_aplicaciongastos.databinding.FragmentHabitualPaymentsBinding;


public class HabitualPaymentsFragment extends Fragment{
    private FragmentHabitualPaymentsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HabitualPaymentsViewModel habitualPaymentsViewModel =
                new ViewModelProvider(this).get(HabitualPaymentsViewModel.class);

        binding = FragmentHabitualPaymentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHabitualPayment;
        habitualPaymentsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
