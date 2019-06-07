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
import com.example.healthapp.Entities.Tansiyon;
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


public class TansiyonFragment extends Fragment {

    Button mBtnTansiyonEkle;
    ListView mLstTansiyon;
    ProgressBar progressBar;
    public TansiyonFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tansiyon, container, false);
        progressBar = rootView.findViewById(R.id.progressBarTansiyon);
        mBtnTansiyonEkle = rootView.findViewById(R.id.btnTansiyonEkle);
        mBtnTansiyonEkle.setOnClickListener(v -> {
            Fragment tansiyonFragment = new TansiyonEkleFragment();
            loadFragment(tansiyonFragment);
        });

        progressBar.setVisibility(View.VISIBLE);
        ArrayList<Tansiyon> list = getTansiyonList(rootView);

        mLstTansiyon = rootView.findViewById(R.id.lstTansiyon);
        TansiyonFragment.TansiyonAdapter adapter = new TansiyonFragment.TansiyonAdapter(getActivity().getApplicationContext(), list);
        mLstTansiyon.setAdapter(adapter);

        return rootView;
    }

    private ArrayList<Tansiyon> getTansiyonList(final View rootView) {
        final ArrayList<Tansiyon> list = new ArrayList<>();
        String tansiyonUrl = URLs.URL_TANSIYON_LISTE +"?request="+"{" +
                "\"KullaniciId\": \""+ SharedPrefManager.getInstance(rootView.getContext().getApplicationContext()).getUser().getId()+"\"" +
                "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, tansiyonUrl,
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("Status") == "true") {
                            Toast.makeText(rootView.getContext().getApplicationContext(), obj.getString("Message"), Toast.LENGTH_SHORT).show();

                            JSONArray jsonArr = new JSONArray(obj.getString("Data"));

                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject object = jsonArr.getJSONObject(i);
                                Tansiyon model = new Tansiyon();

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
                                model.setBuyukTansiyon(object.getString("BuyukTansiyon"));
                                model.setKucukTansiyon(object.getString("KucukTansiyon"));
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

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
    private class TansiyonAdapter extends ArrayAdapter<Tansiyon> {
        Context context;
        public TansiyonAdapter(Context c, ArrayList<Tansiyon> objects){
            super(c, R.layout.lst_tansiyon_row,objects);
            this.context = c;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String tarih = getItem(position).getTarih();
            String saat = getItem(position).getSaat();
            String buyukTansiyon = getItem(position).getBuyukTansiyon();
            String kucukTansiyon = getItem(position).getKucukTansiyon();
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.lst_tansiyon_row, parent, false);

            TextView mTxtTarih = row.findViewById(R.id.txtTarih);
            TextView mTxtSaat = row.findViewById(R.id.txtSaat);
            TextView mTxtBuyukTansiyon = row.findViewById(R.id.txtBuyukTansiyon);
            TextView mTxtKucukTansiyon = row.findViewById(R.id.txtKucukTansiyon);
            ImageView mImgLogo = row.findViewById(R.id.imgLogo);
            ImageView mImgSeperator = row.findViewById(R.id.imgSeperator);
            ImageView mImgBackground = row.findViewById(R.id.imgBackGround);

            mTxtTarih.setText(tarih);
            mTxtSaat.setText(saat);
            mTxtBuyukTansiyon.setText(buyukTansiyon);
            mTxtKucukTansiyon.setText(kucukTansiyon);
            mImgLogo.setImageResource(R.drawable.row_logo);
            mImgSeperator.setImageResource(R.drawable.row_seperator);
            mImgBackground.setImageResource(R.drawable.rectangle_1);

            return row;
        }
    }


}
