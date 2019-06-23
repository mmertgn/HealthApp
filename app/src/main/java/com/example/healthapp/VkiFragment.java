package com.example.healthapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.healthapp.ApiServices.VolleySingleton;
import com.example.healthapp.Entities.Tansiyon;
import com.example.healthapp.Entities.VkiEndeksi;
import com.example.healthapp.Service.SharedPrefManager;
import com.example.healthapp.Service.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class VkiFragment extends Fragment {
    Button mBtnVucutKitleEkle;
    ListView mLstVucutKitleEndeksi;
    ProgressBar progressBar;
    public VkiFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vki, container, false);
        progressBar = rootView.findViewById(R.id.progressBarVki);
        mBtnVucutKitleEkle = rootView.findViewById(R.id.btnVucutKitleEkle);
        mBtnVucutKitleEkle.setOnClickListener(v -> {
            Fragment fragment = new VucutKitleEkleFragment();
            loadFragment(fragment);
        });
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<VkiEndeksi> list = getVkiList(rootView);

        mLstVucutKitleEndeksi = rootView.findViewById(R.id.lstVucutKitleEndeksi);
        VucutKitleAdapter adapter = new VucutKitleAdapter(getActivity().getApplicationContext(), list);
        mLstVucutKitleEndeksi.setAdapter(adapter);

        return rootView;
    }

    private ArrayList<VkiEndeksi> getVkiList(final View rootView) {
        final ArrayList<VkiEndeksi> list = new ArrayList<>();
        String vkiUrl = URLs.URL_VKI_LISTE +"?request="+"{" +
                "\"KullaniciId\": \""+ SharedPrefManager.getInstance(rootView.getContext().getApplicationContext()).getUser().getId()+"\"" +
                "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, vkiUrl,
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("Status") == "true") {
                            Toast.makeText(rootView.getContext().getApplicationContext(), obj.getString("Message"), Toast.LENGTH_SHORT).show();

                            JSONArray jsonArr = new JSONArray(obj.getString("Data"));

                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject object = jsonArr.getJSONObject(i);
                                VkiEndeksi model = new VkiEndeksi();

                                String tarihSaat = object.getString("Tarih");
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                                Date result;
                                try {
                                    result = df.parse(tarihSaat);
                                    SimpleDateFormat ft =  new SimpleDateFormat ("dd-MMM-yyyy");
                                    model.setTarih(ft.format(result));
                                    SimpleDateFormat time_ft =  new SimpleDateFormat ("HH:mm");
                                    time_ft.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    model.setSaat(time_ft.format(result));
                                } catch (ParseException e) {
                                    model.setTarih("-");
                                    model.setSaat("-");
                                    e.printStackTrace();
                                }
                                model.setBoy(object.getString("Boy"));
                                model.setKilo(object.getString("Kilo"));
                                model = VkiHesapla(model);
                                list.add(model);
                            }

                        } else {
                            Toast.makeText(rootView.getContext().getApplicationContext(), obj.getString("Message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(rootView.getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(rootView.getContext().getApplicationContext()).addToRequestQueue(stringRequest);

        return list;
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
    private class VucutKitleAdapter extends ArrayAdapter<VkiEndeksi> {
        Context context;
        public VucutKitleAdapter(Context c, ArrayList<VkiEndeksi> objects){
            super(c, R.layout.lst_vki_endeksi_row,objects);
            this.context = c;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String tarih = getItem(position).getTarih();
            String saat = getItem(position).getSaat();
            String boy = getItem(position).getBoy();
            String kilo = getItem(position).getKilo();
            String vkiEndeksi = getItem(position).getVkiEndeksi();
            String vkiDurumu = getItem(position).getVkiDurumu();
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.lst_vki_endeksi_row, parent, false);

            TextView mTxtTarih = row.findViewById(R.id.txtTarih);
            TextView mTxtSaat = row.findViewById(R.id.txtSaat);
            TextView mTxtBoy = row.findViewById(R.id.txtBoy);
            TextView mTxtKilo = row.findViewById(R.id.txtKilo);
            TextView mTxtVkiEndeksi = row.findViewById(R.id.txtVkiEndeksi);
            TextView mTxtVkiDurumu = row.findViewById(R.id.txtVkiDurumu);
            ImageView mImgLogo = row.findViewById(R.id.imgLogo);
            ImageView mImgSeperator = row.findViewById(R.id.imgSeperator);
            ImageView mImgBackground = row.findViewById(R.id.imgBackGround);

            mTxtTarih.setText(tarih);
            mTxtSaat.setText(saat);
            mTxtBoy.setText(boy);
            mTxtKilo.setText(kilo);
            mTxtVkiEndeksi.setText(vkiEndeksi);
            mTxtVkiDurumu.setText(vkiDurumu);
            mImgLogo.setImageResource(R.drawable.deadlift34);
            mImgSeperator.setImageResource(R.drawable.row_seperator);
            mImgBackground.setImageResource(R.drawable.rectangle_1);

            return row;
        }
    }
    public static VkiEndeksi VkiHesapla(VkiEndeksi model) {
        double kilo,boy,sonuc;
        kilo= Double.valueOf(model.getKilo());
        boy=Double.valueOf(model.getBoy())/100;
        sonuc = kilo/(boy*boy);
        model.setVkiEndeksi(String.valueOf(kilo/(boy*boy)));
        if(sonuc<18.5){
            model.setVkiDurumu("Zayıf");
        } else if(sonuc>=18.5&&sonuc<=24.9) {
            model.setVkiDurumu("Normal");
        } else if(sonuc>24.9&&sonuc<=29.9) {
            model.setVkiDurumu("Kilolu");
        } else if(sonuc>29.9&&sonuc<=34.9) {
            model.setVkiDurumu("1.Derece obez");
        } else if(sonuc>34.9&&sonuc<=39.9) {
            model.setVkiDurumu("2.Derece obez");
        } else if(sonuc>39.9) {
            model.setVkiDurumu("3.Derece obez");
        } else {
            model.setVkiDurumu("Hesaplanamadı...");
        }
        return model;
    }
}
