package com.akillidiyabetim.healthapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentHome extends Fragment {
    Button mBtnKanSekeri;
    Button mBtnTansiyon;
    Button mBtnKitleEndeksi;
    Button mBtnEgzersiz;
    Button mBtnHba1c;
    Button mBtn6;


    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment_home, container, false);

        mBtnKanSekeri = rootView.findViewById(R.id.btnKanSekeri);
        mBtnKanSekeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment kanSekeriFragment = new KanSekeriFragment();
                loadFragment(kanSekeriFragment);
            }
        });
        mBtnTansiyon = rootView.findViewById(R.id.btnTansiyon);
        mBtnTansiyon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment tansiyonFragment = new TansiyonFragment();
                loadFragment(tansiyonFragment);
            }
        });
        mBtnKitleEndeksi = rootView.findViewById(R.id.btnKitleEndeksi);
        mBtnKitleEndeksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment kitleEndeksiFragment = new VkiFragment();
                loadFragment(kitleEndeksiFragment);
            }
        });
        mBtnEgzersiz = rootView.findViewById(R.id.btnEgzersiz);
        mBtnEgzersiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment egzersizFragment = new EgzersizFragment();
                loadFragment(egzersizFragment);
            }
        });
        mBtnHba1c = rootView.findViewById(R.id.btnHba1c);
        mBtnHba1c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment hba1cFragment = new Hba1cFragment();
                loadFragment(hba1cFragment);
            }
        });
        mBtn6 = rootView.findViewById(R.id.btnSuTakibi);
        mBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment suTakibiFragment = new SuTakibiFragment();
                loadFragment(suTakibiFragment);
            }
        });
        return rootView;
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
