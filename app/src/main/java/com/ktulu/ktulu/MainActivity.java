package com.ktulu.ktulu;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView ip;

    private class CheckMyIp extends AsyncTask<Void, Void, Void> {
        private String myIpNumber;
        private WifiInfo wifiInfo;
        private String ssid;
        private String gatewayIp;
        private WifiManager wifiManager;
        private boolean tetheringOn;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private boolean isTetheringOn() {
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

        private String ipAddressFromInt(int ip) {
            byte[] bytes = BigInteger.valueOf(ip).toByteArray();

            StringBuilder stringBuilder = new StringBuilder();

            List<Byte> byteList = new ArrayList<Byte>();
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

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("Ktulu", "LEL");
            myIpNumber = "";
            wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            wifiInfo = wifiManager.getConnectionInfo();

            myIpNumber = ipAddressFromInt(wifiInfo.getIpAddress());
            ssid = wifiInfo.getSSID();
            gatewayIp = ipAddressFromInt(wifiManager.getDhcpInfo().gateway);
            tetheringOn = isTetheringOn();

            Log.d("Ktulu", myIpNumber);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                    .append("IP Number: ")
                    .append(myIpNumber)
                    .append("\nGateway IP: ")
                    .append(gatewayIp)
                    .append("\nSSID: ")
                    .append(ssid)
                    .append("\nTethering: ")
                    .append(tetheringOn ? "On" : "Off");

            ip.setText(stringBuilder.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ip = (TextView) findViewById(R.id.ip);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        new CheckMyIp().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
