package com.example.tfg_aplicaciongastos.ui.category.createCategory;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.databinding.FragmentCreateCategoryBinding;
import com.example.tfg_aplicaciongastos.ddbb.classes.Category;
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;
import com.example.tfg_aplicaciongastos.ui.category.createCategory.CreateCategoryViewModel;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.ColorPickerDialog;

public class CreateCategoryFragment extends Fragment {
    private FragmentCreateCategoryBinding binding;
    private CreateCategoryViewModel mViewModel;
    private int categoryId = -1;
    private String selectedHexCode = "#FFFFFF"; // Default color (white)
    private boolean categoryType = true; // Default category type

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AccountDBHelper dbHelper = new AccountDBHelper(requireContext());

        // ViewModel initialization
        mViewModel = new ViewModelProvider(this).get(CreateCategoryViewModel.class);

        // Initialize category if data passed from CategoryFragment
        Bundle args = getArguments();
        if (args != null) {
            categoryId = args.getInt("category_id", -1);
            String categoryName = args.getString("category_name", "");
            selectedHexCode = args.getString("hexcode", "#FFFFFF");

            binding.categoryNameEditText.setText(categoryName);
            binding.colorDisplayView.setBackgroundColor(android.graphics.Color.parseColor(selectedHexCode));
        }

        // Handle radio button change for category type (expenses or income)
        binding.radioButtonGroupCategoryType.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = group.findViewById(checkedId);
            categoryType = selectedRadioButton.getText().toString().equals(getString(R.string.gastos));
        });

        // Open color picker when color display view is clicked
        binding.colorPickerButton.setOnClickListener(v -> {
            ColorPickerDialog.newBuilder()
                    .setAllowCustom(true)
                    .setShowAlphaSlider(false)
                    .setColor(android.graphics.Color.parseColor(selectedHexCode))
                    .setPresets(new int[]{android.graphics.Color.RED, android.graphics.Color.GREEN, android.graphics.Color.BLUE})
                    .setDialogId(0)
                    .setColor(Color.BLACK)
                    .setAlphaSliderVisible(true)
                    .setPresets(new int[] { android.graphics.Color.BLACK, android.graphics.Color.WHITE, android.graphics.Color.RED })
                    .setAllowCustom(true)
                    .setShowAlphaSlider(false)
                    .setColor(android.graphics.Color.parseColor(selectedHexCode))
                    .setAllowCustom(true)
                    .setShowAlphaSlider(false)
                    .setAllowCustom(true)
                    .setColor(android.graphics.Color.parseColor("#FF0000"))
                    .setListener(new OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int color) {
                            selectedHexCode = String.format("#%06X", (0xFFFFFF & color));
                            binding.colorDisplayView.setBackgroundColor(color);
                        }
                    })
                    .build()
                    .show(getFragmentManager(), "color_dialog");
        });

        // Create or update category when button clicked
        binding.createCategoryButton.setOnClickListener(v -> {
            String categoryName = binding.categoryNameEditText.getText().toString();

            if (TextUtils.isEmpty(categoryName)) {
                Toast.makeText(requireContext(), R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            Category category = new Category(categoryId, categoryType, categoryName, selectedHexCode);

            if (categoryId == -1) {
                mViewModel.addCategory(category);
                Toast.makeText(requireContext(), R.string.category_created, Toast.LENGTH_SHORT).show();
            } else {
                mViewModel.updateCategory(category);
                Toast.makeText(requireContext(), R.string.category_updated, Toast.LENGTH_SHORT).show();
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
