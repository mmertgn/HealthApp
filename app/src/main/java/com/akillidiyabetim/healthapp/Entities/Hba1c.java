package com.akillidiyabetim.healthapp.Entities;

public class Hba1c {
    private String Tarih;
    private String Saat;
    private String Hba1c;
    private String Yorum;
    private int KullaniciId;
    public Hba1c() {

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

    public String getHba1c() {
        return Hba1c;
    }

    public void setHba1c(String hba1c) {
        Hba1c = hba1c;
    }

    public String getYorum() {
        return Yorum;
    }

    public void setYorum(String yorum) {
        Yorum = yorum;
    }

    public Hba1c(String tarih, String saat, String hba1c, String yorum) {
        Tarih = tarih;
        Saat = saat;
        Hba1c = hba1c;
        Yorum = yorum;
    }

    public int getKullaniciId() {
        return KullaniciId;
    }

    public void setKullaniciId(int kullaniciId) {
        KullaniciId = kullaniciId;
    }
}
