package com.ariehb_miriams.shopit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;

public class internetReceiver extends BroadcastReceiver {

    // notification for loss of internet.
    private int notificationID;
    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
//        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String status = checkInternet.getNetworkInfo(context);
        if(status.equals("connected")){
//            notificationTextConnected(context);
//            Toast.makeText(context.getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
        }
        else if (status.equals("no connection")){
//            notificationTextNotConnected(context);
            Toast.makeText(context.getApplicationContext(), "No connection", Toast.LENGTH_LONG).show();
        }

        // 2. Create Notification-Channel.
//        NotificationChannel notificationChannel = new NotificationChannel(
//                "HIGH_CHANNEL_ID",      // Constant for Channel ID
//                "HIGH_CHANNEL_NAME",    // Constant for Channel NAME
//                NotificationManager.IMPORTANCE_HIGH);
//
//        notificationManager.createNotificationChannel(notificationChannel);

    }

//    public void notificationTextConnected(Context context){
//        Notification notification = new NotificationCompat.Builder(context, "HIGH_CHANNEL_ID")
//                .setContentTitle( "connected" + notificationID )
//                //.setContentText(text)
//                .build();
//
//        notificationManager.notify(notificationID, notification);
//        notificationID++;
//
//        notificationID = 1;
//        notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
//    }

//    public void notificationTextNotConnected(Context context){
//        Notification notification = new NotificationCompat.Builder(context, "HIGH_CHANNEL_ID")
//                .setContentTitle( "no internet connection" + notificationID )
//                //.setContentText(text)
//                .build();
//
//
//        notificationManager.notify(notificationID, notification);
//        notificationID++;
//
//
//        notificationID = 1;
//        notificationManager =  (NotificationManager) context.getSystemService(NotificationManager.class);
//    }
}
