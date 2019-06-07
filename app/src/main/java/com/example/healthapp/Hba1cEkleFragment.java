package com.example.healthapp;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.healthapp.ApiServices.VolleySingleton;
import com.example.healthapp.Entities.Hba1c;
import com.example.healthapp.Entities.Kullanici;
import com.example.healthapp.Service.SharedPrefManager;
import com.example.healthapp.Service.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Hba1cEkleFragment extends Fragment {
    EditText mTxtHba1cSaat, mTxtHba1cTarih,mTxtHba1cDegeri,mTxtHba1cYorum;
    Button mBtnKaydet;
    ProgressBar progressBar;
    public Hba1cEkleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_hba1c_ekle, container, false);

        mTxtHba1cDegeri = rootView.findViewById(R.id.txtHba1cDegeri);
        mTxtHba1cSaat = rootView.findViewById(R.id.txtHba1cSaat);
        mTxtHba1cTarih = rootView.findViewById(R.id.txtHba1cTarih);
        mTxtHba1cYorum = rootView.findViewById(R.id.txtHba1cYorum);
        mBtnKaydet = rootView.findViewById(R.id.btnKaydet);
        progressBar = rootView.findViewById(R.id.progressBar);

        mTxtHba1cTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
                int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
                int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

                DatePickerDialog datePicker;//Datepicker objemiz
                datePicker = new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        mTxtHba1cTarih.setText( year + "-" + monthOfYear+ "/"+dayOfMonth);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

                    }
                },year,month,day);//başlarken set edilcek değerlerimizi atıyoruz
                datePicker.setTitle("Tarih Seçiniz");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

                datePicker.show();
            }
        });

        mTxtHba1cSaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
                int minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayı aldık
                TimePickerDialog timePicker; //Time Picker referansımızı oluşturduk

                //TimePicker objemizi oluşturuyor ve click listener ekliyoruz
                timePicker = new TimePickerDialog(rootView.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mTxtHba1cSaat.setText( selectedHour + ":" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                    }
                }, hour, minute, true);//true 24 saatli sistem için
                timePicker.setTitle("Saat Seçiniz");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", timePicker);

                timePicker.show();
            }
        });
        mBtnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hba1c hba1c = new Hba1c();
                hba1c.setHba1c(mTxtHba1cDegeri.getText().toString());
                hba1c.setSaat(mTxtHba1cSaat.getText().toString());
                hba1c.setTarih(mTxtHba1cTarih.getText().toString());
                hba1c.setYorum(mTxtHba1cYorum.getText().toString());
                hba1c.setKullaniciId(SharedPrefManager.getInstance(rootView.getContext().getApplicationContext()).getUser().getId());
                createHba1c(hba1c,rootView);
            }
        });


        return rootView;
    }

    private void createHba1c(Hba1c hba1c, final View rootView) {
        if (TextUtils.isEmpty(hba1c.getHba1c())) {
            mTxtHba1cDegeri.setError("Lütfen Hba1c Değerini giriniz.");
            mTxtHba1cDegeri.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(hba1c.getTarih())) {
            mTxtHba1cTarih.setError("Lütfen Tarih giriniz.");
            mTxtHba1cTarih.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        String hba1cUrl = URLs.URL_HBA1C_EKLE +"?request="+"{" +
                "\"KullaniciId\": \""+hba1c.getKullaniciId()+"\"," +
                "\"Tarih\": \""+hba1c.getTarih()+ " "+hba1c.getSaat()+"\"," +
                "\"HbA1C\": \""+hba1c.getHba1c()+"\"," +
                "\"Yorum\": \""+hba1c.getYorum()+"\"," +
                "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, hba1cUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("Status") == "true") {
                                Toast.makeText(rootView.getContext().getApplicationContext(), obj.getString("Message"), Toast.LENGTH_SHORT).show();

                                //stringRequest.finish();
                                Fragment hba1cFragment = new Hba1cFragment();
                                loadFragment(hba1cFragment);
                            } else {
                                Toast.makeText(rootView.getContext().getApplicationContext(), obj.getString("Message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(rootView.getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
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
