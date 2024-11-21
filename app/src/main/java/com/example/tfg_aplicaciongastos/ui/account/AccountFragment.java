package com.example.tfg_aplicaciongastos.ui.account;

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
import com.example.tfg_aplicaciongastos.adapters.AccountListAdapter;
import com.example.tfg_aplicaciongastos.databinding.FragmentAccountBinding;
import com.example.tfg_aplicaciongastos.ddbb.classes.Account;

import java.util.List;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private AccountViewModel accountViewModel;
    private AccountListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        // Inicializar el ViewModel
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        // Configurar RecyclerView
        binding.AccountRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AccountListAdapter(new AccountListAdapter.OnAccountInteractionListener() {
            @Override
            public void onEditAccount(Account account) {
                navigateToCreateAccount(account);
            }
        });
        binding.AccountRecycler.setAdapter(adapter);

        // Configurar el FAB para navegar al fragmento de creación vacío
        binding.fabAddAccount.setOnClickListener(v -> navigateToCreateAccount(null));

        // Observar cambios en las cuentas
        accountViewModel.getAccounts().observe(getViewLifecycleOwner(), this::updateRecyclerView);

        return binding.getRoot();
    }

    private void updateRecyclerView(List<Account> accounts) {
        adapter.setAccounts(accounts);
    }

    private void navigateToCreateAccount(@Nullable Account account) {
        NavController navController = Navigation.findNavController(binding.getRoot());
        Bundle args = new Bundle();
        if (account != null) {
            args.putInt("account_id", account.getId());
            args.putString("account_name", account.getName());
            args.putDouble("account_total", account.getTotal());
        }
        navController.navigate(R.id.action_accountFragment_to_createAccountFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
