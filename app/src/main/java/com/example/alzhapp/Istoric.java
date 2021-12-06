package com.example.alzhapp;

import java.util.Comparator;

public class Istoric {
    private String nume;
    private String ora;

    public Istoric(String nume, String ora) {
        this.nume = nume;
        this.ora = ora;
    }

    public String getNume() {
        return nume;
    }

    public String getOra() {
        return ora;
    }
    public int getOraInt(){
        return Integer.valueOf(ora);
    }
    public static Comparator<Istoric> ordonare = new Comparator<Istoric>() {

        public int compare(Istoric i1, Istoric i2) {

            int o1 = i1.getOraInt();
            int o2 = i2.getOraInt();

            /*Pentru ordonare crescatoare*/
            return o1-o2;

        }};
}
