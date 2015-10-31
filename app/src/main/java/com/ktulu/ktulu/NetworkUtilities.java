package com.ktulu.ktulu;

import android.net.wifi.WifiManager;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkUtilities {
    public static String ipAddressFromInt(int ip) {
        byte[] bytes = BigInteger.valueOf(ip).toByteArray();

        StringBuilder stringBuilder = new StringBuilder();

        List<Byte> byteList = new ArrayList<>();
        for (byte b : bytes) {
            byteList.add(b);
        }

        Collections.reverse(byteList);

        for (byte b : byteList) {
            int x = (b + 256) % 256;
            stringBuilder
                    .append(String.valueOf(x))
                    .append(".");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static boolean isTetheringOn(WifiManager wifiManager) {
        try {
            Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();
            for (Method method : wmMethods)
                if (method.getName().equals("isWifiApEnabled")) {
                    boolean isOn = (Boolean) method.invoke(wifiManager);
                    return isOn;
                }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
