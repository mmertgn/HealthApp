package com.akillidiyabetim.healthapp.Entities;

public class SuTakibi {
    public SuTakibi(int gunlukToplamBardak, String tarih) {
        GunlukToplamBardak = gunlukToplamBardak;
        Tarih = tarih;
    }

    private int GunlukToplamBardak;

    public SuTakibi() {

    }

    public int getGunlukToplamBardak() {
        return GunlukToplamBardak;
    }

    public void setGunlukToplamBardak(int gunlukToplamBardak) {
        GunlukToplamBardak = gunlukToplamBardak;
    }

    public String getTarih() {
        return Tarih;
    }

    public void setTarih(String tarih) {
        Tarih = tarih;
    }

    private String Tarih;
    private int KullaniciId;

    public int getKullaniciId() {
        return KullaniciId;
    }

    public void setKullaniciId(int kullaniciId) {
        KullaniciId = kullaniciId;
    }
}
