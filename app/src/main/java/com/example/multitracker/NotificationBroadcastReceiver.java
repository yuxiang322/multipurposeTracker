package com.example.multitracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.multitracker.dto.NotificationDTO;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationAlarmManager notificationAlarmManager = new NotificationAlarmManager(context);

        // check for reboot --> restore
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            notificationAlarmManager.restoreAlarmManager();
        }

        if("com.example.multitracker.NotificationAlarmManager".equals(intent.getAction())){

            // reshedule for android 26 + -> get repeatTime -> Calendar + 7(E.g Sun alrm -> next Sun) set the alarm
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                NotificationDTO notificationDTO = intent.getParcelableExtra("notificationObject");
                int notificationUID = intent.getIntExtra("notificationAlarmUID", -1);
                int templateID = intent.getIntExtra("notificationTemplateID", -1);
                String zonedLocalTimeString = intent.getStringExtra("repeatTime");
                LocalTime repeatTime = LocalTime.parse(zonedLocalTimeString);

                Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
                notificationIntent.setAction("com.example.multitracker.NotificationAlarmManager");
                notificationIntent.putExtra("notificationAlarmUID", notificationUID);
                notificationIntent.putExtra("repeatTime", zonedLocalTimeString);
                notificationIntent.putExtra("notificationTemplateID", templateID);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationUID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                if(alarmManager != null && alarmManager.canScheduleExactAlarms()){
                    // set nxt alarm day +7
                    // get calendar of device current date
                    // calculate triggertime = current date + target repeat time?
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, repeatTime.getHour());
                    calendar.set(Calendar.MINUTE, repeatTime.getMinute());
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.add(Calendar.DAY_OF_MONTH, 7);

                    long triggerTime = calendar.getTimeInMillis();
                    assert notificationDTO != null;
                    notificationDTO.setRepeatStartDate(new Date(triggerTime).toString());

                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);

                    notificationAlarmManager.saveAlarmManager(notificationDTO);
                }
            }

            // Notification popup -> TemplteDetails -> check for user login

        }
    }

    // notification compat
    private void notificationSetup(templateID){
//        alarm goes off
//        user clicks to enter app
//                Notificationcompat
//                  -> button listener
//        verify the login data sp
//        if login token not expired
//              -> direct to homapage
//                -> tempalteID + flag
//        if not login
//          -> redirect user to login page
//            -> pass flag + tempalteID
//         firebase redirect -> pass extra intent + Flag

        // Flag check @ homepage
        // if flag -> proceed to  the custom logic

    }

}
