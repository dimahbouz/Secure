package com.uccendigital.secure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.uccendigital.secure.app.App;
import com.uccendigital.secure.app.SharedManager;

public class MainActivity extends AppCompatActivity {
    SharedManager AppShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String version = "";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView showVersion = findViewById(R.id.showVersion);
        showVersion.setText(version);

        AppShared = new SharedManager(this, "app");

        final String tuto = AppShared.getStr("tuto");

        new App(getApplicationContext()).checkUpdate();

        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                Intent i;
                if (tuto.equals("")) {
                    i = new Intent(MainActivity.this, TutoActivity.class);
                } else {
                    i = new Intent(MainActivity.this, LockActivity.class);
                    i.putExtra("CHANGE", false);
                }
                startActivity(i);
                finish();
            }
        }.start();

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
