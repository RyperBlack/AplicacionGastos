package com.example.tfg_aplicaciongastos.adapters;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_aplicaciongastos.databinding.ExchangesListBinding;
import com.example.tfg_aplicaciongastos.ddbb.classes.Exchanges;

import java.util.ArrayList;
import java.util.List;

/** @noinspection ClassEscapesDefinedScope*/
public class ExchangesListAdapter extends RecyclerView.Adapter<ExchangesListAdapter.ExchangesViewHolder> {

    private final List<Exchanges> exchangesList = new ArrayList<>();
    private OnExchangeClickListener clickListener;
    private OnExchangeLongClickListener longClickListener;

    public interface OnExchangeClickListener {
        void onExchangeClick(Exchanges exchange);
    }

    public void setOnExchangeClickListener(OnExchangeClickListener listener) {
        this.clickListener = listener;
    }

    public interface OnExchangeLongClickListener {
        void onExchangeLongClick(Exchanges exchange);
    }

    public void setOnExchangeLongClickListener(OnExchangeLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public ExchangesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ExchangesListBinding binding = ExchangesListBinding.inflate(inflater, parent, false);
        return new ExchangesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangesViewHolder holder, int position) {
        Exchanges exchange = exchangesList.get(position);
        holder.bind(exchange);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                clickListener.onExchangeClick(exchangesList.get(holder.getAdapterPosition()));
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                longClickListener.onExchangeLongClick(exchangesList.get(holder.getAdapterPosition()));
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return exchangesList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setExchanges(List<Exchanges> exchanges) {
        exchangesList.clear();
        exchangesList.addAll(exchanges);
        notifyDataSetChanged();
    }

    static class ExchangesViewHolder extends RecyclerView.ViewHolder {

        private final ExchangesListBinding binding;

        public ExchangesViewHolder(@NonNull ExchangesListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("DefaultLocale")
        public void bind(Exchanges exchange) {

            binding.accountName.setText(exchange.getName());
            binding.categoryName.setText(exchange.getCategoryName());
            binding.accountTotal.setText(String.format("%.2f €", exchange.getQuantity()));

            int color = Color.parseColor(exchange.getCategoryColor());
            binding.selectionCircle.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }
}
