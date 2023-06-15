package com.ariehb_miriams.shopit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class InternetReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = CheckInternet.getNetworkInfo(context);
        if(status.equals("connected")){
            Toast.makeText(context.getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
        }
        else if (status.equals("no connection")){
            Toast.makeText(context.getApplicationContext(), "No connection", Toast.LENGTH_LONG).show();

        }
    }
}
