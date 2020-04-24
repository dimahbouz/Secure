package com.uccendigital.secure.elements;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uccendigital.secure.R;

public class Intro1 extends Fragment {

    ViewPager viewPager;
    TextView btnnext, lanchTest;

    public Intro1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro1, container, false);
        viewPager = getActivity().findViewById(R.id.viewpager);
        btnnext = view.findViewById(R.id.btnnext);
        lanchTest = view.findViewById(R.id.lanchTest);


        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        return view;
    }

}
