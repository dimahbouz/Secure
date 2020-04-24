package com.uccendigital.secure.elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.uccendigital.secure.R;
import com.uccendigital.secure.app.Functions;
import com.uccendigital.secure.app.Hadher;
import com.uccendigital.secure.app.SharedManager;

import java.util.Calendar;

public class Dialog {

    LinearLayout linearHelp, linearStore;
    TextView pointsStore, showVideoBtn, dialogTitle;
    ImageView btnclose;
    private Activity activity;
    private Context context;
    private AlertDialog alert;
    View view;

    // ca....917 -> TestAd
    // ca-app-pub-3940256099942544/5224354917
    private static final String VAD_UNIT_ID = "ca-app-pub-2264850049980959/2030164442";
    private RewardedAd rewardedAd;

    Hadher hadher;

    public Dialog(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void show (String show) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.dialog, null);

        linearHelp = view.findViewById(R.id.linearHelp);
        linearStore = view.findViewById(R.id.linearStore);
        dialogTitle = view.findViewById(R.id.dialogTitle);

        if (show.equals("help")) {
            setupHelp();
        } else {
            setupStore();
        }

        builder.setView(view);
        builder.setCancelable(true);

        alert = builder.create();
        alert.show();

        btnclose = view.findViewById(R.id.btnclose);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
    }

    private void setupHelp () {

        linearHelp.setVisibility(View.VISIBLE);
        linearStore.setVisibility(View.GONE);
        dialogTitle.setText(R.string.help);

        final CheckBox helpCheckbox = view.findViewById(R.id.helpcheckbox);
        Button helpOk = view.findViewById(R.id.helpOk);

        helpOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helpCheckbox.isChecked()) {
                    new SharedManager(context, "app").putbool("help", true);
                }
                alert.cancel();
            }
        });

    }

    private void setupStore () {
        linearStore.setVisibility(View.VISIBLE);
        linearHelp.setVisibility(View.GONE);
        dialogTitle.setText(R.string.store);

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
        hadher.addPoints(coins);

        if (oldPoints == 0) {

            SharedManager AppShared = new SharedManager(context, "app");
            long now = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now);
            int minutes = calendar.get(Calendar.MINUTE);
            AppShared.putInt("minute",minutes);
            AppShared.putLong("fs",now);
            AppShared.putLong("last",now);

            new Functions(context).setStoreManager(0);

        }

        pointsStore.setText(String.valueOf(hadher.extractPoints()));

    }

}
