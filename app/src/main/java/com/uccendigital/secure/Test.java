package com.uccendigital.secure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

public class Test extends AppCompatActivity {

    public static final String TAG = "PinLockView";
    private IndicatorDots mIndicatorDots;
    private PinLockView mPinLockView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        PinLockListener mPinLockListener = new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                Log.d(TAG, "Pin complete: " + pin);
                Toast.makeText(getApplicationContext(),"onComplete",0).show();
            }

            @Override
            public void onEmpty() {
                Log.d(TAG, "Pin empty");
                Toast.makeText(getApplicationContext(),"onEmpty",0).show();
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
                Toast.makeText(getApplicationContext(),"onPinChange",0).show();
            }
        };

        this.mPinLockView = findViewById(R.id.pin_lock_view);
        this.mIndicatorDots = findViewById(R.id.indicator_dots);
        this.mPinLockView.attachIndicatorDots(this.mIndicatorDots);
        this.mPinLockView.setPinLockListener(mPinLockListener);
        this.mPinLockView.setPinLength(5);
        this.mPinLockView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
        this.mIndicatorDots.setIndicatorType(2);

    }
}
