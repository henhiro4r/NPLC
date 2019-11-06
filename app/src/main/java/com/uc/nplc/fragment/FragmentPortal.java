package com.uc.nplc.fragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.android.volley.RequestQueue;
import com.uc.nplc.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPortal extends Fragment {


    public FragmentPortal() {
        // Required empty public constructor
    }

    SharedPreferences userPref, phonePref;
    SharedPreferences.Editor userEditor, phoneEditor;
    RequestQueue requestQueue;

    private ProgressDialog pd;
    private AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);

    CardView cv_p1,cv_p2,cv_p3,cv_p4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_portal, container, false);

        cv_p1 = v.findViewById(R.id.cv_p1_fr_portal);


        cv_p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
            }
        });

        return v;
    }

}
