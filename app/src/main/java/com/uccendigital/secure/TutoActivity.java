package com.uccendigital.secure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.uccendigital.secure.app.sharedManager;

public class TutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto);

        sharedManager AppManager = new sharedManager(this, "app");
        AppManager.putStr("tuto", "done");

    }
}
