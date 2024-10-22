package com.example.tfg_aplicaciongastos.ddbb.classes;

public class Account {

    int id;
    String name;
    double total;

    public Account(int id, double total, String name) {
        this.id = id;
        this.total = total;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
