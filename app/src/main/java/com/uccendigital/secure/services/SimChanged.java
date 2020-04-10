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
        Verifier les nouvelles cartes sim placer
         */

        SharedManager AppShared = new SharedManager(context, "app");
        App app = new App(context);

        if (app.checkPerm(new String[]{Manifest.permission.READ_PHONE_STATE})) {
            Hadher hadher = new Hadher(context);

            if (hadher.checkIffer("achegue3") && AppShared.getBool("enable")) {

                List<Sim> newSimList = new Functions(context).getNewSim();

                if (!newSimList.isEmpty()){
                    String sim1 = "", sim2 = "";

                    for (int i = 0; i < newSimList.size(); i++) {
                        if (i == 0) {
                            sim1 = newSimList.get(i).getSerial();
                        } else if (i == 1) {
                            sim2 = newSimList.get(i).getSerial();
                        }
                    }

                    String lastsim1 = AppShared.getStr("LastSim1");
                    String lastsim2 = AppShared.getStr("LastSim2");

                    String newSim1 = "", newSim2 = "";
                    if (!sim1.equals(lastsim1) && !sim1.equals(lastsim2)) {
                        newSim1 = sim1;
                    }

                    if (!sim2.equals(lastsim1) && !sim2.equals(lastsim2)) {
                        newSim2 = sim2;
                    }

                    app.sendSMS(newSim1, newSim2);
                }
            }
        }

    }
}
