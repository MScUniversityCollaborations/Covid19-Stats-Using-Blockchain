package com.unipi.torpiles.models;

public class Country {
    String name;
    public int deaths;
    public int cases;

    @Override
    public String toString() {
        return name;
    }

    public Country(String name, int deaths, int cases) {
        this.name = name;
        this.deaths = deaths;
        this.cases = cases;
    }
}