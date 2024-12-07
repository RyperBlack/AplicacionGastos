package com.example.tfg_aplicaciongastos.ui.account;

import android.app.AlertDialog;
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

import java.util.Objects;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private AccountViewModel accountViewModel;
    private AccountListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        binding.AccountRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AccountListAdapter(new AccountListAdapter.OnAccountInteractionListener() {
            @Override
            public void onEditAccount(Account account) {
                navigateToCreateAccount(account);
            }

            @Override
            public void onDeleteAccount(Account account) {
                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.confirm_delete_account_title)
                        .setMessage(R.string.confirm_delete_account_message)
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            accountViewModel.deleteAccount(account);
                            Toast.makeText(requireContext(), R.string.account_deleted, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                        .show();
            }

            @Override
            public void onAccountSelected(Account account) {
                accountViewModel.setSelectedAccount(account);
            }
        });
        binding.AccountRecycler.setAdapter(adapter);

        binding.fabAddAccount.setOnClickListener(v -> navigateToCreateAccount(null));

        accountViewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> adapter.setAccounts(accounts));

        return binding.getRoot();
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
        int selectedAccountId = accountViewModel.getSelectedAccountId();
            for (Account account : Objects.requireNonNull(accountViewModel.getAccounts().getValue())) {
                if (account.getId() == selectedAccountId) {
                    adapter.setSelectedAccountId(account.getId());
                    break;
                }
            }
        }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
