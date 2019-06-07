package com.example.healthapp.Entities;

public class Tansiyon {
    public Tansiyon(String tarih, String saat, String buyukTansiyon, String kucukTansiyon) {
        Tarih = tarih;
        Saat = saat;
        BuyukTansiyon = buyukTansiyon;
        KucukTansiyon = kucukTansiyon;
    }

    public Tansiyon() {

    }

    public String getTarih() {
        return Tarih;
    }

    public void setTarih(String tarih) {
        Tarih = tarih;
    }

    public String getSaat() {
        return Saat;
    }

    public void setSaat(String saat) {
        Saat = saat;
    }

    public String getBuyukTansiyon() {
        return BuyukTansiyon;
    }

    public void setBuyukTansiyon(String buyukTansiyon) {
        BuyukTansiyon = buyukTansiyon;
    }

    public String getKucukTansiyon() {
        return KucukTansiyon;
    }

    public void setKucukTansiyon(String kucukTansiyon) {
        KucukTansiyon = kucukTansiyon;
    }

    private String Tarih;
    private String Saat;
    private String BuyukTansiyon;
    private String KucukTansiyon;
    private int KullaniciId;

    public int getKullaniciId() {
        return KullaniciId;
    }

    public void setKullaniciId(int kullaniciId) {
        KullaniciId = kullaniciId;
    }
}
