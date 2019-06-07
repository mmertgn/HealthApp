package com.example.healthapp.Entities;

public class Kullanici {
    public Kullanici(){

    }
    public Kullanici(int id, String adi, String soyadi, String email, String dogumTarihi, String diyabetTipi, String teshisKondoguTarih, String il, String ilce,String cinsiyet) {
        Id = id;
        Adi = adi;
        Soyadi = soyadi;
        Email = email;
        DogumTarihi = dogumTarihi;
        DiyabetTipi = diyabetTipi;
        TeshisKondoguTarih = teshisKondoguTarih;
        Il = il;
        Ilce = ilce;
        Cinsiyet = cinsiyet;
    }

    private int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getAdi() {
        return Adi;
    }

    public void setAdi(String adi) {
        Adi = adi;
    }

    public String getSoyadi() {
        return Soyadi;
    }

    public void setSoyadi(String soyadi) {
        Soyadi = soyadi;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSifre() {
        return Sifre;
    }

    public void setSifre(String sifre) {
        Sifre = sifre;
    }

    public String getSifreTekrar() {
        return SifreTekrar;
    }

    public void setSifreTekrar(String sifreTekrar) {
        SifreTekrar = sifreTekrar;
    }

    public String getDogumTarihi() {
        return DogumTarihi;
    }

    public void setDogumTarihi(String dogumTarihi) {
        DogumTarihi = dogumTarihi;
    }

    public String getDiyabetTipi() {
        return DiyabetTipi;
    }

    public void setDiyabetTipi(String diyabetTipi) {
        DiyabetTipi = diyabetTipi;
    }

    public String getTeshisKondoguTarih() {
        return TeshisKondoguTarih;
    }

    public void setTeshisKondoguTarih(String teshisKondoguTarih) {
        TeshisKondoguTarih = teshisKondoguTarih;
    }

    public String getIl() {
        return Il;
    }

    public void setIl(String il) {
        Il = il;
    }

    public String getIlce() {
        return Ilce;
    }

    public void setIlce(String ilce) {
        Ilce = ilce;
    }

    public String getOlusturmaTarihi() {
        return OlusturmaTarihi;
    }

    public void setOlusturmaTarihi(String olusturmaTarihi) {
        OlusturmaTarihi = olusturmaTarihi;
    }

    private String Adi;
    private String Soyadi;
    private String Email;
    private String Sifre;
    private String SifreTekrar;
    private String DogumTarihi;
    private String DiyabetTipi;
    private String TeshisKondoguTarih;
    private String Il;
    private String Ilce;
    private String OlusturmaTarihi;

    public String getCinsiyet() {
        return Cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        Cinsiyet = cinsiyet;
    }

    private String Cinsiyet;
}
