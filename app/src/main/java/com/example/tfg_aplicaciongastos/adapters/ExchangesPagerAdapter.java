package com.example.tfg_aplicaciongastos.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tfg_aplicaciongastos.ui.exchanges.ExchangesFragment;

public class ExchangesPagerAdapter extends FragmentStateAdapter {

    public ExchangesPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ExchangesFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
