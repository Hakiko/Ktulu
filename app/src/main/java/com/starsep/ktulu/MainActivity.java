package com.starsep.ktulu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView ipTextView;
    private TextView nameTextView;
    private SharedPreferences settings;
    private int netmaskNumber;

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
            netmaskNumber = wifiManager.getDhcpInfo().netmask;
            netmask = NetworkUtilities.ipAddressFromInt(netmaskNumber);

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

            ipTextView.setText(stringBuilder.toString());
        }
    }

    private void makeUi() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ipTextView = (TextView) findViewById(R.id.ip);

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
                intent.putExtra("Netmask", netmaskNumber);
                startActivity(intent);
            }
        });

        TextView version = (TextView) findViewById(R.id.version);
        version.setText("Ktulu v" + BuildConfig.VERSION_NAME);

        nameTextView = (TextView) findViewById(R.id.name);
        updateNameUi(null);
    }

    public void updateNameUi(String name) {
        if(name != null) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("name", name);
            editor.apply();
            //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
        }
        nameTextView.setText(settings.getString("name", "Default Value"));
    }

    private void startServerService() {
        Intent intent = new Intent(MainActivity.this, ServerService.class);
        startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getPreferences(MODE_PRIVATE);
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
        if (id == R.id.action_change_name) {
            //Toast.makeText(getApplicationContext(), "Change name clicked", Toast.LENGTH_LONG).show();
            ChangeNameDialog dialog = new ChangeNameDialog();
            dialog.setOldName(settings.getString("name", getString(R.string.defaultName)));
            dialog.show(getSupportFragmentManager(), "ChangeNameDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
