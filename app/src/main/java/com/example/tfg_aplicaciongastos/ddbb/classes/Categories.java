package com.example.tfg_aplicaciongastos.ddbb.classes;

public class Categories {

    int id, accountId;
    String name, hexCode;

    public Categories(int id, int accountId, String name, String hexCode) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.hexCode = hexCode;
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

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}