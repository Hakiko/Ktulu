package com.starsep.ktulu;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView ip;

    private class CheckMyIp extends AsyncTask<Void, Void, Void> {
        private String myIpNumber;
        private WifiInfo wifiInfo;
        private String ssid;
        private String gatewayIp;
        private String serverIp;
        private String netmask;
        private WifiManager wifiManager;
        private boolean tetheringOn;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Log.d("Ktulu", "LEL");
            myIpNumber = "";
            wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            wifiInfo = wifiManager.getConnectionInfo();

            myIpNumber = NetworkUtilities.ipAddressFromInt(wifiInfo.getIpAddress());
            ssid = wifiInfo.getSSID();
            gatewayIp = NetworkUtilities.ipAddressFromInt(wifiManager.getDhcpInfo().gateway);
            tetheringOn = NetworkUtilities.isTetheringOn(wifiManager);
            serverIp = NetworkUtilities.ipAddressFromInt(wifiManager.getDhcpInfo().serverAddress);
            netmask = NetworkUtilities.ipAddressFromInt(wifiManager.getDhcpInfo().netmask);

            Log.d(AppInfo.LOG_TAG, myIpNumber);

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
                    .append("\nServer IP: ")
                    .append(serverIp)
                    .append("\nNetmask: ")
                    .append(netmask)
                    .append("\nSSID: ")
                    .append(ssid)
                    .append("\nTethering: ")
                    .append(tetheringOn ? "On" : "Off");

            ip.setText(stringBuilder.toString());
        }
    }

    private void makeUi() {
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
        Button refreshButton = (Button) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckMyIp().execute();
            }
        });

        Button showCardButton = (Button) findViewById(R.id.showCardButton);
        showCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowCardActivity.class);
                startActivity(intent);
            }
        });

        Button findServerButton = (Button) findViewById(R.id.findServerButton);
        findServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindServerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startServerService() {
        Intent intent = new Intent(MainActivity.this, ServerService.class);
        startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeUi();
        new CheckMyIp().execute();
        startServerService();
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
