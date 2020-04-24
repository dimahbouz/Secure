package com.uccendigital.secure.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.uccendigital.secure.elements.Intro1;
import com.uccendigital.secure.elements.Intro2;
import com.uccendigital.secure.elements.Intro3;
import com.uccendigital.secure.elements.Intro4;
import com.uccendigital.secure.elements.Intro5;

public class IntroAdapter extends FragmentPagerAdapter {
    public IntroAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Intro1();
            case 1:
                return new Intro2();
            case 2:
                return new Intro3();
            case 3:
                return new Intro4();
            case 4:
                return new Intro5();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
