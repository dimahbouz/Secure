package com.uccendigital.secure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.uccendigital.secure.app.s;
import com.uccendigital.secure.app.sharedManager;

public class LockActivity extends AppCompatActivity {

    sharedManager AppShared, SShared;
    s s;
    int iffer, action;
    Boolean change;
    private IndicatorDots mIndicatorDots1, mIndicatorDots2, mIndicatorDots3;
    private PinLockView mPinLockView1, mPinLockView2, mPinLockView3;
    LinearLayout LLCode1, LLCode2, LLCode3;
    TextView lockactivityTitle;
    String Code1;

    private PinLockListener mPinLockListener1 = new PinLockListener() {
        @SuppressLint("WrongConstant")
        public void onComplete(String code) {

            /*
            // Saisi d'un nouveau code d'acces
             */

            Code1 = code;

            lockactivityTitle.setText(R.string.confirm_security_code);
            LLCode1.setVisibility(View.GONE);
            LLCode2.setVisibility(View.VISIBLE);
            LLCode3.setVisibility(View.GONE);

        }

        public void onEmpty() {
        }

        public void onPinChange(int pinLength, String intermediatePin) {
        }
    };

    private PinLockListener mPinLockListener2 = new PinLockListener() {
        @SuppressLint("WrongConstant")
        public void onComplete(String code) {

            /*
            // Confirmation de nouveau code d'acces saisi
             */

            /*
            // Ajouter un button retour pour pouvoir modifier le nouveau code saisi avant de le confirmer
             */

            if (Code1.equals(code)) {

                int a = Integer.parseInt(Code1);
                s = new s(getApplicationContext());
                int newIffer = s.putIffer(a);

                SShared.putInt("iffer", newIffer);

                if (iffer == -1) {
                    AppShared.putStr("security", "done");
                    Intent i = new Intent(LockActivity.this, HomeActivity.class);
                    LockActivity.this.startActivity(i);
                }
                finish();

            }else Toast.makeText(LockActivity.this, getResources().getString(R.string.error_confirmation_security_code), 0).show();
        }

        public void onEmpty() {
        }

        public void onPinChange(int pinLength, String intermediatePin) {
        }
    };

    private PinLockListener mPinLockListener3 = new PinLockListener() {
        @SuppressLint("WrongConstant")
        public void onComplete(String code) {

            /*
            // Verification de code d'acces
             */

            /*
            // Ajouter un button OK pour pouvoir passer à home (Ne pas passer directement apres la saisi de tout le code)
             */

            Intent i;
            int extractIffer = new s(LockActivity.this).extractIffer(iffer);

            if (Integer.parseInt(code) == extractIffer) {
                if (action == 1 && change) {
                    /*
                    // Action == 1 && change == true
                    // Verification pour modification de code d'accée
                     */


                    lockactivityTitle.setText(R.string.new_security_code);
                    LLCode1.setVisibility(View.VISIBLE);
                    LLCode2.setVisibility(View.GONE);
                    LLCode3.setVisibility(View.GONE);

                }else {
                    /*
                    // Action == 1 && Change == false
                    // Verification pour accéder a home
                     */

                    i = new Intent(LockActivity.this, HomeActivity.class);
                    LockActivity.this.startActivity(i);
                    finish();
                }
            }else Toast.makeText(LockActivity.this, getResources().getString(R.string.error_incorrect_security_code), 0).show();

        }

        public void onEmpty() {
        }

        public void onPinChange(int pinLength, String intermediatePin) {
        }
    };

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        LLCode1 = findViewById(R.id.code1);
        LLCode2 = findViewById(R.id.code2);
        LLCode3 = findViewById(R.id.code3);

        s = new s(getApplicationContext());

        action = getIntent().getExtras().getInt("ACTION");
        change = getIntent().getExtras().getBoolean("CHANGE");
            /*
            // Action == 0 => Il n'y a pas de code d'acces, il faut en créer un
             */

            /*
            // Action == 1
            //  Change == false => Vérification de code d'accée pour pouvoir accéder à home
            //  Change == true => Vérification de code d'accée pour pouvoir le modifier
             */


        AppShared = new sharedManager(this, "app");
        SShared = new sharedManager(this, "s");

        iffer = SShared.getInteg("iffer");

        lockactivityTitle = findViewById(R.id.LockActivityTitle);

        if (action == 0) {
            lockactivityTitle.setText(R.string.new_security_code);

            LLCode1.setVisibility(View.VISIBLE);
            LLCode2.setVisibility(View.GONE);
            LLCode3.setVisibility(View.GONE);

        }else {
            lockactivityTitle.setText(R.string.security_code);

            LLCode1.setVisibility(View.GONE);
            LLCode2.setVisibility(View.GONE);
            LLCode3.setVisibility(View.VISIBLE);
        }

        this.mPinLockView1 = findViewById(R.id.pin_lock_view1);
        this.mIndicatorDots1 = findViewById(R.id.indicator_dots1);
        this.mPinLockView1.attachIndicatorDots(this.mIndicatorDots1);
        this.mPinLockView1.setPinLockListener(this.mPinLockListener1);
        this.mPinLockView1.setPinLength(5);
        this.mPinLockView1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        this.mIndicatorDots1.setIndicatorType(2);

        this.mPinLockView2 = findViewById(R.id.pin_lock_view2);
        this.mIndicatorDots2 = findViewById(R.id.indicator_dots2);
        this.mPinLockView2.attachIndicatorDots(this.mIndicatorDots2);
        this.mPinLockView2.setPinLockListener(this.mPinLockListener2);
        this.mPinLockView2.setPinLength(5);
        this.mPinLockView2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        this.mIndicatorDots2.setIndicatorType(2);

        this.mPinLockView3 = findViewById(R.id.pin_lock_view3);
        this.mIndicatorDots3 = findViewById(R.id.indicator_dots3);
        this.mPinLockView3.attachIndicatorDots(this.mIndicatorDots3);
        this.mPinLockView3.setPinLockListener(this.mPinLockListener3);
        this.mPinLockView3.setPinLength(5);
        this.mPinLockView3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        this.mIndicatorDots3.setIndicatorType(2);

    }
}