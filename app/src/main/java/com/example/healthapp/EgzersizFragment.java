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
import com.example.healthapp.Entities.Egzersiz;
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


public class EgzersizFragment extends Fragment {

    Button mBtnEgzersizEkle;
    ListView mLstEgzersiz;
    ProgressBar progressBar;
    public EgzersizFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_egzersiz, container, false);
        progressBar = rootView.findViewById(R.id.progressBarEgzersiz);
        mBtnEgzersizEkle = rootView.findViewById(R.id.btnEgzersizEkle);
        mBtnEgzersizEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment egzersizEkleFragment = new EgzersizEkleFragment();
                loadFragment(egzersizEkleFragment);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<Egzersiz> list = getEgzersizList(rootView);

        mLstEgzersiz = rootView.findViewById(R.id.lstEgzersiz);
        EgzersizFragment.EgzersizAdapter adapter = new EgzersizFragment.EgzersizAdapter(getActivity().getApplicationContext(), list);
        mLstEgzersiz.setAdapter(adapter);


        // Inflate the layout for this fragment
        return rootView;
    }

    private ArrayList<Egzersiz> getEgzersizList(final View rootView) {
        final ArrayList<Egzersiz> list = new ArrayList<>();
        String vkiUrl = URLs.URL_EGZERSIZ_LISTE +"?request="+"{" +
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
                                Egzersiz model = new Egzersiz();

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
                                model.setSure(object.getString("Sure"));
                                model.setEgzersizTipi(object.getString("EgzersizTipi"));
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
    private class EgzersizAdapter extends ArrayAdapter<Egzersiz> {
        Context context;
        public EgzersizAdapter(Context c, ArrayList<Egzersiz> objects){
            super(c, R.layout.lst_egzersiz_row,objects);
            this.context = c;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String tarih = getItem(position).getTarih();
            String egzersizTipi = getItem(position).getEgzersizTipi();
            String sure = getItem(position).getSure();
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.lst_egzersiz_row, parent, false);

            TextView mTxtTarih = row.findViewById(R.id.txtTarih);
            TextView mTxtEgzersizTipi = row.findViewById(R.id.txtEgzersizTipi);
            TextView mTxtSure = row.findViewById(R.id.txtSure);
            ImageView mImgLogo = row.findViewById(R.id.imgLogo);
            ImageView mImgSeperator = row.findViewById(R.id.imgSeperator);
            ImageView mImgBackground = row.findViewById(R.id.imgBackGround);

            mTxtTarih.setText(tarih);
            mTxtEgzersizTipi.setText(egzersizTipi);
            mTxtSure.setText(sure);
            mImgLogo.setImageResource(R.drawable.running_34);
            mImgSeperator.setImageResource(R.drawable.row_seperator);
            mImgBackground.setImageResource(R.drawable.rectangle_1);

            return row;
        }
    }
}
