package com.uccendigital.secure.services;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.uccendigital.secure.LockActivity;
import com.uccendigital.secure.app.App;
import com.uccendigital.secure.app.Hadher;

public class CallReceiver extends BroadcastReceiver {

    Hadher hadher;
    @Override
    public void onReceive(Context context, Intent intent) {

        App app = new App(context);
        if (app.checkPerm(new String[] {Manifest.permission.READ_PHONE_STATE})) {
            hadher = new Hadher(context);
            String luancher_num = hadher.extractIffer("assiwel");

            if (luancher_num.equals(intent.getStringExtra("android.intent.extra.PHONE_NUMBER"))) {

                Intent i = new Intent(context, LockActivity.class);
                i.putExtra("CHANGE", false);
                context.startActivity(i);

            }

        }

    }

}
