package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils;

import android.util.Base64;

public class AndroidBase64 {
    public static String encode(byte[] message) {
        return Base64.encodeToString(message, Base64.DEFAULT);
    }

    public static byte[] decode(String message) {
        return Base64.decode(message.getBytes(), Base64.DEFAULT);
    }
}
