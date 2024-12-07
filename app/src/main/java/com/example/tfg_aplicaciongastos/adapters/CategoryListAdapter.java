package com.example.tfg_aplicaciongastos.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.databinding.CategoryListBinding;
import com.example.tfg_aplicaciongastos.ddbb.classes.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CATEGORY = 0;
    private static final int TYPE_CREATE = 1;

    private final List<Category> categoryList = new ArrayList<>();
    private final OnCategoryInteractionListener interactionListener;

    public CategoryListAdapter(OnCategoryInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public int getItemViewType(int position) {
        return (position < categoryList.size()) ? TYPE_CATEGORY : TYPE_CREATE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CATEGORY) {
            CategoryListBinding binding = CategoryListBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new CategoryViewHolder(binding);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_button_list, parent, false);
            return new CreateCategoryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_CATEGORY) {
            CategoryViewHolder categoryHolder = (CategoryViewHolder) holder;
            Category category = categoryList.get(position);

            categoryHolder.binding.NameText.setText(category.getName());

            try {
                int color = Color.parseColor(category.getHexCode());
                categoryHolder.binding.colorImageView.setBackgroundTintList(ColorStateList.valueOf(color));
            } catch (IllegalArgumentException e) {
                categoryHolder.binding.colorImageView.setBackgroundColor(Color.GRAY);
            }

            categoryHolder.binding.getRoot().setOnClickListener(v -> interactionListener.onEditCategory(category));
            categoryHolder.binding.getRoot().setOnLongClickListener(v -> {
                showDeleteConfirmationDialog(v.getContext(), category);
                return true;
            });
        } else {
            CreateCategoryViewHolder createHolder = (CreateCategoryViewHolder) holder;
            createHolder.itemView.setOnClickListener(v -> interactionListener.onCreateCategory());
        }
    }


    @Override
    public int getItemCount() {
        return categoryList.size() + 1;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCategories(List<Category> categories) {
        categoryList.clear();
        categoryList.addAll(categories);
        notifyDataSetChanged();
    }

    private void showDeleteConfirmationDialog(Context context, Category category) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.confirm_delete_category_title)
                .setMessage(R.string.confirm_delete_category_message)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    interactionListener.onDeleteCategory(category);
                    Toast.makeText(context, R.string.category_deleted, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .show();
    }

    public interface OnCategoryInteractionListener {
        void onEditCategory(Category category);

        void onCreateCategory();

        void onDeleteCategory(Category category);
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final CategoryListBinding binding;

        public CategoryViewHolder(CategoryListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class CreateCategoryViewHolder extends RecyclerView.ViewHolder {
        public CreateCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
