package com.example.healthapp.Entities;

public class SuTakibi {
    public SuTakibi(String gunlukToplamBardak, String tarih) {
        GunlukToplamBardak = gunlukToplamBardak;
        Tarih = tarih;
    }

    private String GunlukToplamBardak;

    public SuTakibi() {

    }

    public String getGunlukToplamBardak() {
        return GunlukToplamBardak;
    }

    public void setGunlukToplamBardak(String gunlukToplamBardak) {
        GunlukToplamBardak = gunlukToplamBardak;
    }

    public String getTarih() {
        return Tarih;
    }

    public void setTarih(String tarih) {
        Tarih = tarih;
    }

    private String Tarih;
}
