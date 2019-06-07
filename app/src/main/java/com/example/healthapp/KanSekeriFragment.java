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

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.healthapp.ApiServices.VolleySingleton;
import com.example.healthapp.Entities.KanSekeri;
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


public class KanSekeriFragment extends Fragment {
    Button mBtnKanSekeriEkle;
    ListView mLstKanSekeri;
    ProgressBar progressBar;
    public KanSekeriFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kan_sekeri, container, false);
        progressBar = rootView.findViewById(R.id.progressBarKanSekeri);
        mBtnKanSekeriEkle = rootView.findViewById(R.id.btnKanSekeriEkle);
        mBtnKanSekeriEkle.setOnClickListener(v -> {
            Fragment kanSekeriEkleFragment = new KanSekeriEkleFragment();
            loadFragment(kanSekeriEkleFragment);
        });

        progressBar.setVisibility(View.VISIBLE);
        ArrayList<KanSekeri> list = getKanSekeriList(rootView);

        mLstKanSekeri = rootView.findViewById(R.id.lstKamSekeri);
        KanSekeriAdapter adapter = new KanSekeriAdapter(getActivity().getApplicationContext(), list);
        mLstKanSekeri.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    private ArrayList<KanSekeri> getKanSekeriList(final View rootView) {
        final ArrayList<KanSekeri> list = new ArrayList<>();
        String kanSekeriUrl = URLs.URL_KANSEKERI_LISTE +"?request="+"{" +
                "\"KullaniciId\": \""+ SharedPrefManager.getInstance(rootView.getContext().getApplicationContext()).getUser().getId()+"\"" +
                "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, kanSekeriUrl,
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("Status") == "true") {
                            Toast.makeText(rootView.getContext().getApplicationContext(), obj.getString("Message"), Toast.LENGTH_SHORT).show();

                            JSONArray jsonArr = new JSONArray(obj.getString("Data"));

                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject object = jsonArr.getJSONObject(i);
                                KanSekeri model = new KanSekeri();

                                String tarihSaat = object.getString("Tarih");
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                                Date result;
                                try {
                                    result = df.parse(tarihSaat);
                                    SimpleDateFormat ft =  new SimpleDateFormat ("dd-MMM-yyyy");
                                    model.setTarih(ft.format(result));

                                } catch (ParseException e) {
                                    model.setTarih("-");
                                    e.printStackTrace();
                                }
                                model.setZamanDilimi(object.getString("Zaman"));
                                model.setKanSekeriDegeri(object.getString("KanSekeriDegeri"));
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(rootView.getContext().getApplicationContext()).addToRequestQueue(stringRequest);

        return list;
    }

    private class KanSekeriAdapter extends ArrayAdapter<KanSekeri> {
        Context context;
        public KanSekeriAdapter(Context c, ArrayList<KanSekeri> objects){
            super(c, R.layout.lst_kan_sekeri_row,objects);
            this.context = c;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String tarih = getItem(position).getTarih();
            String zamanDilimi = getItem(position).getZamanDilimi();
            String kanSekeriDegeri = getItem(position).getKanSekeriDegeri();

            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.lst_kan_sekeri_row, parent, false);

            TextView mTxtTarih = row.findViewById(R.id.txtTarih);
            TextView mTxtZamanDilimi = row.findViewById(R.id.txtZamanDilimi);
            TextView mTxtKanSekeriDegeri = row.findViewById(R.id.txtKanSekeriDegeri);
            ImageView mImgLogo = row.findViewById(R.id.imgLogo);
            ImageView mImgSeperator = row.findViewById(R.id.imgSeperator);
            ImageView mImgBackground = row.findViewById(R.id.imgBackGround);

            mTxtTarih.setText(tarih);
            mTxtZamanDilimi.setText(zamanDilimi);
            mTxtKanSekeriDegeri.setText(kanSekeriDegeri);
            mImgLogo.setImageResource(R.drawable.row_logo);
            mImgSeperator.setImageResource(R.drawable.row_seperator);
            mImgBackground.setImageResource(R.drawable.rectangle_1);

            return row;
        }
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
