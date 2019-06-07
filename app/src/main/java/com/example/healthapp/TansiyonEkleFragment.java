package com.example.healthapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TansiyonEkleFragment extends Fragment {


    public TansiyonEkleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tansiyon_ekle, container, false);
    }

}
