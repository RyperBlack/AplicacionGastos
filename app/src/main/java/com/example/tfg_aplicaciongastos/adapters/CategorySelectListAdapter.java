package com.example.tfg_aplicaciongastos.adapters;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.ddbb.classes.Category;

import java.util.List;

public class CategorySelectListAdapter extends RecyclerView.Adapter<CategorySelectListAdapter.CategoryViewHolder> {
    private List<Category> categories;
    private int selectedCategoryId = -1;
    private final OnCategorySelectedListener listener;

    public interface OnCategorySelectedListener {
        void onCategorySelected(Category category);
    }

    public CategorySelectListAdapter(OnCategorySelectedListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectedCategoryId(int selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_select_list, parent, false);
        return new CategoryViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);

        holder.name.setText(category.getName());
        int color = Color.parseColor(category.getHexCode());
        holder.colorImageView.setBackgroundTintList(ColorStateList.valueOf(color));

        boolean isSelected = category.getId() == selectedCategoryId;

        if (isSelected) {
            holder.cardView.setBackgroundResource(R.drawable.border_selected);
        } else {
            holder.cardView.setBackgroundResource(R.drawable.border_default);
        }

        holder.itemView.setOnClickListener(v -> {
            selectedCategoryId = category.getId();
            listener.onCategorySelected(category);
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final ImageView colorImageView;
        private final CardView cardView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.categoryName);
            colorImageView = itemView.findViewById(R.id.colorImageView);
            cardView = itemView.findViewById(R.id.categoryCard);
        }
    }
}
