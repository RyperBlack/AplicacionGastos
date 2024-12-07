package com.example.tfg_aplicaciongastos.ui.exchanges.createExchange;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.adapters.CategorySelectListAdapter;
import com.example.tfg_aplicaciongastos.databinding.CreateExchangeFragmentBinding;
import com.example.tfg_aplicaciongastos.ddbb.classes.Category;
import com.example.tfg_aplicaciongastos.ddbb.classes.Exchanges;
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;

public class CreateExchangeFragment extends Fragment {

    private CreateExchangeFragmentBinding binding;
    private CreateExchangeViewModel mViewModel;

    private int editingExchangeId = -1; // Si estamos editando
    private int categoryId = -1;       // Categoría seleccionada
    private int isExpenseType = -1;    // 1 = Gastos, 0 = Ingresos

    private CategorySelectListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CreateExchangeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configuración inicial
        AccountDBHelper dbHelper = new AccountDBHelper(requireContext());
        Executor executor = Runnable::run;
        CreateExchangeViewModelFactory factory = new CreateExchangeViewModelFactory(executor, dbHelper);
        mViewModel = new ViewModelProvider(this, factory).get(CreateExchangeViewModel.class);

        adapter = new CategorySelectListAdapter(category -> categoryId = category.getId());
        binding.CategoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.CategoryRecycler.setAdapter(adapter);

        // Configurar acciones
        binding.datePickerButton.setOnClickListener(v -> openDatePicker());
        binding.radioButtonGastos.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isExpenseType = 1;
                loadCategories(dbHelper);
            }
        });

        binding.radioButtonIngresos.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isExpenseType = 0;
                loadCategories(dbHelper);
            }
        });

        binding.createExchangeButton.setOnClickListener(v -> saveExchange(dbHelper));

        loadCategories(dbHelper);
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            binding.exchangeDateEditText.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void loadCategories(AccountDBHelper dbHelper) {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = "type = ?";
        String[] selectionArgs = new String[]{String.valueOf(isExpenseType)};

        Cursor cursor = db.query("category", new String[]{"_id", "type", "name", "hexcode"},
                selection, selectionArgs, null, null, "_id DESC");

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int type = cursor.getInt(1);
            String name = cursor.getString(2);
            String hexcode = cursor.getString(3);

            categories.add(new Category(id, type, name, hexcode));
        }

        cursor.close();
        db.close();

        adapter.setCategories(categories);
        adapter.setSelectedCategoryId(categoryId);
    }

    private void saveExchange(AccountDBHelper dbHelper) {
        String name = binding.exchangeNameEditText.getText().toString().trim();
        String date = binding.exchangeDateEditText.getText().toString().trim();
        String totalAmountString = binding.totalAmountEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(date) || categoryId == -1 || TextUtils.isEmpty(totalAmountString)) {
            Toast.makeText(requireContext(), "Por favor, rellene todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalAmount;
        try {
            totalAmount = Double.parseDouble(totalAmountString);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Cantidad inválida.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("account_preferences", Context.MODE_PRIVATE);
        int accountId = sharedPreferences.getInt("selected_account_id", -1);

        if (accountId == -1) {
            Toast.makeText(requireContext(), "No se encontró el ID de la cuenta.", Toast.LENGTH_SHORT).show();
            return;
        }

        Exchanges exchange = new Exchanges(
                editingExchangeId, accountId, name, categoryId, isExpenseType, totalAmount, date, null, null
        );

        if (editingExchangeId == -1) {
            mViewModel.addExchange(exchange);
            Toast.makeText(requireContext(), "Intercambio creado.", Toast.LENGTH_SHORT).show();
        } else {
            mViewModel.updateExchange(exchange);
            Toast.makeText(requireContext(), "Intercambio actualizado.", Toast.LENGTH_SHORT).show();
        }
        requireActivity().onBackPressed();
    }
}
