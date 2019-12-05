package com.uccendigital.secure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.uccendigital.secure.app.s;
import com.uccendigital.secure.app.sharedManager;

public class LockActivity extends AppCompatActivity {
    public static final String TAG = "PinLockView";

    sharedManager AppShared;
    s s;
    int iffer;
    int action;
    private IndicatorDots mIndicatorDots1, mIndicatorDots2, mIndicatorDots3;
    private PinLockView mPinLockView1, mPinLockView2, mPinLockView3;

    private PinLockListener mPinLockListener = new PinLockListener() {
        @SuppressLint("WrongConstant")
        public void onComplete(String code) {

            Intent i;
            int extractIffer = new s(LockActivity.this).extractIffer(iffer);
            if (action == 0 && iffer == -1) {
                // Create un first security code

            }else if (action == 0 && iffer != -1) {
                // Change your security code

                if (Integer.parseInt(code) == extractIffer) {

                }else Toast.makeText(LockActivity.this, "The security code is not correct, try again...", 0).show();

            }else {
                // Security code verification

                if (Integer.parseInt(code) == extractIffer) {
                    i = new Intent(LockActivity.this, HomeActivity.class);
                    LockActivity.this.startActivity(i);
                }else Toast.makeText(LockActivity.this, "The security code is not correct, try again...", 0).show();
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

        action = getIntent().getExtras().getInt("ACTION");

        AppShared = new sharedManager(this, "app");
        String security = AppShared.getStr("security");
        if (action == 0 && security.equals("")) {
            AppShared.putStr("security","notdone");
        }
        iffer = AppShared.getInteg("iffer");

        TextView lockactivityTitle = (TextView) findViewById(R.id.LockActivityTitle);

        if (action == 0 && iffer == -1) {
            lockactivityTitle.setText(R.string.new_security_code);
        } else {
            lockactivityTitle.setText(R.string.security_code);
        }

        this.mPinLockView1 = (PinLockView) findViewById(R.id.pin_lock_view1);
        this.mIndicatorDots1 = (IndicatorDots) findViewById(R.id.indicator_dots1);
        this.mPinLockView1.attachIndicatorDots(this.mIndicatorDots1);
        this.mPinLockView1.setPinLockListener(this.mPinLockListener);
        this.mPinLockView1.setPinLength(5);
        this.mPinLockView1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        this.mIndicatorDots1.setIndicatorType(2);

        this.mPinLockView2 = (PinLockView) findViewById(R.id.pin_lock_view2);
        this.mIndicatorDots2 = (IndicatorDots) findViewById(R.id.indicator_dots2);
        this.mPinLockView2.attachIndicatorDots(this.mIndicatorDots2);
        this.mPinLockView2.setPinLockListener(this.mPinLockListener);
        this.mPinLockView2.setPinLength(5);
        this.mPinLockView2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        this.mIndicatorDots2.setIndicatorType(2);

        this.mPinLockView3 = (PinLockView) findViewById(R.id.pin_lock_view3);
        this.mIndicatorDots3 = (IndicatorDots) findViewById(R.id.indicator_dots3);
        this.mPinLockView3.attachIndicatorDots(this.mIndicatorDots3);
        this.mPinLockView3.setPinLockListener(this.mPinLockListener);
        this.mPinLockView3.setPinLength(5);
        this.mPinLockView3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        this.mIndicatorDots3.setIndicatorType(2);

    }
}