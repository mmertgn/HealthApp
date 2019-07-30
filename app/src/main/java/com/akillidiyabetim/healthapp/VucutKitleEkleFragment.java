package com.akillidiyabetim.healthapp;


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
import com.akillidiyabetim.healthapp.ApiServices.VolleySingleton;
import com.akillidiyabetim.healthapp.Entities.VkiEndeksi;
import com.akillidiyabetim.healthapp.Service.SharedPrefManager;
import com.akillidiyabetim.healthapp.Service.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class VucutKitleEkleFragment extends Fragment {
    EditText mTxtVkiSaat, mTxtVkiTarih,mTxtBoy,mTxtKilo;
    Button mBtnKaydet,mBtnVkiGeri;
    ProgressBar progressBar;
    public VucutKitleEkleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_vucut_kitle_ekle, container, false);

        mTxtBoy = rootView.findViewById(R.id.txtBoy);
        mTxtVkiSaat = rootView.findViewById(R.id.txtVkiSaat);
        mTxtVkiTarih = rootView.findViewById(R.id.txtVkiTarih);
        mTxtKilo = rootView.findViewById(R.id.txtKilo);
        mBtnKaydet = rootView.findViewById(R.id.btnKaydet);
        mBtnVkiGeri = rootView.findViewById(R.id.btnVkiGeri);
        progressBar = rootView.findViewById(R.id.progressBarVki);

        mTxtVkiTarih.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
            int month = mcurrentTime.get(Calendar.MONTH)+1;//Güncel Ayı alıyoruz
            int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

            DatePickerDialog datePicker;//Datepicker objemiz
            datePicker = new DatePickerDialog(rootView.getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
                mTxtVkiTarih.setText( year1 + "-" + monthOfYear+ "-"+dayOfMonth);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

            },year,month,day);//başlarken set edilcek değerlerimizi atıyoruz
            datePicker.setTitle("Tarih Seçiniz");
            datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
            datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

            datePicker.show();
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTxtVkiTarih.setShowSoftInputOnFocus(false);
        }
        mTxtVkiSaat.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();//
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
            int minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayı aldık
            TimePickerDialog timePicker; //Time Picker referansımızı oluşturduk

            //TimePicker objemizi oluşturuyor ve click listener ekliyoruz
            timePicker = new TimePickerDialog(rootView.getContext(), (timePicker1, selectedHour, selectedMinute) -> {
                mTxtVkiSaat.setText( selectedHour + ":" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
            }, hour, minute, true);//true 24 saatli sistem için
            timePicker.setTitle("Saat Seçiniz");
            timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
            timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", timePicker);

            timePicker.show();
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTxtVkiSaat.setShowSoftInputOnFocus(false);
        }
        mBtnKaydet.setOnClickListener(v -> {
            VkiEndeksi model = new VkiEndeksi();
            model.setBoy(mTxtBoy.getText().toString());
            model.setSaat(mTxtVkiSaat.getText().toString());
            model.setTarih(mTxtVkiTarih.getText().toString());
            model.setKilo(mTxtKilo.getText().toString());
            model.setKullaniciId(SharedPrefManager.getInstance(rootView.getContext().getApplicationContext()).getUser().getId());
            createVki(model,rootView);
        });
        mBtnVkiGeri.setOnClickListener(v -> {
            Fragment fragment = new VkiFragment();
            loadFragment(fragment);
        });

        return rootView;
    }

    private void createVki(VkiEndeksi model, View rootView) {
        if (TextUtils.isEmpty(model.getTarih())) {
            mTxtVkiTarih.setError("Lütfen Tarih giriniz.");
            mTxtVkiTarih.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(model.getBoy())) {
            mTxtBoy.setError("Lütfen Boy Değerini giriniz.");
            mTxtBoy.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(model.getKilo())) {
            mTxtKilo.setError("Lütfen Kilo Değerini giriniz.");
            mTxtKilo.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String vkiUrl = URLs.URL_VKI_EKLE +"?request="+"{" +
                "\"KullaniciId\": \""+model.getKullaniciId()+"\"," +
                "\"Tarih\": \""+model.getTarih()+ " "+model.getSaat()+"\"," +
                "\"Kilo\": \""+model.getKilo()+"\"," +
                "\"Boy\": \""+model.getBoy()+"\"" +
                "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, vkiUrl,
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("Status") == "true") {
                            Toast.makeText(rootView.getContext().getApplicationContext(), obj.getString("Message"), Toast.LENGTH_SHORT).show();

                            //stringRequest.finish();
                            Fragment vkiFragment = new VkiFragment();
                            loadFragment(vkiFragment);
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
