package com.example.tfg_aplicaciongastos.ddbb.classes;

public class Account {

    int id;
    String name, hexCode;
    double total;

    public Account(int id, double total, String name, String hexCode) {
        this.id = id;
        this.total = total;
        this.name = name;
        this.hexCode = hexCode;
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

    public String getHexCode(String hexCode) { return hexCode; }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }
}
