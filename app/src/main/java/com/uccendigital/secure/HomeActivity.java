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
import com.uccendigital.secure.app.App;
import com.uccendigital.secure.app.Functions;
import com.uccendigital.secure.app.Hadher;
import com.uccendigital.secure.app.SharedManager;
import com.uccendigital.secure.elements.Sim;
import com.uccendigital.secure.elements.Dialog;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    SharedManager AppShared, hadShared;
    Functions Funct;
    Hadher hadher;
    App app;

    ImageView btnsettings, btnshare;
    LinearLayout enablelayout, btnpermissions, btnhideapp, btn_security_num, newSimBox;
    TextView txtenable, permdescription, numberdescription, hideappdesc;
    Button btnenable, btnvideo;
    private FrameLayout adContainerView;

    boolean backButton = false, AppEnable;
    int idrimen;

    // ca....111 -> TestAd
    // ca-app-pub-3940256099942544/6300978111
    private static final String AD_UNIT_ID = "ca-app-pub-2264850049980959/3365574645";
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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        if (!AppShared.getBool("help")) {
            final Dialog dialog = new Dialog(getApplicationContext(), HomeActivity.this);
            dialog.show("help");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        AppEnable = AppShared.getBool("enable");
        idrimen = hadher.extractPoints();

        Boolean perm = checkPerm();
        Boolean secnum = checkSecurityNum();
        checkAppIcon();

        setupBTN();

        setupNewSIM();

        if (perm && secnum) {
            setEnable(AppEnable);
        } else {
            setEnable(false);
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
        txtenable = findViewById(R.id.txtenable);
        enablelayout = findViewById(R.id.enablelayout);

        final Dialog dialog = new Dialog(getApplicationContext(), HomeActivity.this);
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
                                    dialog.show("dialog");
                                } else {
                                    AppShared.putbool("enable",true);
                                    setEnable(true);
                                }
                            }
                        } else {
                            dialog.show("dialog");
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
                dialog.show("dialog");
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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
            finishAffinity();
        }
    }

    private void setEnable(Boolean setEnable) {

        if (setEnable) {
            enablelayout.setBackgroundColor(getResources().getColor(R.color.colorBlue));
            txtenable.setText(R.string.enabled);
            btnenable.setBackgroundColor(getResources().getColor(R.color.colorRed));
            btnenable.setText(R.string.disabled);
        } else {
            enablelayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            txtenable.setText(R.string.disabled);
            btnenable.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            btnenable.setText(R.string.enabled);
        }

    }

    private Boolean checkPerm() {

        btnpermissions = findViewById(R.id.btnpermissions);
        permdescription = findViewById(R.id.permdescription);
        ImageView permchecked = findViewById(R.id.permsChecked);

        if (!app.checkPerm(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS})) {

            btnpermissions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    app.checkPermissions(new String[] {Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS});

                }
            });

            btnpermissions.setBackground(getDrawable(R.drawable.roundered));
            permdescription.setText(getResources().getString(R.string.permissions_notdone));
            permchecked.setVisibility(View.GONE);
            return false;
        } else {
            btnpermissions.setBackground(getDrawable(R.drawable.roundedark));
            permdescription.setText(getResources().getString(R.string.permissions_done));
            permchecked.setVisibility(View.VISIBLE);
            return true;
        }

    }

    private Boolean checkSecurityNum() {
        btn_security_num = findViewById(R.id.btn_security_num);
        numberdescription = findViewById(R.id.numberdescription);
        ImageView numberchecked = findViewById(R.id.numberChecked);

        if (!hadher.checkIffer("achegue3") || hadher.extractIffer("achegue3").equals("")) {

            btn_security_num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                    i.putExtra("SECTION", "security_num");
                    startActivity(i);
                }
            });

            btn_security_num.setBackground(getDrawable(R.drawable.roundered));
            numberdescription.setText(getResources().getString(R.string.security_num_notdone));
            numberchecked.setVisibility(View.GONE);
            return false;
        } else {
            btn_security_num.setBackground(getDrawable(R.drawable.roundedark));
            numberdescription.setText(getResources().getString(R.string.security_num_done));
            numberchecked.setVisibility(View.VISIBLE);
            return true;
        }

    }

    private Boolean checkAppIcon() {
        btnhideapp = findViewById(R.id.btnhideapp);
        hideappdesc = findViewById(R.id.hideappdesc);
        ImageView hideappchecked = findViewById(R.id.hideAppChecked);

        if (!AppShared.getBool("hide_app")) {

            btnhideapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                    i.putExtra("SECTION", "hide_app");
                    startActivity(i);
                }
            });

            btnhideapp.setBackground(getDrawable(R.drawable.roundered));
            hideappdesc.setText(getResources().getString(R.string.hideapp_notdone));
            hideappchecked.setVisibility(View.GONE);
            return false;
        } else {
            btnhideapp.setBackground(getDrawable(R.drawable.roundedark));
            hideappdesc.setText(getResources().getString(R.string.hideapp_done));
            hideappchecked.setVisibility(View.VISIBLE);
            return true;
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
