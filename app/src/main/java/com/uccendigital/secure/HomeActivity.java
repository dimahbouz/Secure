package com.uccendigital.secure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.uccendigital.secure.adapters.Header;
import com.uccendigital.secure.adapters.MyRVAdapter;
import com.uccendigital.secure.app.Ads;
import com.uccendigital.secure.app.App;
import com.uccendigital.secure.app.Functions;
import com.uccendigital.secure.app.Hadher;
import com.uccendigital.secure.app.SharedManager;
import com.uccendigital.secure.elements.Sim;
import com.uccendigital.secure.elements.StoreDialog;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    SharedManager AppShared, hadShared;
    Functions Funct;
    Hadher hadher;
    App app;

    ImageView btnsettings, btnshare, imgenable;
    LinearLayout enablelayout, btnpermissions, btnhideapp, btn_security_num, action_layout, newSimBox;
    TextView txtenable;
    Button btnenable, btnvideo;
    private FrameLayout adContainerView;

    boolean backButton = false, AppEnable;
    int idrimen;

    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        app = new App(HomeActivity.this);
        Funct = new Functions(getApplicationContext());
        AppShared = new SharedManager(getApplicationContext(), "app");
        hadShared = new SharedManager(getApplicationContext(), "hadher");
        hadher = new Hadher(getApplicationContext());
        // ads = new Ads(getApplicationContext());

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
          @Override
          public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        AppEnable = AppShared.getBool("enable");
        idrimen = hadher.extractPoints();

        Boolean perm = checkPerm();
        Boolean secnum = checkSecurityNum();
        Boolean appico = checkAppIcon();

        setupBTN();

        setupNewSIM();

        if (checkPerm() && secnum) {
            setEnable(AppEnable);
        } else {
            setEnable(false);
        }

        action_layout = findViewById(R.id.actions_layout);
        if (perm && secnum && appico) {
            action_layout.setVisibility(View.GONE);
        } else {
            action_layout.setVisibility(View.VISIBLE);
        }

        adContainerView = findViewById(R.id.ad_view_container);
        initAd(adContainerView);

    }

    private void setupNewSIM() {
        newSimBox = findViewById(R.id.newSimBox);
        final List<Sim> newSimList = Funct.getNewSim();

        if (newSimList.size() > 0) {

            ArrayList<Object> mList = new ArrayList<Object>();
            mList.add(new Header("Nouvelles sim detectés"));

            for (int i = 0; i < newSimList.size(); i++) {
                mList.add(newSimList.get(i));
            }

            RecyclerView newSimListView = findViewById(R.id.newSimListView);
            MyRVAdapter adapter = new MyRVAdapter(this,R.layout.list_item, mList, new String[] {"add"});
            newSimListView.setHasFixedSize(true);
            newSimListView.setLayoutManager(new LinearLayoutManager(this));
            newSimListView.setAdapter(adapter);

            newSimBox.setVisibility(View.VISIBLE);

        } else {
            newSimBox.setVisibility(View.GONE);
        }

    }

    private void setupBTN() {

        /*
        // Click in button to enable security in application if you have the points
         */
        btnenable = findViewById(R.id.btn_enable);
        imgenable = findViewById(R.id.imgenable);
        txtenable = findViewById(R.id.txtenable);
        enablelayout = findViewById(R.id.enablelayout);

        final StoreDialog storeDialog = new StoreDialog(getApplicationContext(), HomeActivity.this);
        btnenable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                // AppEnable = 0 => Application is disabled
                // AppEnable = 1 => Application is enabled
                 */

                if (checkPerm()) {
                    if (checkSecurityNum()) {

                        if (hadher.extractPoints() > 0) {

                            AppEnable = AppShared.getBool("enable");
                            if (AppEnable) {
                                AppShared.putbool("enable",false);
                                setEnable(false);
                            } else if (!AppEnable){
                                if (idrimen <= 0) {
                                    storeDialog.show();
                                } else {
                                    AppShared.putbool("enable",true);
                                    setEnable(true);
                                }
                            }
                        } else {
                            storeDialog.show();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.soldenegative), Toast.LENGTH_SHORT).show();
                        }
                    } else Toast.makeText(getApplicationContext(), getResources().getString(R.string.security_num_notdone), Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getApplicationContext(), getResources().getString(R.string.permissions_notdone), Toast.LENGTH_SHORT).show();
            }
        });

        btnvideo = findViewById(R.id.btn_video);
        btnvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeDialog.show();
            }
        });

        /*
        // Click in button and go to SettingsActivity
         */
        btnsettings = findViewById(R.id.btnsettings);
        btnsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra("SECTION", "");
                startActivity(intent);

            }
        });

        /*
        // Click in button and set ShareAction
         */
        btnshare = findViewById(R.id.btnshare);
        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Funct.shareApplication();
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (!backButton) {

            Toast.makeText(HomeActivity.this, "Appuyez une deuxième fois pour quitter", Toast.LENGTH_SHORT).show();

            backButton = true;

            new CountDownTimer(3000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    backButton = false;
                }
            }.start();

        } else if (backButton) {
            finish();
        }
    }

    private void setEnable(Boolean setEnable) {

        if (setEnable) {
            enablelayout.setBackgroundColor(getResources().getColor(R.color.colorBlue));
            imgenable.setImageResource(R.drawable.checked);
            txtenable.setText(R.string.enabled);
            btnenable.setBackgroundColor(getResources().getColor(R.color.colorRed));
            btnenable.setText(R.string.disabled);
        } else {
            enablelayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            imgenable.setImageResource(R.drawable.close);
            txtenable.setText(R.string.disabled);
            btnenable.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            btnenable.setText(R.string.enabled);
        }

    }

    private Boolean checkPerm() {

        btnpermissions = findViewById(R.id.btnpermissions);

        btnpermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                app.checkPermissions(new String[] {"android.permission.READ_PHONE_STATE", "android.permission.SEND_SMS"});

            }
        });

        if (!app.checkPerm(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS})) {
            btnpermissions.setVisibility(View.VISIBLE);
            return false;
        } else {
            btnpermissions.setVisibility(View.GONE);
            return true;
        }

    }

    private Boolean checkSecurityNum() {
        btn_security_num = findViewById(R.id.btn_security_num);

        btn_security_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                i.putExtra("SECTION", "security_num");
                startActivity(i);
            }
        });

        if (!hadher.checkIffer("achegue3")) {
            btn_security_num.setVisibility(View.VISIBLE);
            return false;
        } else {
            btn_security_num.setVisibility(View.GONE);
            return true;
        }

    }

    private Boolean checkAppIcon() {
        btnhideapp = findViewById(R.id.btnhideapp);

        btnhideapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                i.putExtra("SECTION", "hide_app");
                startActivity(i);
            }
        });

        if (AppShared.getBool("hide_app")) {
            btnhideapp.setVisibility(View.GONE);
            return true;
        } else {
            btnhideapp.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private void initAd(final FrameLayout adContainerView) {

        adContainerView.post(new Runnable() {
          @Override
          public void run() {
           loadBanner(adContainerView);
          }
        });
    }

    private void loadBanner(FrameLayout adContainerView) {
        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        adView = new AdView(getApplicationContext());
        adView.setAdUnitId(AD_UNIT_ID);
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdWidth();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdWidth() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        WindowManager wm = (WindowManager) getSystemService(getApplicationContext().WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getApplicationContext(), adWidth);
    }

}
