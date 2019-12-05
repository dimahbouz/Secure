package com.uccendigital.secure.app;

import android.content.Context;
import android.content.SharedPreferences;

public class sharedManager {
    Context context;
    SharedPreferences.Editor editor = this.prefs.edit();
    SharedPreferences prefs;

    public sharedManager(Context context, String pref) {
        this.context = context;
        this.prefs = context.getSharedPreferences(pref, 0);
    }

    public void putInt(String index, int value) {
        this.editor.putInt(index, value).commit();
    }

    public int getInteg(String index) {
        return this.prefs.getInt(index, -1);
    }

    public void putStr(String index, String value) {
        this.editor.putString(index, value).commit();
    }

    public String getStr(String index) {
        return this.prefs.getString(index, "");
    }

    public void putbool(String index, boolean value) {
        this.editor.putBoolean(index, value).commit();
    }

    public boolean getBool(String index) {
        return this.prefs.getBoolean(index, false);
    }
}
