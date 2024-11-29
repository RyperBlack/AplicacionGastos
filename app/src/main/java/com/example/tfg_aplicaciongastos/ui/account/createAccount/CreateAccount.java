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
import com.example.tfg_aplicaciongastos.ddbb.helpers.AccountDBHelper;

import java.util.concurrent.Executor;

/** @noinspection ALL*/
public class CreateAccount extends Fragment {

    private ActivityCreateAccountBinding binding;
    private CreateAccountViewModel mViewModel;
    private int accountId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityCreateAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /** @noinspection deprecation*/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AccountDBHelper dbHelper = new AccountDBHelper(requireContext());
        Executor executor = Runnable::run;

        CreateAccountViewModelFactory factory = new CreateAccountViewModelFactory(executor, dbHelper);
        mViewModel = new ViewModelProvider(this, factory).get(CreateAccountViewModel.class);

        Bundle args = getArguments();
        if (args != null) {
            accountId = args.getInt("account_id", -1);
            String accountName = args.getString("account_name", "");
            double accountTotal = args.getDouble("account_total", 0.0);

            binding.accountNameEditText.setText(accountName);
            binding.accountTotalEditText.setText(String.valueOf(accountTotal));
        }

        binding.createAccountButton.setOnClickListener(v -> {
            String name = binding.accountNameEditText.getText().toString();
            String totalText = binding.accountTotalEditText.getText().toString();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(totalText)) {
                Toast.makeText(requireContext(), R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            double total = Double.parseDouble(totalText);
            Account account = new Account(accountId, name, total);

            if (accountId == -1) {
                mViewModel.addAccount(account);
                Toast.makeText(requireContext(), R.string.account_created, Toast.LENGTH_SHORT).show();
            } else {
                mViewModel.updateAccount(account);
                Toast.makeText(requireContext(), R.string.account_updated, Toast.LENGTH_SHORT).show();
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
