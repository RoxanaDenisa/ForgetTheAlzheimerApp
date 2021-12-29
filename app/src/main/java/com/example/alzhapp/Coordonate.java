package com.example.alzhapp;

public class Coordonate {
    private double lat;
    private double lgn;
    public Coordonate(){}
    public Coordonate(double lat, double lgn) {
        this.lat = lat;
        this.lgn = lgn;
    }

    public double getLat() {
        return lat;
    }

    public double getLgn() {
        return lgn;
    }
}
