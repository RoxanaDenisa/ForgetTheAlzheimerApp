package com.example.alzhapp;

public class Cutie {
    private int luni;
    private int marti;
    private int miercuri;
    private int joi;
    private int vineri;
    private int sambata;
    private int duminica;

    public Cutie(int luni, int marti, int miercuri, int joi, int vineri, int sambata, int duminica) {
        this.luni = luni;
        this.marti = marti;
        this.miercuri = miercuri;
        this.joi = joi;
        this.vineri = vineri;
        this.sambata = sambata;
        this.duminica = duminica;
    }
    public Cutie(){}

    public int getLuni() {
        return luni;
    }

    public int getMarti() {
        return marti;
    }

    public int getMiercuri() {
        return miercuri;
    }

    public int getJoi() {
        return joi;
    }

    public int getVineri() {
        return vineri;
    }

    public int getSambata() {
        return sambata;
    }

    public int getDuminica() {
        return duminica;
    }
}
