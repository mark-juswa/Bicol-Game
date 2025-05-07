package com.example.anobayanbicol;

public class CategoryModel {
    private String id;
    private String name;
    private int noOfTests;

    public CategoryModel(String id, String name, int noOfTests) {
        this.id = id;
        this.name = name;
        this.noOfTests = noOfTests;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNoOfTests() {
        return noOfTests;
    }
}
