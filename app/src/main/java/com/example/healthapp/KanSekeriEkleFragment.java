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
import com.example.healthapp.Entities.KanSekeri;
import com.example.healthapp.Service.SharedPrefManager;
import com.example.healthapp.Service.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class KanSekeriEkleFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private static final String[] paths = {"Sabaha Karşı", "Kahvaltı Öncesi", "Kahvaltı Sonrası","Öğle Yemeği Öncesi","Öğle Yemeği Sonrası","Akşam Yemeği Öncesi","Akşam Yemeği Sonrası","Uyku Öncesi"};
    EditText mTxtKanSekeriTarih,mTxtKanSekeri;
    Button mBtnKaydet,mBtnKanSekeriGeri;
    ProgressBar progressBar;
    public KanSekeriEkleFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kan_sekeri_ekle, container, false);

        mTxtKanSekeriTarih = rootView.findViewById(R.id.txtKanSekeriTarih);
        mTxtKanSekeri = rootView.findViewById(R.id.txtKanSekeri);
        mBtnKaydet = rootView.findViewById(R.id.btnKaydet);
        mBtnKanSekeriGeri = rootView.findViewById(R.id.btnKanSekeriGeri);
        progressBar = rootView.findViewById(R.id.progressBarKanSekeri);
        spinner = rootView.findViewById(R.id.spnZamanDilimi);
        ArrayAdapter<String>adapter = new ArrayAdapter<>(getContext().getApplicationContext(),
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mTxtKanSekeriTarih.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
            int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
            int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

            DatePickerDialog datePicker;//Datepicker objemiz
            datePicker = new DatePickerDialog(rootView.getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
                mTxtKanSekeriTarih.setText( year1 + "-" + monthOfYear+ "-"+dayOfMonth);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

            },year,month,day);//başlarken set edilcek değerlerimizi atıyoruz
            datePicker.setTitle("Tarih Seçiniz");
            datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
            datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

            datePicker.show();
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTxtKanSekeriTarih.setShowSoftInputOnFocus(false);
        }
        mBtnKaydet.setOnClickListener(v -> {
            KanSekeri model = new KanSekeri();
            model.setKanSekeriDegeri(mTxtKanSekeri.getText().toString());
            model.setZamanDilimi(spinner.getSelectedItem().toString());
            model.setTarih(mTxtKanSekeriTarih.getText().toString());
            model.setKullaniciId(SharedPrefManager.getInstance(rootView.getContext().getApplicationContext()).getUser().getId());
            createKanSekeri(model,rootView);
        });
        mBtnKanSekeriGeri.setOnClickListener(v -> {
            Fragment fragment = new KanSekeriFragment();
            loadFragment(fragment);
        });
        return rootView;
    }

    private void createKanSekeri(KanSekeri model, View rootView) {
        if (TextUtils.isEmpty(model.getKanSekeriDegeri())) {
            mTxtKanSekeri.setError("Lütfen Kan Şekeri Değerini giriniz.");
            mTxtKanSekeri.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(model.getTarih())) {
            mTxtKanSekeriTarih.setError("Lütfen Tarih giriniz.");
            mTxtKanSekeriTarih.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        String kanSekeriUrl = URLs.URL_KANSEKERI_EKLE +"?request="+"{" +
                "\"KullaniciId\": \""+model.getKullaniciId()+"\"," +
                "\"Tarih\": \""+model.getTarih()+"\"," +
                "\"KanSekeriDegeri\": \""+model.getKanSekeriDegeri()+"\"," +
                "\"Zaman\": \""+model.getZamanDilimi()+"\"" +
                "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, kanSekeriUrl,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("Status") == "true") {
                            Toast.makeText(rootView.getContext().getApplicationContext(), obj.getString("Message"), Toast.LENGTH_SHORT).show();

                            //stringRequest.finish();
                            Fragment kanSekeriFragment = new KanSekeriFragment();
                            loadFragment(kanSekeriFragment);
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ((TextView) spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.color_assets_3));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
