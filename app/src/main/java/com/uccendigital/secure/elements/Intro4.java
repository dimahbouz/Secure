package com.uccendigital.secure.elements;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uccendigital.secure.LockActivity;
import com.uccendigital.secure.R;
import com.uccendigital.secure.app.SharedManager;

public class Intro4 extends Fragment {


    private ViewPager viewPager;
    private TextView btnprev, btnnext;

    public Intro4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro4, container, false);

        viewPager = getActivity().findViewById(R.id.viewpager);

        btnprev = view.findViewById(R.id.btnprev);

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        btnnext = view.findViewById(R.id.btnnext);

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(4);
            }
        });

        return view;
    }

}
