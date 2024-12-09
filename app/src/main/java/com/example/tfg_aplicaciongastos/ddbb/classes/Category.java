package com.example.tfg_aplicaciongastos.ddbb.classes;

public class Category {

    int id, type;
    String name, hexCode;

    public Category(int id, int type, String name, String hexCode) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.hexCode = hexCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHexCode() {
        return hexCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}