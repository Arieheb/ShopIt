package com.ariehb_miriams.shopit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class checkInternet {
    public static String getNetworkInfo(Context context) {
        String status = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null){
            status = "connected";
            return status;
        }
        else {
            status = "no connection";
            return status;
        }
    }
}
