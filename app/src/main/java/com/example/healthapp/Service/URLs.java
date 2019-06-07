package com.example.healthapp.Service;

import java.net.URI;

public class URLs {

    public static final String ROOT_URL = "http://www.diyabetlog.com/api";

    public static final String URL_REGISTER = ROOT_URL+ "/Kullanici/Register";
    public static final String URL_LOGIN= ROOT_URL + "/Kullanici/Login";
    public static final String URL_HBA1C_EKLE= ROOT_URL + "/HbA1C/HbA1CEkle";
    public static final String URL_HBA1C_LISTE= ROOT_URL + "/HbA1C/HbA1CGetir";
    public static final String URL_EGZERSIZ_EKLE= ROOT_URL + "/Egzersiz/EgzersizEkle";
    public static final String URL_EGZERSIZ_LISTE= ROOT_URL + "/Egzersiz/EgzersizGetir";
    public static final String URL_KANSEKERI_EKLE= ROOT_URL + "/KanSekeri/KanSekeriEkle";
    public static final String URL_KANSEKERI_LISTE= ROOT_URL + "/KanSekeri/KanSekeriGetir";
    public static final String URL_SU_EKLE= ROOT_URL + "/Su/SuEkle";
    public static final String URL_SU_LISTE= ROOT_URL + "/Su/SuGetir";
    public static final String URL_VKI_EKLE= ROOT_URL + "/VucutKitle/VucutKitleEkle";
    public static final String URL_VKI_LISTE= ROOT_URL + "/VucutKitle/VucutKitleGetir";
    public static final String URL_TANSIYON_EKLE= ROOT_URL + "/Tansiyon/TansiyonEkle";
    public static final String URL_TANSIYON_LISTE= ROOT_URL + "/Tansiyon/TansiyonGetir";
}
