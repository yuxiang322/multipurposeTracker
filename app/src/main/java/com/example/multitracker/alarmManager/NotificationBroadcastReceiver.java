package com.example.multitracker.alarmManager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.multitracker.HomePage;
import com.example.multitracker.MainActivity;
import com.example.multitracker.R;
import com.example.multitracker.dto.NotificationDTO;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmManager", "==========================\nOnReceive Started");
        NotificationAlarmManager notificationAlarmManager = new NotificationAlarmManager(context);

        // check for reboot --> restore
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) || Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("AlarmOnReceiveBoot", "==========================\nOnReceive REBOOT");
            notificationAlarmManager.restoreAlarmManager();
        }

        if ("com.example.multitracker.managers.NotificationAlarmManager".equals(intent.getAction())) {
            Log.d("AlarmManager", "==========================\nOnReceive Action");

            NotificationDTO notificationDTO = intent.getParcelableExtra("notificationObject");
            int notificationUID = intent.getIntExtra("notificationAlarmUID", -1);
            int templateID = intent.getIntExtra("notificationTemplateID", -1);
            String zonedLocalTimeString = intent.getStringExtra("repeatTime");
            LocalTime repeatTime = LocalTime.parse(zonedLocalTimeString);

            Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
            notificationIntent.setAction("com.example.multitracker.managers.NotificationAlarmManager");
            notificationIntent.putExtra("notificationAlarmUID", notificationUID);
            notificationIntent.putExtra("repeatTime", zonedLocalTimeString);
            notificationIntent.putExtra("notificationTemplateID", templateID);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, repeatTime.getHour());
            calendar.set(Calendar.MINUTE, repeatTime.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.DAY_OF_MONTH, 7);

            int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String combinedStringKey = notificationUID + String.valueOf(currentDayOfWeek);
            int pendingIntentKey = Integer.parseInt(combinedStringKey);
            long triggerTime = calendar.getTimeInMillis() + 5000;

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, pendingIntentKey, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                Log.d("AlarmManager", "Rescheduling alarm ALARMMANGER not null");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                        Log.d("AlarmManager", "Alarm S+ set NEXT for notificaiton pendingIntentKey: " + pendingIntentKey + " | Date TriggerTime: " + new Date(triggerTime));
                    }
                } else {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                    Log.d("AlarmManager", "Alarm set NEXT for notificaiton pendingIntentKey: " + pendingIntentKey + " | Date: " + new Date(triggerTime));
                }
                assert notificationDTO != null;
                notificationDTO.setRepeatStartDate(new Date(triggerTime).toString());
                notificationAlarmManager.saveAlarmManager(notificationDTO);
            }

            Log.d("AlarmManager", "=============================\nONRECEIVE Notification popup");
            assert notificationDTO != null;
            notificationDTO.setRepeatStartTime(repeatTime.toString());
            String packageName = context.getPackageName();

            Intent foregroundIntent = new Intent(context, ForeGroundService.class);
            foregroundIntent.putExtra("packageName", packageName);
            foregroundIntent.putExtra("foregroundNotificationDTO", notificationDTO);
            context.startService(foregroundIntent);
        }
    }

}
