package com.uccendigital.secure.elements;


import android.Manifest;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uccendigital.secure.R;
import com.uccendigital.secure.app.App;

public class Intro2 extends Fragment {

    private ViewPager viewPager;
    private TextView btnprev, btnnext;
    private Button btnperm;
    App app;

    public Intro2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro2, container, false);

        viewPager = getActivity().findViewById(R.id.viewpager);

        btnprev = view.findViewById(R.id.btnprev);

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        btnnext = view.findViewById(R.id.btnnext);

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        /*
        btnperm = view.findViewById(R.id.getPerms);

        app = new App(getContext());

        btnperm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                app.checkPermissions(new String[] {Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS});

            }
        });
         */

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
        if (!app.checkPerm(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS})) {
            btnperm.setVisibility(View.VISIBLE);
        } else {
            btnperm.setVisibility(View.GONE);
        }

         */

    }
}
