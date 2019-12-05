package com.uccendigital.secure.app;

import android.content.Context;

public class s {
    Context context;

    public s (Context context) {
        this.context = context;
    }

    public int extractIffer (int iffer) {
        final int i = (iffer - 456) / 852;
        return i;
    }

    public int putIffer (int iffer) {
        final int i = (iffer * 852) + 456;
        return i;
    }

}
