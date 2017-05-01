package com.cooldevs.erasmuskit.ui;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Base Activity for Activities, which require Internernet in order to handle appropriate
 * data display or "No connection" stub
 */
public class BaseInternetActivity extends AppCompatActivity implements ConnectivityChangeReceiver.OnConnectivityChangedListener {

    private ConnectivityChangeReceiver connectivityChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, filter);
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        throw new UnsupportedOperationException("Implement onConnectivityChanged when subclass\n" +
                "from BaseInternetActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectivityChangeReceiver);
    }
}
