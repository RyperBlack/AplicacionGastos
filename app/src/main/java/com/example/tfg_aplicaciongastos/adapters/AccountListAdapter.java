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

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolderData> implements View.OnClickListener, View.OnLongClickListener {

    ArrayList<Account> AccountList;

    private View.OnClickListener listener;
    private View.OnLongClickListener longlistener;

    public AccountListAdapter(ArrayList<Account> accountList) {

        this.AccountList = accountList;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list, null, false);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

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

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;

    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public void setOnLongClickListener(View.OnLongClickListener longlistener) {
        this.longlistener = longlistener;
    }

    @Override
    public boolean onLongClick(View v) {
        if (longlistener != null) {
            longlistener.onLongClick(v);
        }
        return true;
    }

    @Override
    public boolean onLongClickUseDefaultHapticFeedback(@NonNull View v) {
        return View.OnLongClickListener.super.onLongClickUseDefaultHapticFeedback(v);
    }

    public static class ViewHolderData extends RecyclerView.ViewHolder {
        TextView accountName;

        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
            accountName = itemView.findViewById(R.id.accountName);

        }
    }
}
