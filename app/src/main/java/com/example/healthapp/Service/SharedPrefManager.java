package com.example.healthapp.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.healthapp.Entities.Kullanici;
import com.example.healthapp.ui.login.LoginActivity;

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_ID = "keyid";
    private static final String KEY_AD = "keyad";
    private static final String KEY_SOYAD = "keysoyad";
    private static final String KEY_DOGUMTARIHI = "keydogumtarihi";
    private static final String KEY_DIYABETTIPI = "keydiyabettipi";
    private static final String KEY_TESHISKONDUGUTARIH = "keyteshiskondugutarih";
    private static final String KEY_IL = "keyil";
    private static final String KEY_ILCE = "keyilce";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(Kullanici user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_AD, user.getAdi());
        editor.putString(KEY_SOYAD, user.getSoyadi());
        editor.putString(KEY_DOGUMTARIHI, user.getDogumTarihi());
        editor.putString(KEY_DIYABETTIPI, user.getDiyabetTipi());
        editor.putString(KEY_TESHISKONDUGUTARIH, user.getTeshisKondoguTarih());
        editor.putString(KEY_IL, user.getIl());
        editor.putString(KEY_ILCE, user.getIlce());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_GENDER, user.getCinsiyet());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    //this method will give the logged in user
    public Kullanici getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Kullanici(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_AD, null),
                sharedPreferences.getString(KEY_SOYAD, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_DOGUMTARIHI, null),
                sharedPreferences.getString(KEY_DIYABETTIPI, null),
                sharedPreferences.getString(KEY_TESHISKONDUGUTARIH, null),
                sharedPreferences.getString(KEY_IL, null),
                sharedPreferences.getString(KEY_ILCE, null),
                sharedPreferences.getString(KEY_GENDER, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}
