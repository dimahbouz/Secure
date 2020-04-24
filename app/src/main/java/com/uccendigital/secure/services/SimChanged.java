package com.uccendigital.secure.services;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.uccendigital.secure.app.App;
import com.uccendigital.secure.app.Functions;
import com.uccendigital.secure.app.Hadher;
import com.uccendigital.secure.app.SharedManager;
import com.uccendigital.secure.elements.Sim;

import java.util.List;

public class SimChanged extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        /*
        * Verifier les nouvelles cartes sim placer
        * et envoyer un sms en cas de nouvelles sim detecter
         */

        SharedManager AppShared = new SharedManager(context, "app");
        App app = new App(context.getApplicationContext());

        if (app.checkPerm(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS})) {
            Hadher hadher = new Hadher(context);

            if (hadher.checkIffer("achegue3") && AppShared.getBool("enable")) {

                app.verifAndSend();

            }
        }
    }
}
