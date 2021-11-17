package com.example.alzhapp;

public class Medicament {
    private String nume;
    private String ora;
    private String interv;
    private String uidPacient;
    public Medicament(String nume, String ora, String interv, String uidPacient) {
        this.nume = nume;
        this.ora = ora;
        this.interv = interv;
        this.uidPacient=uidPacient;
    }
    public Medicament(){}
    public String getNume() {
        return nume;
    }

    public String getOra() {
        return ora;
    }

    public String getInterv() {
        return interv;
    }

    public String getUidPacient() {
        return uidPacient;
    }
}
