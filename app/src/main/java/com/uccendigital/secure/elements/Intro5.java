package com.uccendigital.secure.elements;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.uccendigital.secure.LockActivity;
import com.uccendigital.secure.R;
import com.uccendigital.secure.app.SharedManager;

public class Intro5 extends Fragment {


    private ViewPager viewPager;
    private TextView btnprev, btnnext;

    public Intro5() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro5, container, false);

        viewPager = getActivity().findViewById(R.id.viewpager);

        btnprev = view.findViewById(R.id.btnprev);

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });

        btnnext = view.findViewById(R.id.btnok);

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedManager AppShared = new SharedManager(getContext(), "app");
                String tuto = AppShared.getStr("tuto");
                AppShared.putStr("tuto", "done");

                if (tuto.equals("notdone")) {
                    getActivity().finish();
                } else {


                    Intent intent = new Intent(getContext(), LockActivity.class);
                    intent.putExtra("CHANGE", false);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }

            }
        });

        return view;
    }

}
