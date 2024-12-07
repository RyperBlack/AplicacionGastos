package com.example.tfg_aplicaciongastos.ui.exchanges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.adapters.ExchangesListAdapter;
import com.example.tfg_aplicaciongastos.databinding.FragmentExchangesBinding;
import com.example.tfg_aplicaciongastos.ddbb.classes.Exchanges;

public class ExchangesFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;
    private FragmentExchangesBinding binding;
    private ExchangesListAdapter adapter;

    public static ExchangesFragment newInstance(int position) {
        ExchangesFragment fragment = new ExchangesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ExchangesViewModel exchangesViewModel = new ViewModelProvider(this).get(ExchangesViewModel.class);

        binding = FragmentExchangesBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
        }

        adapter = new ExchangesListAdapter();

        adapter.setOnExchangeClickListener(exchange -> navigateToCreateExchange(exchange));

        binding.ExchangesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.ExchangesRecycler.setAdapter(adapter);

        if (position == 0) {
            exchangesViewModel.loadExchanges(requireContext(), 1);
        } else {
            exchangesViewModel.loadExchanges(requireContext(), 0);
        }

        exchangesViewModel.getExchanges().observe(getViewLifecycleOwner(), exchanges -> adapter.setExchanges(exchanges));

        binding.fabAddExchange.setOnClickListener(v -> navigateToCreateExchange(null));

        return binding.getRoot();
    }

    private void navigateToCreateExchange(@Nullable Exchanges exchange) {
        NavController navController = Navigation.findNavController(binding.getRoot());
        Bundle args = new Bundle();
        if (exchange != null) {
            args.putInt("exchange_id", exchange.getId());
            args.putInt("type", exchange.getType());
            args.putString("exchange_name", exchange.getName());
            args.putInt("category_id", exchange.getCategoryId());
        }
            args.putInt("type", position);
        navController.navigate(R.id.action_exchangesFragment_to_createExchangeFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}