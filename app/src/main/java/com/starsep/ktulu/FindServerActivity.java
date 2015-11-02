package com.starsep.ktulu;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class FindServerActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private int netmask;
    private int addressesInNetwork;

    private void updateProgressBar(int k) {
        progressBar.setProgress(k);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_server);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        netmask = getIntent().getIntExtra("Netmask", 0);
        addressesInNetwork = NetworkUtilities.addressesInNetwork(netmask);
        progressBar.setMax(addressesInNetwork - 1);

        new AsyncTask<Void, Void, List<Integer>>() {
            private int myIpNumber;

            private void checkWifi() {
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                myIpNumber = wifiInfo.getIpAddress();
            }

            private List<Thread> threads;

            @Override
            protected List<Integer> doInBackground(Void... params) {
                checkWifi();
                int initialIp = myIpNumber & netmask;
                Log.d(AppInfo.LOG_TAG, NetworkUtilities.ipAddressFromInt(myIpNumber));
                //Log.d(AppInfo.LOG_TAG, NetworkUtilities.ipAddressFromInt(NetworkUtilities.reverseIp(netmask)));
                //Log.d(AppInfo.LOG_TAG, NetworkUtilities.ipAddressFromInt(initialIp));
                final List<Integer> result = new ArrayList<>();
                threads = new ArrayList<>();
                for(int i = 2; i < 10; i++) {
                    final int k = i;
                    final int ip = NetworkUtilities.reverseIp(NetworkUtilities.reverseIp(initialIp) + i);
                    //Log.d(AppInfo.LOG_TAG, NetworkUtilities.ipAddressFromInt(ip));
                    Thread nextThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(AppInfo.LOG_TAG, "Here I start " + String.valueOf(k));
                            String ipName = NetworkUtilities.ipAddressFromInt(ip);
                            Socket socket = null;
                            Log.d(AppInfo.LOG_TAG, "Socket " + String.valueOf(k));
                            try {
                                socket = new Socket(InetAddress.getByName(ipName), 1337);
                            } catch (IOException e) {
                                Log.d(AppInfo.LOG_TAG, "Ending... " + String.valueOf(k));
                                return;
                            }
                            Log.d(AppInfo.LOG_TAG, "InputStream " + String.valueOf(k));
                            InputStream inputStream;
                            try {
                                inputStream = socket.getInputStream();
                            } catch (IOException e) {
                                Log.d(AppInfo.LOG_TAG, "Ending... " + String.valueOf(k));
                                return;
                            }

                            Log.d(AppInfo.LOG_TAG, "Input " + String.valueOf(k));
                            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
                            String line = null;
                            try {
                                line = input.readLine();
                                Log.d(AppInfo.LOG_TAG, line);
                            } catch (IOException e) {
                                Log.d(AppInfo.LOG_TAG, "Ending... " + String.valueOf(k));
                                return;
                            }
                            if(line.equals("I am server")) {
                                result.add(ip);
                                Log.d(AppInfo.LOG_TAG, NetworkUtilities.ipAddressFromInt(ip));
                            }
                            Log.d(AppInfo.LOG_TAG, "Ending... " + String.valueOf(k));
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    threads.add(nextThread);
                    nextThread.start();
                }
                for (int i = 0; i < threads.size(); i++) {
                    Thread thread = threads.get(i);
                    Log.d(AppInfo.LOG_TAG, "THREAD " + String.valueOf(i + 2));
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d(AppInfo.LOG_TAG, "LEL");
                    }
                    updateProgressBar(i);
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Integer> servers) {
                super.onPostExecute(servers);
                progressBar.setVisibility(View.GONE);
                TextView noServers = (TextView) findViewById(R.id.noServers);
                noServers.setVisibility(View.VISIBLE);
                for(int server : servers) {
                    Log.d(AppInfo.LOG_TAG, "SERVER @ " + NetworkUtilities.ipAddressFromInt(server));
                }
            }
        }.execute();
    }
}
