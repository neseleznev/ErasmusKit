package com.cooldevs.erasmuskit.utils;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Utility for checking Internet connection in background
 */
public class CheckInternetConnection { //extends AsyncTask<Void, Void, Boolean> {

    /**
     * Check whether Google root DNS server is available (equal Internet is available)
     * http://stackoverflow.com/a/27312494
     * @param params
     * @return Boolean
     */
    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
//    @Override
//    protected Boolean doInBackground(Void... params) {
//        try {
//            int timeoutMs = 1500;
//            Socket sock = new Socket();
//            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
//
//            sock.connect(sockaddr, timeoutMs);
//            sock.close();
//
//            return true;
//        } catch (IOException e) { return false; }
//    }
}
