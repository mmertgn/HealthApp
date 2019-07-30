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
import com.akillidiyabetim.healthapp.Entities.SuTakibi;
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
public class SuEkleFragment extends Fragment {
    EditText mTxtSuSaat, mTxtSuTarih,mTxtSu;
    Button mBtnKaydet,mBtnSuGeri;
    ProgressBar progressBar;

    public SuEkleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_su_ekle, container, false);

        mTxtSu = rootView.findViewById(R.id.txtSu);
        mTxtSuSaat = rootView.findViewById(R.id.txtSuSaat);
        mTxtSuTarih = rootView.findViewById(R.id.txtSuTarih);
        mBtnKaydet = rootView.findViewById(R.id.btnKaydet);
        mBtnSuGeri = rootView.findViewById(R.id.btnSuGeri);
        progressBar = rootView.findViewById(R.id.progressBarSu);

        mTxtSuTarih.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
            int month = mcurrentTime.get(Calendar.MONTH)+1;//Güncel Ayı alıyoruz
            int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

            DatePickerDialog datePicker;//Datepicker objemiz
            datePicker = new DatePickerDialog(rootView.getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
                mTxtSuTarih.setText( year1 + "-" + monthOfYear+ "-"+dayOfMonth);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

            },year,month,day);//başlarken set edilcek değerlerimizi atıyoruz
            datePicker.setTitle("Tarih Seçiniz");
            datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
            datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

            datePicker.show();
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTxtSuTarih.setShowSoftInputOnFocus(false);
        }
        mTxtSuSaat.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();//
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
            int minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayı aldık
            TimePickerDialog timePicker; //Time Picker referansımızı oluşturduk

            //TimePicker objemizi oluşturuyor ve click listener ekliyoruz
            timePicker = new TimePickerDialog(rootView.getContext(), (timePicker1, selectedHour, selectedMinute) -> {
                mTxtSuSaat.setText( selectedHour + ":" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
            }, hour, minute, true);//true 24 saatli sistem için
            timePicker.setTitle("Saat Seçiniz");
            timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
            timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", timePicker);

            timePicker.show();
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTxtSuSaat.setShowSoftInputOnFocus(false);
        }
        mBtnKaydet.setOnClickListener(v -> {
            SuTakibi model = new SuTakibi();
            model.setGunlukToplamBardak(Integer.valueOf(mTxtSu.getText().toString()));
            model.setTarih(mTxtSuTarih.getText().toString());
            model.setKullaniciId(SharedPrefManager.getInstance(rootView.getContext().getApplicationContext()).getUser().getId());
            createSu(model,rootView);
        });
        mBtnSuGeri.setOnClickListener(v -> {
            Fragment fragment = new SuTakibiFragment();
            loadFragment(fragment);
        });

        return rootView;
    }

    private void createSu(SuTakibi model, View rootView) {
        if (TextUtils.isEmpty(model.getTarih())) {
            mTxtSuTarih.setError("Lütfen Tarih giriniz.");
            mTxtSuTarih.requestFocus();
            return;
        }
        if (model.getGunlukToplamBardak() < 0) {
            mTxtSu.setError("Lütfen Tüketilen Su Miktarını giriniz.");
            mTxtSu.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String SuUrl = URLs.URL_SU_EKLE +"?request="+"{" +
                "\"KullaniciId\": \""+model.getKullaniciId()+"\"," +
                "\"Tarih\": \""+model.getTarih()+"\"," +
                "\"ToplamBardak\": \""+model.getGunlukToplamBardak()+"\","+
                "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SuUrl,
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("Status") == "true") {
                            Toast.makeText(rootView.getContext().getApplicationContext(), obj.getString("Message"), Toast.LENGTH_SHORT).show();

                            //stringRequest.finish();
                            Fragment SuFragment = new SuTakibiFragment();
                            loadFragment(SuFragment);
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
