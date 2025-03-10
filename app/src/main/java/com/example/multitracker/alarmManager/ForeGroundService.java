package com.example.multitracker.alarmManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.multitracker.HomePage;
import com.example.multitracker.MainActivity;
import com.example.multitracker.R;
import com.example.multitracker.commonUtil.EncryptedSharePreferenceUtils;
import com.example.multitracker.commonUtil.GlobalConstant;
import com.example.multitracker.commonUtil.JwtUtils;
import com.example.multitracker.dto.NotificationDTO;

public class ForeGroundService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("AlarmManager", "===========================\nForegroundService");
        NotificationDTO notificationDTO = intent.getParcelableExtra("foregroundNotificationDTO");
        String packageName = intent.getStringExtra("packageName");

        assert notificationDTO != null;
        startForeground(1,notificationBuilder(notificationDTO, packageName));

        return START_NOT_STICKY;
    }

    private Notification notificationBuilder(NotificationDTO notificationDTO, String packageName){
        Log.d("AlarmManager", "Building notification....");
        Log.d("AlarmManager", "Notification Object..." + "ID: " + notificationDTO.getNotificationID() + " USERID: " + notificationDTO.getUserUID() + " TemplateID: " + notificationDTO.getTemplateID()
        + " RepeatTime: " + notificationDTO.getRepeatStartTime());
        String notificationDetails = "Please proceed to update the template details.\nTime: " + notificationDTO.getRepeatStartTime();

        RemoteViews remoteViews = new RemoteViews(packageName, R.layout.notification_custom_alert);
        remoteViews.setTextViewText(R.id.notificationMessage, notificationDetails);

        String spJwtToken = EncryptedSharePreferenceUtils.getEncryptedSharePreference(this, GlobalConstant.jwtSPLoginFileName, notificationDTO.getUserUID());

        Intent intent;
        if (spJwtToken != null ||!JwtUtils.isTokenExpired(spJwtToken)) {
            intent = new Intent(this, HomePage.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        intent.putExtra("fromNotificationAlertTemplateID", notificationDTO.getTemplateID());
        intent.putExtra("fromNotificationAlertUserUID", notificationDTO.getUserUID());
        intent.putExtra("isFromNotification", true);
        intent.putExtra("isFromNotificationLogin", false);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // set on click
        remoteViews.setOnClickPendingIntent(R.id.notificationAction, pendingIntent);

        createChannel();

        Log.d("AlarmManager", "Building notificationCompat");
        return new NotificationCompat.Builder(this, "channelId")
                .setSmallIcon(R.drawable.notification_icon)
                .setCustomBigContentView(remoteViews)
                .setAutoCancel(true)
                .build();

    }

    //create notificaiton channel
    private void createChannel(){
        Log.d("AlarmManager", "Create Channel....");
        CharSequence name = "Notification Channel";
        String description = "Channel for notification";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel("channelId", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
