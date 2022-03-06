package com.unipi.torpiles.models;

public class RecordForStats {

    private final int month;
    private final int cases;
    private final int deaths;
    private final String country;

    public RecordForStats(String country, int cases , int deaths, int month) {
        this.country = country;
        this.deaths = deaths;
        this.cases = cases;
        this.month = month;
    }

    public int getCases() {
        return cases;
    }

    public String getCountry() {
        return country;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getMonth() {
        return month;
    }

    @Override
    public String toString() {
        return country;
    }

}
