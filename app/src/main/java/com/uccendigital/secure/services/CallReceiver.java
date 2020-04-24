package com.uccendigital.secure.services;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.uccendigital.secure.LockActivity;
import com.uccendigital.secure.app.App;
import com.uccendigital.secure.app.Hadher;
import com.uccendigital.secure.app.SharedManager;

import java.lang.reflect.Method;

public class CallReceiver extends BroadcastReceiver {

    Hadher hadher;
    @Override
    public void onReceive(Context context, Intent intent) {

        App app = new App(context);
        if (app.checkPerm(new String[] {Manifest.permission.READ_PHONE_STATE})) {
            hadher = new Hadher(context);
            String luancher_num = hadher.extractIffer("assiwel");
            Boolean hide = new SharedManager(context, "app").getBool("hide_app");

            if (hide && luancher_num.equals(intent.getStringExtra("android.intent.extra.PHONE_NUMBER"))) {

                Intent i = new Intent(context, LockActivity.class);
                i.putExtra("CHANGE", false);
                context.startActivity(i);

                killCall(context.getApplicationContext());

            }

        }

    }

    public boolean killCall(Context context) {
        try {
            // Get the boring old TelephonyManager
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // Get the getITelephony() method
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);

            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception ex) { // Many things can go wrong with reflection calls
            Log.d("CallReceiver Error", "PhoneStateReceiver **" + ex.toString());
            return false;
        }
        return true;

    }

}
