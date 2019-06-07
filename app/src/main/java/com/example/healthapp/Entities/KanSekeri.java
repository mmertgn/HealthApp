package com.example.healthapp.Entities;

public class KanSekeri {
    public KanSekeri(String tarih, String zamanDilimi, String kanSekeriDegeri) {
        Tarih = tarih;
        ZamanDilimi = zamanDilimi;
        KanSekeriDegeri = kanSekeriDegeri;
    }

    private String Tarih;
    private String ZamanDilimi;
    private String KanSekeriDegeri;

    public KanSekeri() {

    }

    public String getTarih() {
        return Tarih;
    }

    public void setTarih(String tarih) {
        Tarih = tarih;
    }

    public String getZamanDilimi() {
        return ZamanDilimi;
    }

    public void setZamanDilimi(String zamanDilimi) {
        ZamanDilimi = zamanDilimi;
    }

    public String getKanSekeriDegeri() {
        return KanSekeriDegeri;
    }

    public void setKanSekeriDegeri(String kanSekeriDegeri) {
        KanSekeriDegeri = kanSekeriDegeri;
    }

}
