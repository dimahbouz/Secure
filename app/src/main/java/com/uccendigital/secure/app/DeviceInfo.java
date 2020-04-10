package com.uccendigital.secure.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

import androidx.annotation.Nullable;

import java.util.List;

public class DeviceInfo {

    Context context;

    public DeviceInfo (Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    public int simCount () {

        SubscriptionManager sm = SubscriptionManager.from(context);
        return sm.getActiveSubscriptionInfoCount();

    }

    @SuppressLint("MissingPermission")
    @Nullable
    public List<SubscriptionInfo> getSubscriptions() {

        SubscriptionManager sm = SubscriptionManager.from(context);
        return sm.getActiveSubscriptionInfoList();

    }
}
