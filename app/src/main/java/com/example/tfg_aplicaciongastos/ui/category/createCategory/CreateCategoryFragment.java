package com.example.tfg_aplicaciongastos.ui.category.createCategory;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.databinding.FragmentCreateCategoryBinding;
import com.example.tfg_aplicaciongastos.ddbb.classes.Category;
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;
import com.github.dhaval2404.colorpicker.ColorPickerDialog;

import java.util.concurrent.Executor;


public class CreateCategoryFragment extends Fragment {
    private FragmentCreateCategoryBinding binding;
    private CreateCategoryViewModel mViewModel;
    private int editingCategoryId = -1;
    private String selectedHexCode = "#FFFFFF";
    private boolean isExpenseType = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * @noinspection deprecation
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AccountDBHelper dbHelper = new AccountDBHelper(requireContext());
        Executor executor = Runnable::run;

        CreateCategoryViewModelFactory factory = new CreateCategoryViewModelFactory(executor, dbHelper);
        mViewModel = new ViewModelProvider(this, factory).get(CreateCategoryViewModel.class);

        Bundle args = getArguments();
        if (args != null) {
            editingCategoryId = args.getInt("category_id", -1);
            String categoryName = args.getString("category_name", "");
            selectedHexCode = args.getString("hexcode", "#FFFFFF");

            binding.categoryNameEditText.setText(categoryName);
            binding.colorDisplayView.setBackgroundColor(Color.parseColor(selectedHexCode));
        }

        binding.radioButtonGroupCategoryType.setOnCheckedChangeListener((group, checkedId) -> isExpenseType = checkedId == R.id.radioButtonGastos);

        binding.colorPickerButton.setOnClickListener(v -> new ColorPickerDialog.Builder(requireContext())
                .setTitle("Selecciona un color")
                .setDefaultColor(R.color.white)
                .setColorListener((color, colorHex) -> {
                    selectedHexCode = colorHex;
                    binding.colorDisplayView.setBackgroundColor(color);
                })
                .show());

        binding.createCategoryButton.setOnClickListener(v -> {
            String name = binding.categoryNameEditText.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            Category category = new Category(
                    editingCategoryId,
                    isExpenseType ? 1 : 0,
                    name,
                    selectedHexCode
            );

            if (editingCategoryId == -1) {
                mViewModel.addCategory(category);
                Toast.makeText(requireContext(), "Categoría creada", Toast.LENGTH_SHORT).show();
            } else {
                mViewModel.updateCategory(category);
                Toast.makeText(requireContext(), "Categoría actualizada", Toast.LENGTH_SHORT).show();
            }

            requireActivity().onBackPressed();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
