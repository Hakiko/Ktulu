package com.ktulu.ktulu;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView ip;

    private class CheckMyIp extends AsyncTask<Void, Void, Void> {
        private String ipNumber;
        private WifiInfo wifiInfo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private String ipAddressFromInt(int ip) {
            byte[] bytes = BigInteger.valueOf(ip).toByteArray();

            StringBuilder stringBuilder = new StringBuilder();

            List<Byte> byteList = new ArrayList<Byte>();
            for(byte b : bytes) {
                byteList.add(b);
            }

            Collections.reverse(byteList);

            for(byte b : byteList) {
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
            ipNumber = "";
            WifiManager mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            wifiInfo = mainWifi.getConnectionInfo();

            ipNumber = ipAddressFromInt(wifiInfo.getIpAddress());

            Log.d("Ktulu", ipNumber);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ip.setText("IP Number: " + ipNumber);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ip = (TextView)findViewById(R.id.ip);

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
