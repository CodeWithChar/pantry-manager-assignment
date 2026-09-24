package com.example.smartpantrymanager;

//this is just the structure of how each recipe will be laid out
public class Recipe {

    private long id;
    private String name;
    private String steps;

    public Recipe(long id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getSteps() { return steps; }
}