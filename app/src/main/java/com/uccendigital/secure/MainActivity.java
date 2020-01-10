package com.uccendigital.secure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.uccendigital.secure.app.sharedManager;

public class MainActivity extends AppCompatActivity {

    sharedManager AppShared;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppShared = new sharedManager(this, "app");

        final String tuto = AppShared.getStr("tuto");
        final String security = AppShared.getStr("security");


        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                if (tuto.equals("") || tuto.equals("notdone")) {
                    i = new Intent(MainActivity.this, TutoActivity.class);
                }else if (security.equals("") || security.equals("notdone")) {
                    i = new Intent(MainActivity.this, LockActivity.class);
                    i.putExtra("ACTION",0); // Create new security code.
                }else {
                    i = new Intent(MainActivity.this, LockActivity.class);
                    i.putExtra("ACTION",1); // Security code exist
                    i.putExtra("CHANGE", false);
                }
                MainActivity.this.startActivity(i);
                MainActivity.this.finish();
            }
        }.start();

    }
}
