package com.example.tfg_aplicaciongastos.ddbb.classes;

public class Exchanges {

    int id, accountId, categoryId, type;
    String name, date, categoryName, categoryColor;
    double quantity;

    public Exchanges(int id, int accountId, String name, int categoryId, int type, double quantity, String date, String categoryName, String categoryColor) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.categoryId = categoryId;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public double getQuantity() {
        return quantity;
    }

}
