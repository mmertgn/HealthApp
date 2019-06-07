package com.example.healthapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.healthapp.ApiServices.VolleySingleton;
import com.example.healthapp.Entities.Kullanici;
import com.example.healthapp.Service.SharedPrefManager;
import com.example.healthapp.Service.URLs;
import com.example.healthapp.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    TextView mTxtAd,mTxtSoyad,mTxtEmail,mTxtSifre,mTxtSifreTekrar,mTxtCinsiyet,mTxtDogumTarihi,mTxtDiyabetTipi,mTxtTeshisKonduguTarih,mTxtIl,mTxtIlce;
    Button mBtnRegister;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mTxtAd = findViewById(R.id.txtAd);
        mTxtSoyad = findViewById(R.id.txtSoyad);
        mTxtEmail = findViewById(R.id.txtEmail);
        mTxtSifre = findViewById(R.id.txtPassword);
        mTxtSifreTekrar = findViewById(R.id.txtPasswordTekrar);
        mTxtCinsiyet = findViewById(R.id.txtCinsiyet);
        mTxtDogumTarihi = findViewById(R.id.txtDogumTarihi);
        mTxtDiyabetTipi = findViewById(R.id.txtDiyabetTipi);
        mTxtTeshisKonduguTarih = findViewById(R.id.txtTeshisTarihi);
        mTxtIl = findViewById(R.id.txtIl);
        mTxtIlce = findViewById(R.id.txtIlce);

        progressBar = findViewById(R.id.progressBar);
        mBtnRegister = findViewById(R.id.btnRegister);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kullanici kullanici = new Kullanici();
                kullanici.setAdi(mTxtAd.getText().toString());
                kullanici.setSoyadi(mTxtSoyad.getText().toString());
                kullanici.setEmail(mTxtEmail.getText().toString());
                kullanici.setSifre(mTxtSifre.getText().toString());
                kullanici.setSifreTekrar(mTxtSifreTekrar.getText().toString());
                kullanici.setCinsiyet(mTxtCinsiyet.getText().toString());
                kullanici.setDogumTarihi(mTxtDogumTarihi.getText().toString());
                kullanici.setDiyabetTipi(mTxtDiyabetTipi.getText().toString());
                kullanici.setTeshisKondoguTarih(mTxtTeshisKonduguTarih.getText().toString());
                kullanici.setIl(mTxtIl.getText().toString());
                kullanici.setIlce(mTxtIlce.getText().toString());

                if (kullanici.getSifre().equals(kullanici.getSifreTekrar())){
                    registerUser(kullanici);
                }else{
                    mTxtSifreTekrar.setError("Girilen şifreler uyuşmamaktadır!");
                    mTxtSifreTekrar.requestFocus();
                    return;
                }

            }
        });

    }

    private void registerUser(final Kullanici kullanici) {
        if (TextUtils.isEmpty(kullanici.getAdi())) {
            mTxtAd.setError("Lütfen Adınızı Giriniz!");
            mTxtAd.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(kullanici.getSoyadi())) {
            mTxtSoyad.setError("Lütfen Soyadınızı Giriniz!");
            mTxtSoyad.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(kullanici.getEmail())) {
            mTxtEmail.setError("Lütfen Mail Adresinizi Giriniz");
            mTxtEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(kullanici.getEmail()).matches()) {
            mTxtEmail.setError("Lütfen Geçerli bir Mail Adresi Giriniz");
            mTxtEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(kullanici.getSifre())) {
            mTxtSifre.setError("Lütfen Şifrenizi Giriniz!");
            mTxtSifre.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String registerUrl = URLs.URL_REGISTER +"?request="+"{" +
                "\"adi\": \""+kullanici.getAdi()+"\"," +
                "\"soyadi\": \""+kullanici.getSoyadi()+"\"," +
                "\"email\": \""+kullanici.getEmail()+"\"," +
                "\"sifre\": \""+kullanici.getSifre()+"\"," +
                "\"sifreTekrar\": \""+kullanici.getSifreTekrar()+"\"," +
                "\"cinsiyet\": \""+kullanici.getCinsiyet()+"\"," +
                "\"dogumTarihi\": \""+kullanici.getDogumTarihi()+"\"," +
                "\"diyabetTipi\": \""+kullanici.getDiyabetTipi()+"\"," +
                "\"teshisKonduguTarih\": \""+kullanici.getTeshisKondoguTarih()+"\"," +
                "\"il\": \""+kullanici.getIl()+"\"," +
                "\"ilce\": \""+kullanici.getIlce()+"\"" +
                "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, registerUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("Status") == "true") {
                                Toast.makeText(getApplicationContext(), obj.getString("Message"), Toast.LENGTH_SHORT).show();

                                JSONObject userJson = new JSONObject(obj.getString("Data"));

                                Kullanici user = new Kullanici();
                                user.setEmail(userJson.getString("Email"));
                                user.setSifre(userJson.getString("Sifre"));
                                user.setAdi(userJson.getString("Adi"));
                                user.setSoyadi(userJson.getString("Soyadi"));
                                user.setCinsiyet(userJson.getString("Cinsiyet"));
                                user.setDogumTarihi(userJson.getString("DogumTarihi"));
                                user.setDiyabetTipi(userJson.getString("DiyabetTipi"));
                                user.setTeshisKondoguTarih(userJson.getString("TeshisKonduguTarih"));
                                user.setIl(userJson.getString("il"));
                                user.setIlce(userJson.getString("ilce"));
                                user.setId(userJson.getInt("Id"));
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("Message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("request", "{\n" +
                        "  \"adi\": \"Mustafa Mert\",\n" +
                        "  \"soyadi\": \"Gün\",\n" +
                        "  \"email\": \"m.mertgn1@gmail.com\",\n" +
                        "  \"sifre\": \"123456\",\n" +
                        "  \"sifreTekrar\": \"123456\",\n" +
                        "  \"cinsiyet\": \"Erkek\",\n" +
                        "  \"dogumTarihi\": \"2019-05-06\",\n" +
                        "  \"diyabetTipi\": \"test\",\n" +
                        "  \"teshisKonduguTarih\": \"2019-02-06\",\n" +
                        "  \"il\": \"test\",\n" +
                        "  \"ilce\": \"test\"\n" +
                        "}");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
