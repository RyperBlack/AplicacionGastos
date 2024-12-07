package com.example.tfg_aplicaciongastos.ui.exchanges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.adapters.ExchangesPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ExchangesTabsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tabs, container, false);

        ViewPager2 viewPager = root.findViewById(R.id.viewPager);
        TabLayout tabLayout = root.findViewById(R.id.tabLayout);

        ExchangesPagerAdapter adapter = new ExchangesPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Gastos");
            } else {
                tab.setText("Ingresos");
            }
        }).attach();

        return root;
    }
}
