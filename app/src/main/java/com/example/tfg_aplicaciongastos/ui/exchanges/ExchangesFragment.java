package com.example.tfg_aplicaciongastos.ui.exchanges;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tfg_aplicaciongastos.R;
import com.example.tfg_aplicaciongastos.adapters.ExchangesListAdapter;
import com.example.tfg_aplicaciongastos.databinding.FragmentExchangesBinding;
import com.example.tfg_aplicaciongastos.ddbb.classes.Exchanges;
import com.example.tfg_aplicaciongastos.ddbb.classes.PieChartData;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @noinspection ALL
 */
public class ExchangesFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;
    private FragmentExchangesBinding binding;
    private ExchangesListAdapter adapter;
    private ExchangesViewModel exchangesViewModel;
    private PieChart pieChart;

    public static ExchangesFragment newInstance(int position) {
        ExchangesFragment fragment = new ExchangesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        exchangesViewModel = new ViewModelProvider(this).get(ExchangesViewModel.class);
        binding = FragmentExchangesBinding.inflate(inflater, container, false);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        Date endDate = calendar.getTime();


        pieChart = binding.pieChart;
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
        }

        adapter = new ExchangesListAdapter();
        adapter.setOnExchangeClickListener(this::navigateToCreateExchange);

        adapter.setOnExchangeLongClickListener(exchange -> showDeleteConfirmationDialog(requireContext(), exchange, startDate, endDate));

        binding.ExchangesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.ExchangesRecycler.setAdapter(adapter);

        if (position == 0) {
            exchangesViewModel.loadExchanges(requireContext(), 1, startDate, endDate);
            exchangesViewModel.loadPieChartData(requireContext(), 1, startDate, endDate);
        } else {
            exchangesViewModel.loadExchanges(requireContext(), 0, startDate, endDate);
            exchangesViewModel.loadPieChartData(requireContext(), 0, startDate, endDate);
        }

        exchangesViewModel.getExchanges().observe(getViewLifecycleOwner(), exchanges -> adapter.setExchanges(exchanges));

        binding.fabAddExchange.setOnClickListener(v -> navigateToCreateExchange(null));

        exchangesViewModel.getSelectedAccountTotal(requireContext());
        exchangesViewModel.getTotalLiveData().observe(getViewLifecycleOwner(), total -> {
            binding.TotalTextBox.setText(String.format("%.2f €", total));
        });

        exchangesViewModel.getExchanges().observe(getViewLifecycleOwner(), exchanges -> {
            if (exchanges != null) {
                adapter.setExchanges(exchanges);
            }
        });

        exchangesViewModel.getPieChartData().observe(getViewLifecycleOwner(), pieChartData -> {
            if (pieChartData != null) {
                updatePieChart(pieChartData);
            }
        });

        exchangesViewModel.getPieChartData().observe(getViewLifecycleOwner(), this::updatePieChart);

        binding.btnDay.setOnClickListener(v -> filterExchanges(0));
        binding.btnWeek.setOnClickListener(v -> filterExchanges(7));
        binding.btnMonth.setOnClickListener(v -> filterExchanges(30));
        binding.btnYear.setOnClickListener(v -> filterExchanges(365));
        binding.btnPeriod.setOnClickListener(v -> filterExchanges(-1));

        return binding.getRoot();
    }

    private void navigateToCreateExchange(@Nullable Exchanges exchange) {
        NavController navController = Navigation.findNavController(binding.getRoot());
        Bundle args = new Bundle();
        if (exchange != null) {
            args.putInt("exchange_id", exchange.getId());
            args.putInt("type", exchange.getType());
            args.putString("exchange_name", exchange.getName());
            args.putInt("category_id", exchange.getCategoryId());
            args.putDouble("quantity", exchange.getQuantity());
            args.putString("date", exchange.getDate());
        } else {
            args.putInt("type", position == 0 ? 1 : 0);
        }
        navController.navigate(R.id.action_exchangesFragment_to_createExchangeFragment, args);
    }

    private void showDeleteConfirmationDialog(Context context, Exchanges exchange, Date startDate, Date endDate) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.confirm_delete_exchange_title)
                .setMessage(R.string.confirm_delete_exchange_message)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    exchangesViewModel.deleteExchange(exchange, startDate, endDate);
                    updateAccountTotal(context, exchange);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .show();
    }

    @SuppressLint("DefaultLocale")
    private void updateAccountTotal(Context context, Exchanges exchange) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("account_preferences", Context.MODE_PRIVATE);
        int accountId = sharedPreferences.getInt("selected_account_id", -1);

        if (accountId != -1) {
            exchangesViewModel.updateAccountTotal(context, accountId, exchange);
            exchangesViewModel.getTotalLiveData().observe(getViewLifecycleOwner(), total -> {
                binding.TotalTextBox.setText(String.format("%.2f €", total));
            });

        }
    }

    @SuppressLint("DefaultLocale")
    private void updatePieChart(PieChartData chartData) {
        if (chartData == null || chartData.getData().isEmpty()) {
            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(1f, "Sin datos"));

            PieDataSet dataSet = new PieDataSet(entries, "");

            PieData pieData = new PieData(dataSet);
            pieData.setDrawValues(false);

            dataSet.setColor(Color.GRAY);

            pieChart.setData(pieData);
            pieChart.setCenterText("Sin datos");
            pieChart.setCenterTextSize(16f);
            pieChart.getLegend().setEnabled(false);
            pieChart.setDescription(null);
            pieChart.setDrawEntryLabels(false);
            pieChart.invalidate();
            return;
        }

        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        List<String> legendLabels = new ArrayList<>();

        double totalSum = chartData.getData().values().stream().mapToDouble(Double::doubleValue).sum();

        for (Map.Entry<String, Double> entry : chartData.getData().entrySet()) {
            float percentage = (float) (entry.getValue() / totalSum * 100);

            entries.add(new PieEntry(percentage, entry.getKey()));
            int colorIndex = colors.size() % chartData.getColors().size();
            colors.add(android.graphics.Color.parseColor(chartData.getColors().get(colorIndex)));

            legendLabels.add(String.format("%.1f%%", percentage));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(12f);

        pieChart.setUsePercentValues(true);
        pieChart.setDescription(null);
        pieChart.setDrawEntryLabels(false);

        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.getLegend().setXEntrySpace(10f);
        pieChart.getLegend().setYEntrySpace(10f);
        pieChart.getLegend().setTextColor(android.graphics.Color.WHITE);

        dataSet.setValueFormatter(new PercentFormatter());
        pieChart.setData(pieData);

        pieChart.setDrawCenterText(true);
        pieChart.setCenterText(String.format("%.2f €", totalSum));

        pieChart.invalidate();
    }

    public class PercentFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.format("%.1f%%", value);
        }
    }

    private void filterExchanges(int days) {
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();

        switch (days) {
            case 0:
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date startDate = calendar.getTime();
                loadExchangesData(startDate, endDate);
                break;

            case 7:
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date today = calendar.getTime();

                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                Date startOfWeek = calendar.getTime();

                if (today.before(startOfWeek)) {
                    calendar.add(Calendar.DAY_OF_YEAR, -7);
                    startOfWeek = calendar.getTime();
                }

                calendar.add(Calendar.DAY_OF_YEAR, 6);
                Date endOfWeek = calendar.getTime();
                loadExchangesData(startOfWeek, endOfWeek);
                break;

            case 30:
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startDate = calendar.getTime();

                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                endDate = calendar.getTime();
                loadExchangesData(startDate, endDate);
                break;

            case 365:
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startDate = calendar.getTime();

                calendar.set(Calendar.MONTH, Calendar.DECEMBER);
                calendar.set(Calendar.DAY_OF_MONTH, 31);
                endDate = calendar.getTime();
                loadExchangesData(startDate, endDate);
                break;

            case -1:
                showDateRangePicker();
                break;
        }
    }

    private void loadExchangesData(Date startDate, Date endDate) {
        if (position == 0) {
            exchangesViewModel.loadExchanges(requireContext(), 1, startDate, endDate);
            exchangesViewModel.loadPieChartData(requireContext(), 1, startDate, endDate);
        } else {
            exchangesViewModel.loadExchanges(requireContext(), 0, startDate, endDate);
            exchangesViewModel.loadPieChartData(requireContext(), 0, startDate, endDate);
        }
    }

    private void showDateRangePicker() {


        MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select a date range")
                .build();

        dateRangePicker.show(getParentFragmentManager(), dateRangePicker.toString());

        dateRangePicker.addOnPositiveButtonClickListener(selection -> {
            Long startDateMillis = selection.first;
            Long endDateMillis = selection.second;

            Date startDate = new Date(startDateMillis);
            Date endDate = new Date(endDateMillis);

            loadExchangesData(startDate, endDate);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}