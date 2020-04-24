package com.uccendigital.secure.services;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.uccendigital.secure.app.App;
import com.uccendigital.secure.app.Functions;
import com.uccendigital.secure.app.Hadher;
import com.uccendigital.secure.app.SharedManager;
import com.uccendigital.secure.elements.Sim;

import java.util.List;

public class BootCompleted extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        /*
        * Demarrer les services et les broadcasts
        * Verifier les cartes sim
         */

        SharedManager AppShared = new SharedManager(context, "app");
        Hadher hadher = new Hadher(context);
        App app = new App(context.getApplicationContext());

        int a = hadher.extractPoints();

        if (a > 0) {
            new Functions(context).setStoreManager(1);
        }

        if (app.checkPerm(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS})) {

            if (hadher.checkIffer("achegue3") && AppShared.getBool("enable")) {

                app.verifAndSend();

            }
        }
    }
}
