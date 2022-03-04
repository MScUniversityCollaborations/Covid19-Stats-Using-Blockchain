package com.unipi.torpiles.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Statistic {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public String location;
    public Integer confirmed;
    public Integer deaths;
    public Integer recovered;
    public Integer active;
    public Long ts;

    public Statistic(String location, Integer confirmed, Integer deaths, Integer recovered, Integer active, Long ts) {
        this.location = location;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.active = active;
        this.ts = ts;
    }

    public Statistic(String location, Integer confirmed, Integer deaths, Integer recovered, Integer active) {
        this.location = location;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.active = active;
    }

    public String jsonMaker()
    {
        return GSON.toJson(this);
    }

    public String getLocation() {
        return location;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public Integer getRecovered() {
        return recovered;
    }

    public Integer getActive() {
        return active;
    }

    public Long getTs() {
        return ts;
    }
}
