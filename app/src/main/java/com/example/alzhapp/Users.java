package com.example.alzhapp;
public class Users {
    private String numeComplet;
    private String adresa;
    private String telefon;
    private String numeSupraveghetor;
    private String telefonSupraveghetor;
    private String adresaMail;
    private String parola;
    private String mailDoctor;
    private String tipUtilizator;
    private String uid;
    private boolean medicament_neluat;
    public Users(String uid,String numeComplet, String adresa, String telefon, String numeSupraveghetor, String telefonSupraveghetor, String adresaMail, String parola, String mailDoctor,String tipUtilizator, boolean medicament_neluat){
        this.numeComplet=numeComplet;
        this.adresa=adresa;
        this.telefon=telefon;
        this.numeSupraveghetor=numeSupraveghetor;
        this.telefonSupraveghetor=telefonSupraveghetor;
        this.adresaMail=adresaMail;
        this.parola=parola;
        this.mailDoctor=mailDoctor;
        this.tipUtilizator=tipUtilizator;
        this.uid=uid;
        this.medicament_neluat=medicament_neluat;
    }
    public Users(){}



    public Users(String numeComplet, String adresaMail, String parola, String telefon, String adresa, String tipUtilizator){
        this.numeComplet=numeComplet;
        this.adresa=adresa;
        this.telefon=telefon;
        this.adresaMail=adresaMail;
        this.parola=parola;
        this.tipUtilizator=tipUtilizator;
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
    public boolean getMedicament_neluat(){return medicament_neluat;}

    public String getUid() {
        return uid;
    }
    public String getMailDoctor() {
        return mailDoctor;
    }
    public String getTipUtilizator() {
        return tipUtilizator;
    }
    public void setMedicament_neluat(boolean medicament_neluat) {
        this.medicament_neluat = medicament_neluat;
    }
}