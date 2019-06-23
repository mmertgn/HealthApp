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
import com.example.healthapp.Entities.Hba1c;
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

public class Hba1cFragment extends Fragment {
    Button mBtnHba1cEkle;
    ListView mLstHba1c;
    ProgressBar progressBar;
    public Hba1cFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hba1c, container, false);
        progressBar = rootView.findViewById(R.id.progressBarHba1c);
        mBtnHba1cEkle = rootView.findViewById(R.id.btnHba1cEkle);
        mBtnHba1cEkle.setOnClickListener(v -> {
            Fragment hba1cEkleFragment = new Hba1cEkleFragment();
            loadFragment(hba1cEkleFragment);
        });
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<Hba1c> list = getHba1cList(rootView);

        mLstHba1c = rootView.findViewById(R.id.lstHba1c);
        Hba1cAdapter adapter = new Hba1cAdapter(getActivity().getApplicationContext(), list);
        mLstHba1c.setAdapter(adapter);

        return rootView;
    }

    private ArrayList<Hba1c> getHba1cList(final View rootView) {
        final ArrayList<Hba1c> list = new ArrayList<>();
        String hba1cUrl = URLs.URL_HBA1C_LISTE +"?request="+"{" +
                "\"KullaniciId\": \""+ SharedPrefManager.getInstance(rootView.getContext().getApplicationContext()).getUser().getId()+"\"" +
                "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, hba1cUrl,
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("Status") == "true") {
                            Toast.makeText(rootView.getContext().getApplicationContext(), obj.getString("Message"), Toast.LENGTH_SHORT).show();

                            JSONArray jsonArr = new JSONArray(obj.getString("Data"));

                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject object = jsonArr.getJSONObject(i);
                                Hba1c model = new Hba1c();

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
                                model.setHba1c(object.getString("HbA1c1"));
                                model.setYorum(object.getString("Yorum"));
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
    private class Hba1cAdapter extends ArrayAdapter<Hba1c> {
        Context context;
        public Hba1cAdapter(Context c, ArrayList<Hba1c> objects){
            super(c, R.layout.lst_hba1c_row,objects);
            this.context = c;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String tarih = getItem(position).getTarih();
            String saat = getItem(position).getSaat();
            String hba1c = getItem(position).getHba1c();
            String yorum = getItem(position).getYorum();
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.lst_hba1c_row, parent, false);

            TextView mTxtTarih = row.findViewById(R.id.txtTarih);
            TextView mTxtSaat = row.findViewById(R.id.txtSaat);
            TextView mTxtHba1c = row.findViewById(R.id.txtHba1c);
            TextView mTxtYorum = row.findViewById(R.id.txtYorum);
            ImageView mImgLogo = row.findViewById(R.id.imgLogo);
            ImageView mImgSeperator = row.findViewById(R.id.imgSeperator);
            ImageView mImgBackground = row.findViewById(R.id.imgBackGround);

            mTxtTarih.setText(tarih);
            mTxtSaat.setText(saat);
            mTxtHba1c.setText(hba1c);
            mTxtYorum.setText(yorum);
            mImgLogo.setImageResource(R.drawable.diabetes34);
            mImgSeperator.setImageResource(R.drawable.row_seperator);
            mImgBackground.setImageResource(R.drawable.rectangle_1);

            return row;
        }
    }
}
