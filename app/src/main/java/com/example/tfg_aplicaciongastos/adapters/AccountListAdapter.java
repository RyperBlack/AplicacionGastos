package com.example.tfg_aplicaciongastos.adapters;

import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.databinding.AccountListBinding;
import com.example.tfg_aplicaciongastos.ddbb.classes.Account;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {

    private final List<Account> accountList = new ArrayList<>();
    private final OnAccountInteractionListener interactionListener;
    private int selectedPosition = RecyclerView.NO_POSITION; // Track the selected position

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
        holder.binding.accountTotal.setText(String.format("%.2f €", account.getTotal()));

        holder.binding.selectedTick.setVisibility(holder.getAdapterPosition() == selectedPosition ? View.VISIBLE : View.INVISIBLE);

        holder.binding.getRoot().setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPosition); // Update old item
            notifyItemChanged(selectedPosition); // Update new item
            interactionListener.onAccountSelected(account); // Notify selection change
        });

        holder.binding.getRoot().setOnLongClickListener(v -> {
            showPopupMenu(v, account);
            return true;
        });
    }

    private void showPopupMenu(View view, Account account) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);

        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.account_context_menu, popupMenu.getMenu());

        try {
            Field mFieldPopup = popupMenu.getClass().getDeclaredField("mPopup");
            mFieldPopup.setAccessible(true);
            Object mPopup = mFieldPopup.get(popupMenu);
            mPopup.getClass()
                    .getDeclaredMethod("setForceShowIcon", boolean.class)
                    .invoke(mPopup, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupMenu.setGravity(Gravity.END); // Aparece alineado a la derecha
        }
        popupMenu.setOnMenuItemClickListener(item -> handleMenuItemClick(item, account));

        popupMenu.show();
    }

    private boolean handleMenuItemClick(MenuItem item, Account account) {
        if (item == null) return false;

        Map<Integer, Runnable> menuActions = new HashMap<>();
        menuActions.put(R.id.menu_edit, () -> interactionListener.onEditAccount(account));
        menuActions.put(R.id.menu_delete, () -> interactionListener.onDeleteAccount(account));

        Runnable action = menuActions.get(item.getItemId());
        if (action != null) {
            action.run();
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public void setAccounts(List<Account> accounts) {
        accountList.clear();
        accountList.addAll(accounts);
        notifyDataSetChanged();
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
        void onDeleteAccount(Account account);
        void onAccountSelected(Account account);
    }
}
