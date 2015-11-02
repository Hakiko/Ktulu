package com.starsep.ktulu;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerService extends IntentService {
    public ServerService() {
        super("ServerService");
    }

    private static final int PORT = 1337;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Ktulu", "ServerService started");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //maybe switchOffMulticastPermission()?
    }

    private void makeToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    private class Connection implements Runnable {
        private Socket socket;
        private BufferedReader input;
        private int id;

        public Connection(Socket socket, int id) {
            this.socket = socket;
            this.id = id;
            try {
                this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                socket.getOutputStream().write("I am server\n".getBytes());
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Log.d(AppInfo.LOG_TAG, "Connected id = " + String.valueOf(id));
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String read = input.readLine();
                    if (read != null) {
                        Log.d(AppInfo.LOG_TAG, "READ id = " + String.valueOf(id) + ": " + read);
                    } else {
                        Log.d(AppInfo.LOG_TAG, "Disconnected id = " + String.valueOf(id));
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Ktulu", "onHandleIntent");

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int connectionNumber = 0;

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, connectionNumber++);
                new Thread(connection).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
