package com.example.tfg_aplicaciongastos.ui.account;

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
import com.example.tfg_aplicaciongastos.adapters.AccountListAdapter;
import com.example.tfg_aplicaciongastos.ddbb.classes.Account;
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountContract;
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;

import java.util.ArrayList;
import java.util.List;


public class AccountFragment extends Fragment{
    private FragmentAccountBinding binding;
    private ArrayList<Account> accountsDataList;
    private RecyclerView accountsRecycler;
    private AccountDBHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializamos la base de datos y la lista de categorías
        dbHelper = new AccountDBHelper(getContext());
        accountsDataList = new ArrayList<>();

        // Configuramos el RecyclerView
        accountsRecycler = binding.getRoot().findViewById(R.id.AccountRecycler);
        accountsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Actualizamos los datos del RecyclerView
        UpdateAccountRecycler(root);  // Pasamos el "root" para el uso en NavController

        return root;
    }

    public void UpdateAccountRecycler(View root) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] ColumnList = {AccountContract.accountEntry._ID, AccountContract.accountEntry.NAME, AccountContract.accountEntry.TOTAL};
        String OrderBy = AccountContract.accountEntry._ID + " DESC";

        Cursor cursor = db.query(AccountContract.accountEntry.TABLE_NAME, ColumnList, null, null, null, null, OrderBy);

        List<Integer> idSelected = new ArrayList<>();
        List<String> nameSelected = new ArrayList<>();
        List<Double> totalSelected = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(AccountContract.accountEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(AccountContract.accountEntry.NAME));
            double total = cursor.getDouble(cursor.getColumnIndexOrThrow(AccountContract.accountEntry.TOTAL));

            idSelected.add(id);
            nameSelected.add(name);
            totalSelected.add(total);
        }
        cursor.close();

        // Creamos objetos de tipo Categories y los añadimos a la lista
        for (int i = 0; i < nameSelected.size(); i++) {
            accountsDataList.add(new Account(idSelected.get(i), nameSelected.get(i), totalSelected.get(i)));
        }

        accountsDataList.add(new Account(1,"prueba",100.16));
        // Configuramos el adaptador del RecyclerView
        AccountListAdapter adapter = new AccountListAdapter(accountsDataList, new AccountListAdapter.OnAddAccountClickListener() {
            @Override
            public void onAddAccountClick() {
                // Navegamos a CreateCategoryFragment al hacer clic en el botón de añadir
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_categoryFragment_to_createCategoryFragment);
            }
        });
        accountsRecycler.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
