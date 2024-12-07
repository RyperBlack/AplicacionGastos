package com.example.tfg_aplicaciongastos.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.adapters.CategoryListAdapter;
import com.example.tfg_aplicaciongastos.databinding.FragmentCategoryBinding;
import com.example.tfg_aplicaciongastos.ddbb.classes.Category;

public class CategoryFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;
    private FragmentCategoryBinding binding;
    private CategoryViewModel categoryViewModel;
    private CategoryListAdapter adapter;

    public static CategoryFragment newInstance(int position) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
        }

        adapter = new CategoryListAdapter(new CategoryListAdapter.OnCategoryInteractionListener() {
            @Override
            public void onEditCategory(Category category) {
                navigateToCreateCategory(category);
            }

            @Override
            public void onCreateCategory() {
                navigateToCreateCategory(null);
            }

            @Override
            public void onDeleteCategory(Category category) {
                categoryViewModel.deleteCategory(category);
            }
        });

        binding.CategoryRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.CategoryRecycler.setAdapter(adapter);

        if (position == 0) {
            categoryViewModel.loadCategoryList(1);
        } else {
            categoryViewModel.loadCategoryList(0);
        }

        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> adapter.setCategories(categories));

        return binding.getRoot();
    }

    private void navigateToCreateCategory(@Nullable Category category) {
        NavController navController = Navigation.findNavController(binding.getRoot());
        Bundle args = new Bundle();
        if (category != null) {
            args.putInt("category_id", category.getId());
            args.putInt("type", category.getType());
            args.putString("category_name", category.getName());
            args.putString("hexcode", category.getHexCode());
        }
        navController.navigate(R.id.action_categoryFragment_to_createCategoryFragment, args);
    }
}
