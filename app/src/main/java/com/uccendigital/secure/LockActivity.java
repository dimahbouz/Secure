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
import com.uccendigital.secure.app.Functions;
import com.uccendigital.secure.app.Hadher;
import com.uccendigital.secure.app.SharedManager;

import java.util.Calendar;

public class LockActivity extends AppCompatActivity {

    SharedManager AppShared, hadShared;
    Hadher hadher;
    int action;
    Boolean change, check = false;
    private IndicatorDots mIndicatorDots1;
    private PinLockView mPinLockView1;
    LinearLayout lockBtnBack;
    TextView lockactivityTitle;
    String Code1;

    private PinLockListener mPinLockListener1 = new PinLockListener() {
        @SuppressLint("WrongConstant")
        public void onComplete(String code) {

            if (action == 0 && !check) {

                /*
                // Saisi d'un nouveau code d'acces
                 */

                Code1 = code;

                lockactivityTitle.setText(R.string.confirm_security_code);

                lockBtnBack.setVisibility(View.VISIBLE);
                mPinLockView1.resetPinLockView();
                check = true;

            } else if (action == 0 && check) {

                /*
                // Confirmation de nouveau code d'acces saisi
                 */

                /*
                // Ajouter un button retour pour pouvoir modifier le nouveau code saisi avant de le confirmer
                 */

                if (Code1.equals(code)) {

                    hadher = new Hadher(getApplicationContext());
                    hadher.putIffer("iffer", Code1);

                    if (change) {
                        finish();
                    } else {

                        AppShared.putLong("last",System.currentTimeMillis());
                        hadher.putPoints(70);

                        long now = System.currentTimeMillis();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(now);
                        int minutes = calendar.get(Calendar.MINUTE);
                        AppShared.putInt("minute",minutes);
                        AppShared.putLong("fs", now);

                        new Functions(getApplicationContext()).setStoreManager(0);

                        AppShared.putStr("security", "done");
                        AppShared.putbool("enable", false);

                        Intent i = new Intent(LockActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }

                }else Toast.makeText(LockActivity.this, getResources().getString(R.string.error_confirmation_security_code), Toast.LENGTH_SHORT).show();
            } else if (action == 1) {

                /*
                // Verification de code d'acces
                 */

                /*
                // Ajouter un button OK pour pouvoir passer à home (Ne pas passer directement apres la saisi de tout le code)
                 */

                Intent i;
                String iffer = new Hadher(getApplicationContext()).extractIffer("iffer");

                if (code.equals(iffer)) {
                    if (action == 1 && change) {
                        /*
                        // Action == 1 && change == true
                        // Verification pour modification de code d'accée
                         */


                        lockactivityTitle.setText(R.string.new_security_code);
                        mPinLockView1.resetPinLockView();
                        action = 0;
                        check = false;

                    }else if (action == 1 && !change){
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

        lockBtnBack = findViewById(R.id.lockBtnBack);

        hadher = new Hadher(getApplicationContext());

        change = getIntent().getExtras().getBoolean("CHANGE");

        if (!hadher.checkIffer("iffer")) {
            action = 0;
        } else {
            action = 1;
        }

            /*
            // Action == 0 => Il n'y a pas de code d'acces, il faut en créer un
             */

            /*
            // Action == 1
            //  Change == false => Vérification de code d'accée pour pouvoir accéder à home
            //  Change == true => Vérification de code d'accée pour pouvoir le modifier
             */

        AppShared = new SharedManager(this, "app");
        hadShared = new SharedManager(this, "hadher");

        lockactivityTitle = findViewById(R.id.LockActivityTitle);

        if (action == 0) {
            lockactivityTitle.setText(R.string.new_security_code);

        }else if (action == 1){
            lockactivityTitle.setText(R.string.security_code);

        } else {
            finish();
        }

        if (action == 0 || (action == 1 && change)) {

            lockBtnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    lockactivityTitle.setText(R.string.new_security_code);

                    lockBtnBack.setVisibility(View.INVISIBLE);
                    mPinLockView1.resetPinLockView();
                    check = false;

                }
            });

        }

        this.mPinLockView1 = findViewById(R.id.pin_lock_view1);
        this.mIndicatorDots1 = findViewById(R.id.indicator_dots1);
        this.mPinLockView1.attachIndicatorDots(this.mIndicatorDots1);
        this.mPinLockView1.setPinLockListener(this.mPinLockListener1);
        this.mPinLockView1.setPinLength(5);
        this.mPinLockView1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}