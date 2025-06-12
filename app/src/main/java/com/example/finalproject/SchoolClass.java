package com.example.finalproject;

public class SchoolClass {
    private int id;
    private String name;

    public SchoolClass(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    @Override
    public String toString() {
        return name;
    }
}

