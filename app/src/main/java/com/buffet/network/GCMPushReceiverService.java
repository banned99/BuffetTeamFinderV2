package com.buffet.network;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.buffet.NotificationHandler;
import com.buffet.activities.DealMemberActivity;
import com.buffet.activities.MainActivity;
import com.buffet.activities.SplashActivity;
import com.buffet.adapters.DealMemberRecyclerAdapter;
import com.buffet.models.DealMember;
import com.google.android.gms.gcm.GcmListenerService;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * Created by Tastomy on 12/4/2016 AD.
 */

public class GCMPushReceiverService extends GcmListenerService {
    //This method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Getting the message from the bundle
        String message = data.getString("message");
        String title = data.getString("title");
        //Displaying a notiffication with the message
        sendNotification(title, message);
    }

    //This method is generating a notification and displaying the notification
    private void sendNotification(String title, String message) {
////        Intent intent = new Intent(this, SplashActivity.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////
////        int requestCode = 0;
////        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
////        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
////                .setSmallIcon(R.drawable.logocrop)
////                .setContentTitle(title)
////                .setContentText(message)
////                .setAutoCancel(true)
////                .setVibrate(new long[] { 300, 300 })
////                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification

        Intent pushNoti = new Intent("pushNoti");
        pushNoti.putExtra("message", message);
        pushNoti.putExtra("title", title);
        pushNoti.putExtra("id", "1");

        //We will create this class to handle notifications
        NotificationHandler notificationHandler = new NotificationHandler(getApplicationContext());

        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNoti);
//        //If the app is in foreground
//        if (!NotificationHandler.isAppIsInBackground(getApplicationContext())) {
//            //Sending a broadcast to the chatroom to add the new message
//            System.out.println("Backgroundddd");
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNoti);
//        } else {
//            //If app is in foreground displaying push notification
//            notificationHandler.showNotificationMessage(title, message);
//            System.out.println("Foregroundddd");
//
//        }
    }

}
