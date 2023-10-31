package com.kiwitomatostudio.anisearch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;

public class NetworkUtils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetworkInfo = connectivityManager.getActiveNetwork();
        return activeNetworkInfo != null;
    }
}
