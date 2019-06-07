package com.example.healthapp.Entities;

public class VkiEndeksi {
    private String Tarih;
    private String Saat;
    private String Boy;
    private String Kilo;
    private String VkiEndeksi;
    private String VkiDurumu;
    private int KullaniciId;
    public VkiEndeksi() {

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

    public String getBoy() {
        return Boy;
    }

    public void setBoy(String boy) {
        Boy = boy;
    }

    public String getKilo() {
        return Kilo;
    }

    public void setKilo(String kilo) {
        Kilo = kilo;
    }

    public VkiEndeksi(String tarih, String saat, String boy, String kilo) {
        Tarih = tarih;
        Saat = saat;
        Boy = boy;
        Kilo = kilo;
    }

    public String getVkiEndeksi() {
        return VkiEndeksi;
    }

    public void setVkiEndeksi(String vkiEndeksi) {
        VkiEndeksi = vkiEndeksi;
    }

    public String getVkiDurumu() {
        return VkiDurumu;
    }

    public void setVkiDurumu(String vkiDurumu) {
        VkiDurumu = vkiDurumu;
    }

    public int getKullaniciId() {
        return KullaniciId;
    }

    public void setKullaniciId(int kullaniciId) {
        KullaniciId = kullaniciId;
    }
}
