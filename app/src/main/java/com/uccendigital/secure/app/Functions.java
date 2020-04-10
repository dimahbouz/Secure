package com.uccendigital.secure.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.telephony.SubscriptionInfo;
import android.util.Log;

import com.uccendigital.secure.elements.Sim;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Functions {

    Context context;

    public Functions (Context context) {
        this.context = context;
    }

    public void shareApplication () {

        ApplicationInfo app = context.getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let'Hadher use "*/*" instead.
        intent.setType("*/*");

        // Append file and send Intent
        File originalApk = new File(filePath);

        try {
            //Make new directory in new location
            File tempFile = new File(context.getExternalCacheDir() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application'Hadher name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + context.getString(app.labelRes).replace(" ","").toLowerCase() + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            //Open share dialog
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            context.startActivity(Intent.createChooser(intent, "Share application via"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addWhiteList (Sim sim) {
        SharedManager AppShared = new SharedManager(context, "app");

        List<Sim> simList = getWhiteList();

        if (!simList.isEmpty()) {

            Boolean newSim = true;
            for (int j = 0; j < simList.size(); j++) {
                Sim sim1 = simList.get(j);

                if (newSim && sim.getSerial().equals(sim1.getSerial())) {
                    newSim = false;
                }
            }

            if (newSim) {
                simList.add(simList.size(), sim);
            }
        } else {
            simList.add(simList.size(), sim);
        }

        JSONObject listJSON = new JSONObject();
        try {
            for (int i = 0; i < simList.size(); i++) {
                JSONObject simJSON = new JSONObject();
                simJSON.accumulate("name", simList.get(i).getName());
                simJSON.accumulate("serial", simList.get(i).getSerial());
                listJSON.accumulate(String.valueOf(i),simJSON);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppShared.putStr("whitelist", listJSON.toString());

    }

    public void removeWhiteList (int position) {

        SharedManager AppShared = new SharedManager(context, "app");

        String whitelist = AppShared.getStr("whitelist");

        try {
            JSONObject whitelistJSON = new JSONObject(whitelist);
            whitelistJSON.remove(String.valueOf(position));

            AppShared.putStr("whitelist", whitelistJSON.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public List<Sim> getWhiteList () {
        SharedManager AppShared = new SharedManager(context, "app");
        String whitelist = AppShared.getStr("whitelist");

        List<Sim> simList = new ArrayList<>();

        if (!whitelist.isEmpty()) {
            try {
                JSONObject whitelistJSON = new JSONObject(whitelist);

                for (int i = 0; i < whitelistJSON.length(); i++) {

                    JSONObject simJSON = whitelistJSON.getJSONObject(String.valueOf(i));
                    String name = simJSON.getString("name");
                    String serial = simJSON.getString("serial");

                    simList.add(new Sim(name, serial));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return simList;
    }

    public List<Sim> getNewSim() {

        App app = new App(context);

        List<Sim> newList = new ArrayList<>();

        if (app.checkPerm(new String[] {Manifest.permission.READ_PHONE_STATE})) {
            DeviceInfo deviceInfo = new DeviceInfo(context);
            List<SubscriptionInfo> simInfo = deviceInfo.getSubscriptions();
            List<Sim> whitelist = this.getWhiteList();

            if (deviceInfo.simCount() > 0) {

                for (int i = 0; i < simInfo.size(); i++) {
                    String newName = simInfo.get(i).getDisplayName().toString();
                    String newSerial = simInfo.get(i).getIccId();

                    if (!whitelist.isEmpty()) {

                        Boolean newSim = true;
                        for (int j = 0; j < whitelist.size(); j++) {
                            Sim sim = whitelist.get(j);

                            if (newSim && newSerial.equals(sim.getSerial())) {
                                newSim = false;
                            }
                        }

                        if (newSim) {
                            newList.add(new Sim(newName, newSerial));
                        }
                    } else {
                        newList.add(new Sim(newName, newSerial));
                    }
                }
            }
        }

        return newList;
    }
}
