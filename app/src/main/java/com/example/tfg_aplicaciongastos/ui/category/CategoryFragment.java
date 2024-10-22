package com.example.tfg_aplicaciongastos.ui.category;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.adapters.CategoryListAdapter;
import com.example.tfg_aplicaciongastos.databinding.FragmentCategoryBinding;
import com.example.tfg_aplicaciongastos.ddbb.classes.Categories;
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountContract;
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;
import com.example.tfg_aplicaciongastos.ui.category.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;
    private ArrayList<Categories> categoriesDataList;
    private RecyclerView categoriesRecycler;
    private AccountDBHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializamos la base de datos y la lista de categorías
        dbHelper = new AccountDBHelper(getContext());
        categoriesDataList = new ArrayList<>();

        // Configuramos el RecyclerView
        categoriesRecycler = binding.getRoot().findViewById(R.id.CategoryRecycler);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Actualizamos los datos del RecyclerView
        UpdateCategoryRecycler(root);  // Pasamos el "root" para el uso en NavController

        return root;
    }

    public void UpdateCategoryRecycler(View root) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] ColumnList = {AccountContract.categoryEntry._ID, AccountContract.categoryEntry.ACCOUNT_ID, AccountContract.categoryEntry.NAME, AccountContract.categoryEntry.HEXCODE};
        String OrderBy = AccountContract.categoryEntry._ID + " DESC";

        Cursor cursor = db.query(AccountContract.categoryEntry.TABLE_NAME, ColumnList, null, null, null, null, OrderBy);

        List<Integer> idSelected = new ArrayList<>();
        List<Integer> accountIDSelected = new ArrayList<>();
        List<String> nameSelected = new ArrayList<>();
        List<String> hexcodeSelected = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(AccountContract.categoryEntry._ID));
            int accountId = cursor.getInt(cursor.getColumnIndexOrThrow(AccountContract.categoryEntry.ACCOUNT_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(AccountContract.categoryEntry.NAME));
            String hexcode = cursor.getString(cursor.getColumnIndexOrThrow(AccountContract.categoryEntry.HEXCODE));

            idSelected.add(id);
            accountIDSelected.add(accountId);
            nameSelected.add(name);
            hexcodeSelected.add(hexcode);
        }
        cursor.close();

        // Creamos objetos de tipo Categories y los añadimos a la lista
        for (int i = 0; i < nameSelected.size(); i++) {
            categoriesDataList.add(new Categories(idSelected.get(i), accountIDSelected.get(i), nameSelected.get(i), hexcodeSelected.get(i)));
        }

        // Configuramos el adaptador del RecyclerView
        CategoryListAdapter adapter = new CategoryListAdapter(categoriesDataList, new CategoryListAdapter.OnAddCategoryClickListener() {
            @Override
            public void onAddCategoryClick() {
                // Navegamos a CreateCategoryFragment al hacer clic en el botón de añadir
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_categoryFragment_to_createCategoryFragment);
            }
        });
        categoriesRecycler.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}