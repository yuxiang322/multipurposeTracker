package com.example.multitracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.multitracker.commonUtil.ConvertTimeZone;
import com.example.multitracker.dto.NotificationDTO;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;

public class NotificationAlarmManager {
    private final Context context;

    public NotificationAlarmManager(Context context) {
        this.context = context;
    }


    // save alarm -> save sharedpreference
    // notificationid+useruid
    // Saving first time + Overwrite alarm, sharedpreference(serialization)
    // add pendingintent -> template details page

    // delet alarm -> delete from shared preference
    // notification + useruid
    // delete from alarm, sharedpreference key if found

    // restore alarm after device reboot
    // get all current alarm from sharedpreference(deserialization)

    // -- broadcast receveir --> notification compat

    public void setUpAlarmManager(NotificationDTO notificationAlarm) {
        if (notificationAlarm == null) {
            return;
        }

        String[] dayRepeat = notificationAlarm.getRepeatDays().split(",");

        // date time UTC to Local cater to reboot
        ZonedDateTime zonedNotificationDateTime = ConvertTimeZone.convertToLocalTimeZone(LocalDate.now(ZoneOffset.UTC).toString(), notificationAlarm.getRepeatStartTime() != null ? notificationAlarm.getRepeatStartTime() : null);
        assert zonedNotificationDateTime != null;
        long notificationDateTime = zonedNotificationDateTime.toInstant().toEpochMilli();

        int notificationUID = notificationAlarm.getNotificationID();

        Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
        notificationIntent.putExtra("notificationAlarmUID", notificationUID);
        notificationIntent.putExtra("repeatTime", zonedNotificationDateTime.toLocalDateTime().toLocalTime().toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationUID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null && notificationDateTime != 0) {
            for (String day : dayRepeat) {
                int dayInt = getDayOfWeek(day.trim());

                if (dayInt != -1) {
                    long triggerTime = getNextDayOfWeekMillis(notificationDateTime, dayInt);
                    // Set alarm Request Permission
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if(alarmManager.canScheduleExactAlarms()){
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime,pendingIntent); // need reschedule
                        }
                    } else {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                    }

                }
            }
        }

    }

    public void deleteAlarmManager(NotificationDTO notificationAlarm) {

        int notificationUID = notificationAlarm.getNotificationID();

        Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationUID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }

    public void saveAlarmManager(NotificationDTO notificationSave) {
        // retrieve based on key
        // if not found
            // create new
            // serialize
            // save in sharedpreference
        // else
          // revert object back
          // List<> add
          // serialize
          // save

    }

    public void restoreAlarmManager() {
        // on reboot
        // gather all Alarm_....
        // setupAlarmManager(object)
    }

    private int getDayOfWeek(String day) {
        switch (day) {
            case "Mon":
                return Calendar.MONDAY;
            case "Tue":
                return Calendar.TUESDAY;
            case "Wed":
                return Calendar.WEDNESDAY;
            case "Thu":
                return Calendar.THURSDAY;
            case "Fri":
                return Calendar.FRIDAY;
            case "Sat":
                return Calendar.SATURDAY;
            case "Sun":
                return Calendar.SUNDAY;
            default:
                return -1;
        }
    }

    private long getNextDayOfWeekMillis(long baseTime, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(baseTime);

        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int daysUntilNext = (dayOfWeek - currentDayOfWeek + 7) % 7;
        if (daysUntilNext == 0) {
            daysUntilNext = 7;
        }

        calendar.add(Calendar.DAY_OF_YEAR, daysUntilNext);
        return calendar.getTimeInMillis();
    }
}
