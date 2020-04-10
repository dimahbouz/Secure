package com.uccendigital.secure.services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.uccendigital.secure.app.App;
import com.uccendigital.secure.app.Hadher;
import com.uccendigital.secure.app.SharedManager;

import java.util.Date;

public class StoreService extends Service {
    public StoreService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        SharedManager AppShared = new SharedManager(getApplicationContext(), "app");
        int fs = AppShared.getInteg("fs");

        Date date = new Date();
        int now = (int) date.getTime();

        int modulo = (now - fs)%(1000*30);

        Toast.makeText(this, "Service is starting", Toast.LENGTH_SHORT).show();
        reduceStore(modulo);

        throw new UnsupportedOperationException("Not yet implemented");

    }

    private void reduceStore (int modulo) {

        Toast.makeText(this, "reduceStore modulo : " + modulo, Toast.LENGTH_SHORT).show();
        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                updateStore();
            }
        },modulo);

    }

    private void updateStore () {

        Toast.makeText(this, "store is updated", Toast.LENGTH_SHORT).show();
        Hadher hadher = new Hadher(getApplicationContext());
        int oldPoints = hadher.extractPoints();
        int newPoints = oldPoints - 5;

        if (oldPoints > 0) {
            hadher.putPoints(oldPoints - 5);
        }

        App app = new App(getApplicationContext());

        if (newPoints == 20) {
            app.SimpleNotify(001,"Solde de magasin", "Title de solde", "Votre solde dans le magasin est désormais à 20 points, entrer pour en gagner plus", true);
        }
        if (newPoints == 10) {
            app.SimpleNotify(002,"Solde de magasin", "Title de solde", "Votre solde dans le magasin est désormais à 10 points, entrer pour en gagner plus", true);
        }
        if (newPoints == 0) {
            app.SimpleNotify(003, "Solde de magasin", "Title de solde", "Votre solde dans le magasin est désormais à 0 points, entrer pour en gagner plus", true);
            app.SimpleNotify(001, "Sécurité", "L'application est désactiver", "Cliquer pour la réactiver à nouveau", true);

            new SharedManager(getApplicationContext(), "app").putbool("enable", false);

            Intent i = new Intent(getApplicationContext(), StoreService.class);
            stopService(i);

        } else {
            reduceStore(1000*30);
        }
    }
}
