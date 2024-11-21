package com.example.tfg_aplicaciongastos.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        // Configurar el RecyclerView
        binding.AccountRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AccountListAdapter(new AccountListAdapter.OnAccountInteractionListener() {
            @Override
            public void onEditAccount(Account account) {
                navigateToCreateAccount(account);
            }

            @Override
            public void onDeleteAccount(Account account) {
                accountViewModel.deleteAccount(account);
                Toast.makeText(requireContext(), R.string.account_deleted, Toast.LENGTH_SHORT).show();
            }
        });
        binding.AccountRecycler.setAdapter(adapter);

        // Configurar FAB
        binding.fabAddAccount.setOnClickListener(v -> navigateToCreateAccount(null));

        // Observar cambios en la lista de cuentas
        accountViewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> {
            adapter.setAccounts(accounts); // Actualizar el adaptador
        });

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
    public void onResume() {
        super.onResume();
        accountViewModel.loadAccounts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
