package com.example.scratchapplication.gcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.scratchapplication.MainActivity;
import com.example.scratchapplication.R;
import com.example.scratchapplication.ViewMessageActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.nio.channels.Channel;

public class MyServiceCloudMessage extends FirebaseMessagingService {
    private PendingIntent pendingIntent;
    @Override
    public void onNewToken(@NonNull String s) {
        Log.e("new token", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification()!=null){
            String uid ="";
            if (remoteMessage.getData().size()>0) {
                uid = remoteMessage.getData().get("uId");
            }
            Log.e("noti", uid);
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            sendNotification(uid,title, body);
        }
    }

    private void sendNotification(String uid,String title,String body) {
        Intent intentMain = new Intent(this, MainActivity.class);
        intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent intentMessage = new Intent(this, ViewMessageActivity.class);
        intentMessage.putExtra("idReceive", uid);
        intentMessage.putExtra("AVATAR","");
        intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (title.toLowerCase()){
            case "message":
                 pendingIntent = PendingIntent.getActivity(this, 0, intentMessage, PendingIntent.FLAG_CANCEL_CURRENT);
                 break;
            default:
                 pendingIntent = PendingIntent.getActivity(this, 0, intentMain, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        String channelId = "";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel title",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }
        if (!ViewMessageActivity.idChat.equals(uid))
            notificationManager.notify(0,notificationBuilder.build());
    }
}
