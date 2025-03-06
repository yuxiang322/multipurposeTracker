package com.example.multitracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;

import com.example.multitracker.commonUtil.TimeUtil;
import com.example.multitracker.dto.NotificationDTO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class NotificationAlarmManager {
    private final Context context;

    public NotificationAlarmManager(Context context) {
        this.context = context;
    }

    public void setUpAlarmManager(NotificationDTO notificationAlarm) {
        if (notificationAlarm == null) {
            return;
        }
        Log.d("AlarmManager", "==========================\nNotificaiton not null");
        Log.d("AlarmOnReceiveBoot", "==========================\nNotificaiton not null");
        boolean alarmSaveFlag = false;
        String[] dayRepeat = notificationAlarm.getRepeatDays().split(",");

        // date time UTC to Local cater to reboot
        ZonedDateTime zonedNotificationDateTime = TimeUtil.convertToLocalTimeZone(LocalDate.now(ZoneOffset.UTC).toString(), notificationAlarm.getRepeatStartTime() != null ? notificationAlarm.getRepeatStartTime() : null);
        assert zonedNotificationDateTime != null;
        long notificationDateTime = zonedNotificationDateTime.toInstant().toEpochMilli();

        int notificationUID = notificationAlarm.getNotificationID();

        Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
        notificationIntent.setAction("com.example.multitracker.NotificationAlarmManager");
        notificationIntent.putExtra("notificationAlarmUID", notificationUID);
        notificationIntent.putExtra("repeatTime", zonedNotificationDateTime.toLocalDateTime().toLocalTime().toString());
        notificationIntent.putExtra("notificationTemplateID", notificationAlarm.getTemplateID());
        notificationIntent.putExtra("notificationObject", notificationAlarm);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null && notificationDateTime != 0) {
            for (String day : dayRepeat) {
                int dayInt = getDayOfWeek(day.trim());
                String combinedStringKey = notificationUID + String.valueOf(dayInt);
                int pendingIntentKey = Integer.parseInt(combinedStringKey);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, pendingIntentKey, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                if (dayInt != -1) {
                    Log.d("AlarmManager", "BaseTime passed: " + notificationDateTime);
                    Log.d("AlarmOnReceiveBoot", "BaseTime passed: " + notificationDateTime);
                    long triggerTime = getNextDayOfWeekMillis(notificationDateTime, dayInt) + 5000;
                    // Set alarm Request Permission
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (alarmManager.canScheduleExactAlarms()) {
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent); // need reschedule
                            alarmSaveFlag = true;
                            Log.d("AlarmManager", "Alarm S+ set for notificaiton pendingIntentKey: " + pendingIntentKey + " |  + day " + dayInt + " Date: " + new Date(triggerTime));
                            Log.d("AlarmOnReceiveBoot", "Alarm S+ set for notificaiton pendingIntentKey: " + pendingIntentKey + " |  + day " + dayInt + " Date: " + new Date(triggerTime));
                        }
                    } else {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent); // need reschedule
                        alarmSaveFlag = true;
                        Log.d("AlarmManager", "Alarm set for notificaiton pendingIntentKey: " + pendingIntentKey + " |  + day " + dayInt + " Date: " + new Date(triggerTime));
                        Log.d("AlarmOnReceiveBoot", "Alarm set for notificaiton pendingIntentKey: " + pendingIntentKey + " |  + day " + dayInt + " Date: " + new Date(triggerTime));
                    }
                }
            }
        }

        if (alarmSaveFlag) {
            saveAlarmManager(notificationAlarm);
        } else {
            Log.e("AlarmManager", "Failed to set alarm for notificationUID: " + notificationUID);
        }
    }

    public void deleteAlarmManager(NotificationDTO notificationAlarm) {
        Log.d("AlarmManager", "==========================\nDelete alarm sharepreference");
        int notificationUID = notificationAlarm.getNotificationID();

        Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationUID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);

        // delete sharepreference
        SharedPreferences alarmSharedPreferences = context.getSharedPreferences("alarmManager", Context.MODE_PRIVATE);
        String serializedPreference = alarmSharedPreferences.getString("alarmManager", "");

        List<NotificationDTO> tempAlarmList;
        Gson gson = new Gson();
        Type listType = new TypeToken<List<NotificationDTO>>() {
        }.getType();

        tempAlarmList = serializedPreference.isEmpty() ? new ArrayList<>() : gson.fromJson(serializedPreference, listType);

        if (tempAlarmList != null) {
            Iterator<NotificationDTO> iterator = tempAlarmList.iterator();

            while (iterator.hasNext()) {
                NotificationDTO notificationDTO = iterator.next();
                if (notificationDTO.getNotificationID() == notificationAlarm.getNotificationID()) {
                    iterator.remove();
                    break;
                }
            }
        }
        SharedPreferences.Editor editor = alarmSharedPreferences.edit();
        editor.putString("alarmManager", gson.toJson(tempAlarmList));
        editor.apply();
    }

    public void saveAlarmManager(NotificationDTO notificationSave) {
        Log.d("AlarmManager", "==========================\nSaving alarm sharepreference");
        Log.d("AlarmOnReceiveBoot", "==========================\nSaving alarm sharepreference");
        SharedPreferences alarmSharedPreferences = context.getSharedPreferences("alarmManager", Context.MODE_PRIVATE);
        String serializedPreference = alarmSharedPreferences.getString("alarmManager", "");

        List<NotificationDTO> tempAlarmList;
        Gson gson = new Gson();
        Type listType = new TypeToken<List<NotificationDTO>>() {
        }.getType();

        tempAlarmList = serializedPreference.isEmpty() ? new ArrayList<>() : gson.fromJson(serializedPreference, listType);

        if (tempAlarmList != null) {
            Iterator<NotificationDTO> iterator = tempAlarmList.iterator();

            while (iterator.hasNext()) {
                NotificationDTO notificationDTO = iterator.next();
                Log.d("AlarmManager", "SharePreference. Notificaition number: " + notificationDTO.getNotificationID() + "\n");
                Log.d("AlarmOnReceiveBoot", "SharePreference. Notificaition number: " + notificationDTO.getNotificationID() + "\n");
                if (notificationDTO.getNotificationID() == notificationSave.getNotificationID()) {
                    Log.d("AlarmManager", "SharePreference. Notificaition number(BREAK): " + notificationDTO.getNotificationID() + "\n");
                    Log.d("AlarmOnReceiveBoot", "SharePreference. Notificaition number(BREAK): " + notificationDTO.getNotificationID() + "\n");
                    iterator.remove();
                    break;
                }
            }
            tempAlarmList.add(notificationSave);
        }

        SharedPreferences.Editor editor = alarmSharedPreferences.edit();
        editor.putString("alarmManager", gson.toJson(tempAlarmList));
        editor.apply();
    }

    public void restoreAlarmManager() {
        Log.d("AlarmOnReceiveBoot", "REBOOT Restore Alarm");
        SharedPreferences alarmSharedPreferences = context.getSharedPreferences("alarmManager", Context.MODE_PRIVATE);
        String serializedPreference = alarmSharedPreferences.getString("alarmManager", "");

        List<NotificationDTO> tempAlarmList;
        Gson gson = new Gson();
        Type listType = new TypeToken<List<NotificationDTO>>() {
        }.getType();

        tempAlarmList = serializedPreference.isEmpty() ? new ArrayList<>() : gson.fromJson(serializedPreference, listType);

        if (tempAlarmList != null) {
            for (NotificationDTO alarmDTO : tempAlarmList) {
                Log.d("AlarmOnReceiveBoot", "Restoring Alarm for notification ID: " + alarmDTO.getNotificationID());
                setUpAlarmManager(alarmDTO);
            }
        } else {
            Log.w("AlarmOnReceiveBoot", "Restore failed");
        }
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

        Log.d("AlarmManager", "=============\nDay of Week: " + dayOfWeek + ". Day until next: " + daysUntilNext);
        if (daysUntilNext == 0) {
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(System.currentTimeMillis());

            if (calendar.get(Calendar.HOUR_OF_DAY) < now.get(Calendar.HOUR_OF_DAY) || (calendar.get(Calendar.HOUR_OF_DAY) == now.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.MINUTE) < now.get(Calendar.MINUTE))) {
                daysUntilNext = 7;
            }
        }

        calendar.add(Calendar.DAY_OF_YEAR, daysUntilNext);
        return calendar.getTimeInMillis();
    }
}
