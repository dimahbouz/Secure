package com.uccendigital.secure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.uccendigital.secure.adapters.IntroAdapter;
import com.uccendigital.secure.app.SharedManager;

public class TutoActivity extends AppCompatActivity {
    ViewPager viewPager;
    SharedManager AppShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto);

        viewPager = findViewById(R.id.viewpager);
        IntroAdapter adapter = new IntroAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        AppShared = new SharedManager(getApplicationContext(), "app");
        String tuto = AppShared.getStr("tuto");

        if (!tuto.equals("") && !tuto.equals("notdone")) {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppShared.putStr("tuto", "done");
    }
}
