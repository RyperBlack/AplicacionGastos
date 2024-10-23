package com.example.tfg_aplicaciongastos.ddbb.classes;

public class Exchanges {

    int id, accountId, categoryId;
    String name, type;
    double quantity;

    public Exchanges(int id, int accountId, String name, int categoryId, String type, double quantity) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.categoryId = categoryId;
        this.type = type;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
