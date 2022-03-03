package com.unipi.torpiles.models;

public class Records {

    private final int cases;
    private final String country;
    private final String dateRep;
    private final String day;
    private final int deaths;
    private final String month;
    private final String year;

    public Records(int cases, String country, String dateRep, String day, int deaths, String month, String year) {
        this.cases = cases;
        this.country = country;
        this.dateRep = dateRep;
        this.day = day;
        this.deaths = deaths;
        this.month = month;
        this.year = year;
    }

    public int getCases() {
        return cases;
    }

    public String getCountry() {
        return country;
    }

    public String getDateRep() {
        return dateRep;
    }

    public String getDay() {
        return day;
    }

    public int getDeaths() {
        return deaths;
    }

    public String getMonth() {
        return month.replaceAll("^[0]+", "");
    }

    public String getYear() {
        return year;
    }


    @Override
    public String toString() {
        return "Records [Country=" + country + "]";
    }
}



