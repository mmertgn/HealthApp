package com.akillidiyabetim.healthapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.akillidiyabetim.healthapp.Entities.Kullanici;
import com.akillidiyabetim.healthapp.Service.SharedPrefManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ProfileFragment extends Fragment {
    TextView mTxtAdSoyad,mTxtCinsiyet,mTxtDogumTarihi,mTxtSehir,mTxtIlce,mTxtDiyabetTipi,mTxtDiyabetTeshisTarihi,mTxtEmail;
    Button mBtnLogout;
    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mBtnLogout = rootView.findViewById(R.id.btnLogout);
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(rootView.getContext().getApplicationContext()).logout();
            }
        });
        mTxtAdSoyad = rootView.findViewById(R.id.txtAdSoyad);
        mTxtCinsiyet = rootView.findViewById(R.id.txtCinsiyet);
        mTxtDogumTarihi = rootView.findViewById(R.id.txtDogumTarihi);
        mTxtSehir = rootView.findViewById(R.id.txtSehir);
        mTxtIlce = rootView.findViewById(R.id.txtIlce);
        mTxtDiyabetTipi = rootView.findViewById(R.id.txtDiyabetTipi);
        mTxtDiyabetTeshisTarihi = rootView.findViewById(R.id.txtDiyabetTeshisTarihi);
        mTxtEmail = rootView.findViewById(R.id.txtEmail);

        Kullanici kullanici = SharedPrefManager.getInstance(rootView.getContext().getApplicationContext()).getUser();
        mTxtAdSoyad.setText(String.format("%s %s", kullanici.getAdi(), kullanici.getSoyadi()));
        mTxtCinsiyet.setText(kullanici.getCinsiyet());

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        Date result;
        try {
            result = df.parse(kullanici.getDogumTarihi());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            mTxtDogumTarihi.setText(sdf.format(result));
        } catch (ParseException e) {
            mTxtDogumTarihi.setText("-");
            e.printStackTrace();
        }

        mTxtSehir.setText(kullanici.getIl());
        mTxtIlce.setText(kullanici.getIlce());
        mTxtDiyabetTipi.setText(kullanici.getDiyabetTipi());
        try {
            result = df.parse(kullanici.getTeshisKondoguTarih());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            mTxtDiyabetTeshisTarihi.setText(sdf.format(result));
        } catch (ParseException e) {
            mTxtDiyabetTeshisTarihi.setText("-");
            e.printStackTrace();
        }

        mTxtEmail.setText(kullanici.getEmail());
        return rootView;
    }

}
