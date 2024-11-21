package com.example.tfg_aplicaciongastos.ui.account.createAccount;

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

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.databinding.ActivityCreateAccountBinding;
import com.example.tfg_aplicaciongastos.ddbb.classes.Account;

public class CreateAccount extends Fragment {

    private ActivityCreateAccountBinding binding;
    private CreateAccountViewModel mViewModel;
    private int accountId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ActivityCreateAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(CreateAccountViewModel.class);

        // Obtener argumentos
        Bundle args = getArguments();
        if (args != null) {
            accountId = args.getInt("account_id", -1);
            String accountName = args.getString("account_name", "");
            double accountTotal = args.getDouble("account_total", 0.0);

            // Rellenar campos si hay datos
            binding.accountNameEditText.setText(accountName);
            binding.accountTotalEditText.setText(String.valueOf(accountTotal));
        }

        // Configurar el botón de creación/edición
        binding.createAccountButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.accountNameEditText.getText()) ||
                    TextUtils.isEmpty(binding.accountTotalEditText.getText())) {
                Toast.makeText(requireContext(), R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            String name = binding.accountNameEditText.getText().toString();
            double total = Double.parseDouble(binding.accountTotalEditText.getText().toString());

            Account account = new Account(accountId, name, total);


            Toast.makeText(requireContext(), R.string.account_saved, Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
