package com.akillidiyabetim.healthapp;

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
import com.android.volley.toolbox.StringRequest;
import com.akillidiyabetim.healthapp.ApiServices.VolleySingleton;
import com.akillidiyabetim.healthapp.Entities.SuTakibi;
import com.akillidiyabetim.healthapp.Service.SharedPrefManager;
import com.akillidiyabetim.healthapp.Service.URLs;

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


public class SuTakibiFragment extends Fragment {
    Button mBtnSuEkle;
    ListView mLstSuTakibi;
    ProgressBar progressBar;
    public SuTakibiFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_su_takibi, container, false);
        progressBar = rootView.findViewById(R.id.progressBarSu);
        mBtnSuEkle = rootView.findViewById(R.id.btnSuEkle);
        mBtnSuEkle.setOnClickListener(v -> {
            Fragment suEkleFragment = new SuEkleFragment();
            loadFragment(suEkleFragment);
        });
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<SuTakibi> list = getSuTakibiList(rootView);

        mLstSuTakibi = rootView.findViewById(R.id.lstSuTakibi);
        SuTakibiFragment.SuTakibiAdapter adapter = new SuTakibiFragment.SuTakibiAdapter(getActivity().getApplicationContext(), list);
        mLstSuTakibi.setAdapter(adapter);


        return rootView;
    }

    private ArrayList<SuTakibi> getSuTakibiList(final View rootView) {
        final ArrayList<SuTakibi> list = new ArrayList<>();
        String vkiUrl = URLs.URL_SU_LISTE +"?request="+"{" +
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
                                SuTakibi model = new SuTakibi();

                                String tarihSaat = object.getString("Tarih");
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                                Date result;
                                try {
                                    result = df.parse(tarihSaat);
                                    SimpleDateFormat ft =  new SimpleDateFormat ("dd-MM-yyyy");
                                    model.setTarih(ft.format(result));

                                } catch (ParseException e) {
                                    model.setTarih("-");
                                    e.printStackTrace();
                                }
                                model.setGunlukToplamBardak(object.getInt("GunlukToplamBardak"));
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
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
        }
    }
    private class SuTakibiAdapter extends ArrayAdapter<SuTakibi> {
        Context context;
        SuTakibiAdapter(Context c, ArrayList<SuTakibi> objects){
            super(c, R.layout.lst_su_takibi_row,objects);
            this.context = c;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String tarih = getItem(position).getTarih();
            int SuTakibi = getItem(position).getGunlukToplamBardak();
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.lst_su_takibi_row, parent, false);

            TextView mTxtTarih = row.findViewById(R.id.txtTarih);
            TextView mTxtSuTakibi = row.findViewById(R.id.txtSuTakibi);
            ImageView mImgLogo = row.findViewById(R.id.imgLogo);
            ImageView mImgSeperator = row.findViewById(R.id.imgSeperator);
            ImageView mImgBackground = row.findViewById(R.id.imgBackGround);

            mTxtTarih.setText(tarih);
            mTxtSuTakibi.setText(String.valueOf(SuTakibi));
            mImgLogo.setImageResource(R.drawable.water34);
            mImgSeperator.setImageResource(R.drawable.row_seperator);
            mImgBackground.setImageResource(R.drawable.rectangle_1);

            return row;
        }
    }
}
