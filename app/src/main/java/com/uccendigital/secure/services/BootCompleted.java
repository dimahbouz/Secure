package com.uccendigital.secure.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.uccendigital.secure.app.Hadher;
import com.uccendigital.secure.app.SharedManager;

public class BootCompleted extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        /*
        Demarrer les services et les broadcasts
        Verifier les cartes sim placer
         */

        SharedManager AppShared = new SharedManager(context, "app");
        Hadher hadher = new Hadher(context);
        if (hadher.checkIffer("assiwel") && AppShared.getBool("hide_app")) {
            context.sendBroadcast(new Intent(context, CallReceiver.class));
        }
/*
        if (hadher.extractPoints() != 0) {
            Toast.makeText(context, "Lanch StoreService from BootCompleted ... ", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, StoreService.class);
            context.startService(i);
        }
 */
    }
}
