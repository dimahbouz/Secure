package com.uccendigital.secure.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedManager {
    Context context;
    SharedPreferences prefs;
    Editor editor;

    public SharedManager(Context context, String pref) {
        this.context = context;
        this.prefs = context.getSharedPreferences(pref, 0);
        this.editor = this.prefs.edit();
    }

    public void putInt(String index, int value) {
        this.editor.putInt(index, value).commit();
    }

    public int getInteg(String index) {
        return this.prefs.getInt(index, -1);
    }

    public void putLong(String index, long value) {
        this.editor.putLong(index, value).commit();
    }

    public long getLong(String index) {
        return this.prefs.getLong(index, -1);
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
