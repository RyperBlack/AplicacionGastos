package com.example.tfg_aplicaciongastos.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.ddbb.classes.Account;

import java.util.ArrayList;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolderData> {

    ArrayList<Account> AccountList;
    OnAddAccountClickListener addAccountClickListener;


    public AccountListAdapter(ArrayList<Account> accountList, OnAddAccountClickListener addAccountClickListener) {

        this.AccountList = accountList;
        this.addAccountClickListener = addAccountClickListener;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list, null, false);

        return new ViewHolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {
        holder.accountName.setText(AccountList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return AccountList.size();
    }

    public static class ViewHolderData extends RecyclerView.ViewHolder {
        TextView accountName;

        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
            accountName = itemView.findViewById(R.id.accountName);

        }
    }

    public interface OnAddAccountClickListener {
        void onAddAccountClick();
    }
}
