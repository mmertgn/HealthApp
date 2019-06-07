package com.example.healthapp;


import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.healthapp.ApiServices.VolleySingleton;
import com.example.healthapp.Entities.Egzersiz;
import com.example.healthapp.Entities.KanSekeri;
import com.example.healthapp.Service.SharedPrefManager;
import com.example.healthapp.Service.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class EgzersizEkleFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private static final String[] paths = {"Hafif Egzersizler", "Orta Yoğunlukta Egzersiz", "Ağır Egzersizler","Yüksek Yoğunluklu Egzersizler"};
    EditText mTxtEgzersizTarih,mTxtEgzersizSure;
    Button mBtnKaydet,mBtnEgzersizGeri;
    ProgressBar progressBar;

    public EgzersizEkleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_egzersiz_ekle, container, false);
        mTxtEgzersizTarih = rootView.findViewById(R.id.txtEgzersizTarih);
        mTxtEgzersizSure = rootView.findViewById(R.id.txtEgzersizSuresi);
        mBtnKaydet = rootView.findViewById(R.id.btnKaydet);
        mBtnEgzersizGeri = rootView.findViewById(R.id.btnEgzersizGeri);
        progressBar = rootView.findViewById(R.id.progressBarEgzersiz);
        spinner = rootView.findViewById(R.id.spnEgzersizTuru);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext().getApplicationContext(),
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mTxtEgzersizTarih.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
            int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
            int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

            DatePickerDialog datePicker;//Datepicker objemiz
            datePicker = new DatePickerDialog(rootView.getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
                mTxtEgzersizTarih.setText( year1 + "-" + monthOfYear+ "-"+dayOfMonth);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

            },year,month,day);//başlarken set edilcek değerlerimizi atıyoruz
            datePicker.setTitle("Tarih Seçiniz");
            datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
            datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

            datePicker.show();
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTxtEgzersizTarih.setShowSoftInputOnFocus(false);
        }
        mBtnKaydet.setOnClickListener(v -> {
            Egzersiz model = new Egzersiz();
            model.setSure(mTxtEgzersizSure.getText().toString());
            model.setEgzersizTipi(spinner.getSelectedItem().toString());
            model.setTarih(mTxtEgzersizTarih.getText().toString());
            model.setKullaniciId(SharedPrefManager.getInstance(rootView.getContext().getApplicationContext()).getUser().getId());
            createEgzersiz(model,rootView);
        });
        mBtnEgzersizGeri.setOnClickListener(v -> {
            Fragment fragment = new EgzersizFragment();
            loadFragment(fragment);
        });
        return rootView;
    }

    private void createEgzersiz(Egzersiz model, View rootView) {
        if (TextUtils.isEmpty(model.getTarih())) {
            mTxtEgzersizTarih.setError("Lütfen Tarih giriniz.");
            mTxtEgzersizTarih.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(model.getSure())) {
            mTxtEgzersizSure.setError("Lütfen Egzersiz Sürenizi giriniz.");
            mTxtEgzersizSure.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        String kanSekeriUrl = URLs.URL_EGZERSIZ_EKLE +"?request="+"{" +
                "\"KullaniciId\": \""+model.getKullaniciId()+"\"," +
                "\"Tarih\": \""+model.getTarih()+"\"," +
                "\"EgzersizTipi\": \""+model.getEgzersizTipi()+"\"," +
                "\"Sure\": \""+model.getSure()+"\"" +
                "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, kanSekeriUrl,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("Status") == "true") {
                            Toast.makeText(rootView.getContext().getApplicationContext(), obj.getString("Message"), Toast.LENGTH_SHORT).show();

                            //stringRequest.finish();
                            Fragment egzersizFragment = new EgzersizFragment();
                            loadFragment(egzersizFragment);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.color_assets_3));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
