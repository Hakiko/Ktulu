package com.starsep.ktulu;

import android.net.wifi.WifiManager;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkUtilities {
    public static String ipAddressFromInt(int ip) {
        return  ((ip & 0xFF) + "." +
                ((ip >>>= 8) & 0xFF) + "." +
                ((ip >>>= 8) & 0xFF) + "." +
                ((ip >>>= 8) & 0xFF));
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
