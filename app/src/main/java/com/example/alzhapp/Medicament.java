package com.example.alzhapp;

public class Medicament {
    private String nume;
    private String ora;
    private String interv;

    public Medicament(String nume, String ora, String interv) {
        this.nume = nume;
        this.ora = ora;
        this.interv = interv;
    }

    public String getNume() {
        return nume;
    }

    public String getOra() {
        return ora;
    }

    public String getInterv() {
        return interv;
    }
}
