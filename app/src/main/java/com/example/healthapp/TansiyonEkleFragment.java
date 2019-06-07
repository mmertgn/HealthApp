package com.example.healthapp;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.healthapp.ApiServices.VolleySingleton;
import com.example.healthapp.Entities.Tansiyon;
import com.example.healthapp.Entities.VkiEndeksi;
import com.example.healthapp.R;
import com.example.healthapp.Service.SharedPrefManager;
import com.example.healthapp.Service.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TansiyonEkleFragment extends Fragment {
    EditText mTxtTansiyonSaat, mTxtTansiyonTarih,mTxtBuyukTansiyon,mTxtKucukTansiyon;
    Button mBtnKaydet,btnTansiyonGeri;
    ProgressBar progressBar;

    public TansiyonEkleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tansiyon_ekle, container, false);

        mTxtBuyukTansiyon = rootView.findViewById(R.id.txtBuyukTansiyon);
        mTxtTansiyonSaat = rootView.findViewById(R.id.txtTansiyonSaat);
        mTxtTansiyonTarih = rootView.findViewById(R.id.txtTansiyonTarih);
        mTxtKucukTansiyon = rootView.findViewById(R.id.txtKucukTansiyon);
        mBtnKaydet = rootView.findViewById(R.id.btnKaydet);
        btnTansiyonGeri = rootView.findViewById(R.id.btnTansiyonGeri);
        progressBar = rootView.findViewById(R.id.progressBarTansiyon);

        mTxtTansiyonTarih.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
            int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
            int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

            DatePickerDialog datePicker;//Datepicker objemiz
            datePicker = new DatePickerDialog(rootView.getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
                mTxtTansiyonTarih.setText( year1 + "-" + monthOfYear+ "-"+dayOfMonth);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

            },year,month,day);//başlarken set edilcek değerlerimizi atıyoruz
            datePicker.setTitle("Tarih Seçiniz");
            datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
            datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

            datePicker.show();
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTxtTansiyonTarih.setShowSoftInputOnFocus(false);
        }
        mTxtTansiyonSaat.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();//
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
            int minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayı aldık
            TimePickerDialog timePicker; //Time Picker referansımızı oluşturduk

            //TimePicker objemizi oluşturuyor ve click listener ekliyoruz
            timePicker = new TimePickerDialog(rootView.getContext(), (timePicker1, selectedHour, selectedMinute) -> {
                mTxtTansiyonSaat.setText( selectedHour + ":" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
            }, hour, minute, true);//true 24 saatli sistem için
            timePicker.setTitle("Saat Seçiniz");
            timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
            timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", timePicker);

            timePicker.show();
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTxtTansiyonSaat.setShowSoftInputOnFocus(false);
        }
        mBtnKaydet.setOnClickListener(v -> {
            Tansiyon model = new Tansiyon();
            model.setBuyukTansiyon(mTxtBuyukTansiyon.getText().toString());
            model.setSaat(mTxtTansiyonSaat.getText().toString());
            model.setTarih(mTxtTansiyonTarih.getText().toString());
            model.setKucukTansiyon(mTxtKucukTansiyon.getText().toString());
            model.setKullaniciId(SharedPrefManager.getInstance(rootView.getContext().getApplicationContext()).getUser().getId());
            createTansiyon(model,rootView);
        });
        btnTansiyonGeri.setOnClickListener(v -> {
            Fragment fragment = new TansiyonFragment();
            loadFragment(fragment);
        });

        return rootView;
    }

    private void createTansiyon(Tansiyon model, View rootView) {
        if (TextUtils.isEmpty(model.getTarih())) {
            mTxtTansiyonTarih.setError("Lütfen Tarih giriniz.");
            mTxtTansiyonTarih.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(model.getBuyukTansiyon())) {
            mTxtBuyukTansiyon.setError("Lütfen Büyük Tansiyon Değerini giriniz.");
            mTxtBuyukTansiyon.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(model.getKucukTansiyon())) {
            mTxtKucukTansiyon.setError("Lütfen Küçük Tansiyon Değerini giriniz.");
            mTxtKucukTansiyon.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String tansiyonUrl = URLs.URL_TANSIYON_EKLE +"?request="+"{" +
                "\"KullaniciId\": \""+model.getKullaniciId()+"\"," +
                "\"Tarih\": \""+model.getTarih()+ " "+model.getSaat()+"\"," +
                "\"BuyukTansiyon\": \""+model.getBuyukTansiyon()+"\"," +
                "\"KucukTansiyon\": \""+model.getKucukTansiyon()+"\"" +
                "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, tansiyonUrl,
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("Status") == "true") {
                            Toast.makeText(rootView.getContext().getApplicationContext(), obj.getString("Message"), Toast.LENGTH_SHORT).show();

                            //stringRequest.finish();
                            Fragment tansiyonFragment = new TansiyonFragment();
                            loadFragment(tansiyonFragment);
                        } else {
                            Toast.makeText(rootView.getContext().getApplicationContext(), obj.getString("Message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(rootView.getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(rootView.getContext().getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
