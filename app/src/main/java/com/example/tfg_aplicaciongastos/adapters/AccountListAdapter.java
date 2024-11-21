package com.example.tfg_aplicaciongastos.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_aplicaciongastos.databinding.AccountListBinding;
import com.example.tfg_aplicaciongastos.ddbb.classes.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {

    private final List<Account> accountList = new ArrayList<>();
    private final OnAccountInteractionListener interactionListener;

    public AccountListAdapter(OnAccountInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AccountListBinding binding = AccountListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account account = accountList.get(position);
        holder.binding.accountName.setText(account.getName());
        holder.binding.accountTotal.setText(String.format("Total: %.2f €", account.getTotal()));

        // Configurar clic en el botón "Editar"
        holder.binding.editAccountButton.setOnClickListener(v -> interactionListener.onEditAccount(account));

        // Configurar clic en el botón "Eliminar"
        holder.binding.deleteAccountButton.setOnClickListener(v -> interactionListener.onDeleteAccount(account));
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public void setAccounts(List<Account> accounts) {
        accountList.clear();
        accountList.addAll(accounts);
        notifyDataSetChanged(); // Notificar cambios al RecyclerView
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final AccountListBinding binding;

        public ViewHolder(AccountListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public interface OnAccountInteractionListener {
        void onEditAccount(Account account);
        void onDeleteAccount(Account account); // Nuevo método
    }
}
