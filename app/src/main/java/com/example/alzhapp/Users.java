package com.example.alzhapp;
public class Users {
    private String numeComplet;
    private String adresa;
    private String telefon;
    private String numeSupraveghetor;
    private String telefonSupraveghetor;
    private String adresaMail;
    private String parola;
    private String codDoctor;
    public Users(String numeComplet, String adresa, String telefon, String numeSupraveghetor, String telefonSupraveghetor, String adresaMail, String parola, String codDoctor){
        this.numeComplet=numeComplet;
        this.adresa=adresa;
        this.telefon=telefon;
        this.numeSupraveghetor=numeSupraveghetor;
        this.telefonSupraveghetor=telefonSupraveghetor;
        this.adresaMail=adresaMail;
        this.parola=parola;
        this.codDoctor=codDoctor;
    }

    public String getNumeComplet() {
        return numeComplet;
    }

    public String getAdresa() {
        return adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getNumeSupraveghetor() {
        return numeSupraveghetor;
    }

    public String getTelefonSupraveghetor() {
        return telefonSupraveghetor;
    }

    public String getAdresaMail() {
        return adresaMail;
    }

    public String getParola() {
        return parola;
    }

    public String getCodDoctor() {
        return codDoctor;
    }
}