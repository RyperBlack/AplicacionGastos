package com.example.tfg_aplicaciongastos.ddbb.classes;

import java.util.List;
import java.util.Map;

public class PieChartData {
    private final Map<String, Double> data;
    private final List<String> colors;

    public PieChartData(Map<String, Double> data, List<String> colors) {
        this.data = data;
        this.colors = colors;
    }

    public Map<String, Double> getData() {
        return data;
    }

    public List<String> getColors() {
        return colors;
    }
}
