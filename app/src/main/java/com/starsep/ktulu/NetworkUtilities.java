package com.starsep.ktulu;

import android.net.wifi.WifiManager;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkUtilities {
    public static int reverseIp(int ip) {
        return (((ip & 0xFF) << 24) +
                (((ip >>>= 8) & 0xFF) << 16) +
                (((ip >>>= 8) & 0xFF) << 8) +
                ((ip >>>= 8) & 0xFF));
    }

    public static String ipAddressFromInt(int ip) {
        return ((ip & 0xFF) + "." +
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

    public static int addressesInNetwork(final int netmask) {
        int log2Addresses = 32;
        for (int net = netmask; net % 2 == 1; net /= 2) {
            log2Addresses--;
        }
        int result = 1;
        while (log2Addresses-- > 0) {
            result *= 2;
        }
        return result;
    }
}
