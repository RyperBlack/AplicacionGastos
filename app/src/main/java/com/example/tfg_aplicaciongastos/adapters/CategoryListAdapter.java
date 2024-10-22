package com.example.tfg_aplicaciongastos.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.ddbb.classes.Categories;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CATEGORY = 0;
    private static final int TYPE_ADD_BUTTON = 1;

    ArrayList<Categories> categoryDataList;
    OnAddCategoryClickListener addCategoryClickListener;

    public CategoryListAdapter(ArrayList<Categories> categoryDataList, OnAddCategoryClickListener addCategoryClickListener) {
        this.categoryDataList = categoryDataList;
        this.addCategoryClickListener = addCategoryClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        // Si la posición es la última, mostramos el botón de añadir
        if (position == categoryDataList.size()) {
            return TYPE_ADD_BUTTON;
        } else {
            return TYPE_CATEGORY;
        }
    }

    @Override
    public int getItemCount() {
        // Sumar 1 para incluir el botón de añadir al final
        return categoryDataList.size() + 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CATEGORY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false);
            return new ViewHolderData(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_button, parent, false);
            return new ViewHolderAddButton(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderData) {
            ViewHolderData viewHolderData = (ViewHolderData) holder;
            viewHolderData.Name.setText(categoryDataList.get(position).getName());
        } else if (holder instanceof ViewHolderAddButton) {
            ViewHolderAddButton viewHolderAddButton = (ViewHolderAddButton) holder;
            viewHolderAddButton.cardViewButton.setOnClickListener(v -> {
                if (addCategoryClickListener != null) {
                    addCategoryClickListener.onAddCategoryClick();
                }
            });
        }
    }

    public static class ViewHolderData extends RecyclerView.ViewHolder {
        TextView Name;

        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.NameText);
        }
    }

    public static class ViewHolderAddButton extends RecyclerView.ViewHolder {
        CardView cardViewButton;

        public ViewHolderAddButton(@NonNull View itemView) {
            super(itemView);
            cardViewButton = itemView.findViewById(R.id.cardViewButton);
        }
    }

    public interface OnAddCategoryClickListener {
        void onAddCategoryClick();
    }
}