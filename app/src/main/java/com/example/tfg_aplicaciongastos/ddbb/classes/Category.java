package com.example.tfg_aplicaciongastos.ddbb.classes;

public class Category {

    int id;
    boolean type;
    String name, hexCode;

    public Category(int id,boolean type, String name, String hexCode) {
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

    public boolean getType(){
        return type;
    }

    public void setType(boolean type){
        this.type = type;
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