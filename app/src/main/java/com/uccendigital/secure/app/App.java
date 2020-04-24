package com.uccendigital.secure.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.uccendigital.secure.LockActivity;
import com.uccendigital.secure.MainActivity;
import com.uccendigital.secure.R;
import com.uccendigital.secure.elements.Sim;

import java.util.List;

public class App extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST = 2398;
    private static final int MY_UPDATE_REQUEST = 2399;
    Context context;

    public App (Context ctx) {this.context = ctx;}

    @SuppressLint("WrongConstant")
    public void hideApp (Boolean hide) {

        // if AppShared = true => Application masquer
        SharedManager AppShared = new SharedManager(context, "app");
        int i = 1;
        if (hide) {i = 2;}
        context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, MainActivity.class), i, 1);
        AppShared.putbool("hide_app", hide);
        
    }

    public boolean checkPerm(String[] permissions) {

        int permissionCheck = 0;
        for (String permission : permissions) {

            permissionCheck += ContextCompat.checkSelfPermission(context, permission);

        }

        if (permissionCheck == 0) {
            return true;
        }
        return false;
    }

    @SuppressLint("WrongConstant")
    public void checkPermissions(final String[] permissions) {

        int permissionCheck = 0;
        Boolean showRequestPermissions = false;
        for (String permission : permissions) {

            permissionCheck += ContextCompat.checkSelfPermission(context, permission);
            if (showRequestPermissions || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                showRequestPermissions = true;
            } else {
                showRequestPermissions = false;
            }

        }

        if (permissionCheck == 0) {

        } else if (showRequestPermissions) {

            final Intent i = new Intent();
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setData(Uri.parse("package:" + context.getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(i);

        } else {

            ActivityCompat.requestPermissions((Activity) context, permissions, MY_PERMISSIONS_REQUEST);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(context, "Permissions was granted",0).show();
                } else {
                    Toast.makeText(context, "Permissions was not granted",0).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }

    }

    public void checkUpdate () {

        // Creates instance of the manager.
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                                (Activity) context,
                            // Include a request code to later monitor this update request.
                            MY_UPDATE_REQUEST);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }else if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){

                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                appUpdateInfo,
                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                AppUpdateType.FLEXIBLE,
                                // The current activity making the update request.
                                (Activity) context,
                                // Include a request code to later monitor this update request.
                                MY_UPDATE_REQUEST);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @SuppressLint("WrongConstant")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_UPDATE_REQUEST) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Update flow failed! Result code: " + resultCode,0).show();
                Log.e("UPDATE_STATUS","Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    public void SimpleNotify(int id, String subtext, String title, String text, Boolean red) {

        int notificationId = id;
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle = bigTextStyle.bigText(text);

        Builder notificationBuilder = new Builder(context).setDefaults(-1).setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title).setContentText(text)
                .setStyle(bigTextStyle);

        if (red) {
            Intent i = new Intent(context, LockActivity.class);
            i.putExtra("CHANGE", false);
            notificationBuilder.setContentIntent(PendingIntent.getActivity(context, 0, i, 0));
        }

        NotificationManagerCompat.from(context).notify(notificationId, notificationBuilder.build());
    }

    public void verifAndSend () {

        Boolean send = false;
        SharedManager AppShared = new SharedManager(context, "app");
        Hadher hadher = new Hadher(context);
        int oldPoints = hadher.extractPoints();

        long last = AppShared.getLong("last");

        long now = System.currentTimeMillis();

        long interval = 1000*60*60*24;

        long bLast = now - last;
        long days = (bLast/interval);
        if (days >= 1) {

            int points = (int) (days * 10);
            if (points > oldPoints) {
                send = true;
            }

        }

        List<Sim> newSimList = new Functions(context).getNewSim();

        if (oldPoints > 0 && !send && !newSimList.isEmpty()){
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

            Boolean sent = false;
            String newSim1 = "", newSim2 = "";
            if (!sim1.equals("") && !sim1.equals(lastsim1) && !sim1.equals(lastsim2)) {
                newSim1 = sim1;
                sent = true;
            }

            if (newSimList.size() > 1) {
                if (!sim2.equals("") && !sim2.equals(lastsim1) && !sim2.equals(lastsim2)) {
                    if (newSim1.equals("")) {
                        newSim1 = sim2;
                    } else newSim2 = sim2;
                    sent = true;
                }
            }

            if (sent) {
                sendSMS(newSim1, newSim2);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void sendSMS(final String sim1Serial, final String sim2Serial) {

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = "0124578963";

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            deviceId = tm.getImei();
        } else {
            deviceId = tm.getDeviceId();
        }

        String security_num = new Hadher(context).extractIffer("achegue3");

        // PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, Object.class), 0);

        String text = context.getResources().getString(R.string.sms1);
        text += "\n" + context.getResources().getString(R.string.sms2);
        if (!sim2Serial.equals("")) {
            text += "1 : " + sim1Serial + "\n" + "Sim 2 : " + sim2Serial;
        } else {
            text += " : " + sim1Serial;
        }
        text += "\n" + context.getResources().getString(R.string.sms3) + " " + deviceId;
        text += "\n" + context.getResources().getString(R.string.endSms);

            String SENT = "SMS_SENT";

            PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);

            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    int resultCode = getResultCode();
                    switch (resultCode) {
                        case Activity.RESULT_OK:

                            new SharedManager(context, "app").putStr("LastSim1", sim1Serial);
                            if (!sim2Serial.equals("")) {
                                new SharedManager(context, "app").putStr("LastSim2", sim2Serial);
                            }

                            Toast.makeText(context, "SMS sent", Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(context, "Generic failure", Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(context, "No service", Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(context, "Null PDU", Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(context, "Radio off", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }, new IntentFilter(SENT));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(security_num, null, text, sentPI, null);

    }

}
