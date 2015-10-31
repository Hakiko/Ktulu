package com.starsep.ktulu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FindServerActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    private void updateProgressBar(int k) {
        progressBar.setProgress(k);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_server);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for(int i = 1; i <= 100; i++) {
                    SystemClock.sleep(5);
                    updateProgressBar(i);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressBar.setVisibility(View.GONE);
                TextView noServers = (TextView) findViewById(R.id.noServers);
                noServers.setVisibility(View.VISIBLE);
            }
        }.execute();
    }
}
