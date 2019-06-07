package com.example.healthapp.Entities;

public class Egzersiz {
    private String Tarih;
private int KullaniciId;
    public Egzersiz() {

    }

    public String getTarih() {
        return Tarih;
    }

    public void setTarih(String tarih) {
        Tarih = tarih;
    }

    public String getEgzersizTipi() {
        return EgzersizTipi;
    }

    public void setEgzersizTipi(String egzersizTipi) {
        EgzersizTipi = egzersizTipi;
    }

    public String getSure() {
        return Sure;
    }

    public void setSure(String sure) {
        Sure = sure;
    }

    public Egzersiz(String tarih, String egzersizTipi, String sure) {
        Tarih = tarih;
        EgzersizTipi = egzersizTipi;
        Sure = sure;
    }

    private String EgzersizTipi;
    private String Sure;

    public int getKullaniciId() {
        return KullaniciId;
    }

    public void setKullaniciId(int kullaniciId) {
        KullaniciId = kullaniciId;
    }
}
