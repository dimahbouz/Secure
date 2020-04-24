package com.uccendigital.secure.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.uccendigital.secure.app.App;
import com.uccendigital.secure.app.Functions;
import com.uccendigital.secure.app.Hadher;
import com.uccendigital.secure.app.SharedManager;

public class StoreReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Hadher hadher = new Hadher(context);
        int oldPoints = hadher.extractPoints();

        if (oldPoints > 0) {
            SharedManager AppShared = new SharedManager(context, "app");
            App app = new App(context);

            hadher.sousPoints(10,System.currentTimeMillis());

            int newPoints = oldPoints - 10;

            if (newPoints == 20 && !AppShared.getBool("notifications")) {
                app.SimpleNotify(001,"Solde de magasin", "Title de solde", "Vos points sont à 20, entrer pour en gagner plus", true);
            }
            if (newPoints == 10 && !AppShared.getBool("notifications")) {
                app.SimpleNotify(001,"Solde de magasin", "Title de solde", "Vos points sont désormais à 10, entrer pour en gagner plus", true);
            }
            if (newPoints == 0) {

                AppShared.putbool("enable", false);

                if (!AppShared.getBool("notifications")) {
                    app.SimpleNotify(001, "Solde de magasin", "Title de solde", "Votre solde de points est épuisé, entrer pour en gagner plus", true);
                }
                app.SimpleNotify(002, "Sécurité", "L'application désactiver", "Vos points sont 0, l'application est désactiver", true);

                if (hadher.extractPoints() == 0) {
                    new Functions(context).cancelStoreManager();
                }
            }

        }
    }
}
