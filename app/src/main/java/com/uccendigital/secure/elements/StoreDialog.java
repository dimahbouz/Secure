package com.uccendigital.secure.elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.uccendigital.secure.R;
import com.uccendigital.secure.app.Hadher;
import com.uccendigital.secure.app.SharedManager;
import com.uccendigital.secure.services.StoreService;

import java.util.Date;

public class StoreDialog {

    TextView btnclose, pointsStore, showVideoBtn;
    private Activity activity;
    private Context context;
    private AlertDialog alert;

    private static final String VAD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private RewardedAd rewardedAd;

    Hadher hadher;

    public StoreDialog(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void show () {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.store, null);

        hadher = new Hadher(context);

        pointsStore = view.findViewById(R.id.pointsStore);
        pointsStore.setText(String.valueOf(hadher.extractPoints()));

        showVideoBtn = view.findViewById(R.id.showVideoBtn);

        loadVideo();

        showVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rewardedAd.isLoaded()) {
                    showVideo();
                } else {
                    loadVideo();
                }
            }
        });

        builder.setView(view);
        builder.setCancelable(true);

        alert = builder.create();
        alert.show();

        btnclose = view.findViewById(R.id.btnclose);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }

    private void loadVideo () {

        if (rewardedAd == null || !rewardedAd.isLoaded()) {
            rewardedAd = new RewardedAd(context, VAD_UNIT_ID);
            rewardedAd.loadAd(new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
                @Override
                public void onRewardedAdLoaded() {
                    // Ad successfully loaded.
                    showVideoBtn.setBackgroundColor(context.getResources().getColor(R.color.colorAccent, context.getTheme()));
                }

                @Override
                public void onRewardedAdFailedToLoad(int errorCode) {
                    // Ad failed to load.
                    showVideoBtn.setBackgroundColor(context.getResources().getColor(R.color.colorGrey, context.getTheme()));
                }
            });
        }

    }

    private void showVideo () {

        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {

                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    showVideoBtn.setBackgroundColor(context.getResources().getColor(R.color.colorGrey, context.getTheme()));
                    loadVideo();
                }

                @Override
                public void onUserEarnedReward(RewardItem rewardItem) {
                    // User earned reward.
                    updateStore(rewardItem.getAmount());
                }

                @Override
                public void onRewardedAdFailedToShow(int errorCode) {
                    // Ad failed to display
                }
            };
            rewardedAd.show(activity, adCallback);
        }
    }

    private void updateStore(int coins) {

        Hadher hadher = new Hadher(context);
        int oldPoints = hadher.extractPoints();
        hadher.putPoints(coins);

        if (oldPoints == 0) {

            Date date = new Date();
            int now = (int) date.getTime();

            new SharedManager(context, "app").putInt("fs",now);

            /*
            Toast.makeText(context, "Lanch StoreService from UpdateStore ... ", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, StoreService.class);
            context.startService(i);

             */

        }

        pointsStore.setText(String.valueOf(hadher.extractPoints()));

    }

}
