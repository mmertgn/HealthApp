package com.akillidiyabetim.healthapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.akillidiyabetim.healthapp.ApiServices.VolleySingleton;
import com.akillidiyabetim.healthapp.Entities.Kullanici;
import com.akillidiyabetim.healthapp.Service.SharedPrefManager;
import com.akillidiyabetim.healthapp.Service.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
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

        mTxtDogumTarihi.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
            int month = mcurrentTime.get(Calendar.MONTH)+1;//Güncel Ayı alıyoruz
            int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

            DatePickerDialog datePicker;//Datepicker objemiz
            datePicker = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
                mTxtDogumTarihi.setText( year1 + "-" + monthOfYear+ "-"+dayOfMonth);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

            },year,month,day);//başlarken set edilcek değerlerimizi atıyoruz
            datePicker.setTitle("Tarih Seçiniz");
            datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
            datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

            datePicker.show();
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTxtDogumTarihi.setShowSoftInputOnFocus(false);
            mTxtTeshisKonduguTarih.setShowSoftInputOnFocus(false);

        }
        mTxtTeshisKonduguTarih.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
            int month = mcurrentTime.get(Calendar.MONTH)+1;//Güncel Ayı alıyoruz
            int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

            DatePickerDialog datePicker;//Datepicker objemiz
            datePicker = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
                mTxtTeshisKonduguTarih.setText( year1 + "-" + monthOfYear+ "-"+dayOfMonth);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

            },year,month,day);//başlarken set edilcek değerlerimizi atıyoruz
            datePicker.setTitle("Tarih Seçiniz");
            datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
            datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

            datePicker.show();
        });
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
                response -> {
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
                },
                error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
