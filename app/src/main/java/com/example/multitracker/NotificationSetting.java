package com.example.multitracker;

import static com.example.multitracker.commonUtil.GlobalConstant.userID;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.multitracker.api.NotificationReportAPI;
import com.example.multitracker.api.UserDetailsAPI;
import com.example.multitracker.commonUtil.TimeUtil;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.NotificationDTO;
import com.example.multitracker.dto.NotificationReportDTO;
import com.example.multitracker.dto.ReportStatusDTO;
import com.example.multitracker.dto.TemplateDTO;
import com.example.multitracker.dto.UserDetailsDTO;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationSetting extends AppCompatActivity {
    private TemplateDTO templateDTOIntent;
    private String selectedInterval;
    private String otherRepeatInterval;
    private String userEmail;
    private String userPhone;
    private int reportID;
    private int notificationID;

    private final ActivityResultLauncher<Intent> requestAlarmPermission = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != RESULT_OK) {
                    Toast.makeText(this, "Alarm Permission denied. Notification can't be set.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_settings);
        // revamp
        // Get intent
        setupIntent();
        // Set up switch listener
        setupSwitchListener();
        // initial interval selection
        setupIntervalSelection();
        // Get user details
        setUpUserDetails();
        // Populate notification
        populateNotification();
        // Set up date and time
        setUpDateTimeDialog();
        // Save and cancel
        setupSaveCancel();
    }

    private void setupIntent() {
        Intent intent = getIntent();
        templateDTOIntent = intent.getParcelableExtra("templateObject");
    }

    private void setUpUserDetails() {
        UserDetailsDTO userRequest = new UserDetailsDTO();
        userRequest.setUserUID(userID);
        UserDetailsAPI userAPI = RetrofitClientInstance.getRetrofitInstance().create(UserDetailsAPI.class);
        Call<UserDetailsDTO> call = userAPI.getUserDetails(userRequest);
        call.enqueue(new Callback<UserDetailsDTO>() {
            @Override
            public void onResponse(Call<UserDetailsDTO> call, Response<UserDetailsDTO> response) {
                if (response.isSuccessful()) {
                    UserDetailsDTO responseUserDetails = response.body();
                    if (responseUserDetails != null) {
                        EditText email = findViewById(R.id.notificationEmail);
                        EditText phone = findViewById(R.id.notificationPhone);
                        userEmail = responseUserDetails.getEmail();
                        userPhone = responseUserDetails.getPhone();

                        email.setText(userEmail);
                        phone.setText(userPhone);

                    } else {
                        Toast.makeText(NotificationSetting.this, "Failed to retrieve user details", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NotificationSetting.this, "Failed to retrieve user details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetailsDTO> call, Throwable t) {
                // Handle errors
                Toast.makeText(NotificationSetting.this, "Error retrieving user details: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSwitchListener() {
        SwitchCompat notificationSwitch = findViewById(R.id.notificationSwitchCompat);
        SwitchCompat reportSwitch = findViewById(R.id.reportSwitchCompat);
        // notification swtich
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setNotificationField(isChecked);
        });

        // report switch
        reportSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setReportField(isChecked);
        });
    }

    private void setNotificationField(boolean shouldEnable) {

        CheckBox smsCheckbox = findViewById(R.id.smsCheckbox);
        smsCheckbox.setEnabled(shouldEnable);

        CheckBox whatappsCheckbox = findViewById(R.id.whatappsCheckbox);
        whatappsCheckbox.setEnabled(shouldEnable);

        Spinner intervalSpinner = findViewById(R.id.interval_spinner);
        intervalSpinner.setEnabled(shouldEnable);

        // add remaining field + chip
        Chip mon = findViewById(R.id.chipMonday);
        Chip tue = findViewById(R.id.chipTuesday);
        Chip wed = findViewById(R.id.chipWednesday);
        Chip thu = findViewById(R.id.chipThursday);
        Chip fri = findViewById(R.id.chipFriday);
        Chip sat = findViewById(R.id.chipSaturday);
        Chip sun = findViewById(R.id.chipSunday);

        mon.setEnabled(shouldEnable);
        tue.setEnabled(shouldEnable);
        wed.setEnabled(shouldEnable);
        thu.setEnabled(shouldEnable);
        fri.setEnabled(shouldEnable);
        sat.setEnabled(shouldEnable);
        sun.setEnabled(shouldEnable);

        EditText notificationInterval = findViewById(R.id.notificationInterval);
        notificationInterval.setEnabled(shouldEnable);
    }

    private void setReportField(boolean shouldEnable) {
        EditText reportStatusStartDate = findViewById(R.id.reportStatusStartDate);
        reportStatusStartDate.setEnabled(shouldEnable);

        EditText reportStatusTime = findViewById(R.id.reportStatusTime);
        reportStatusTime.setEnabled(shouldEnable);

        EditText customIntervalEditText = findViewById(R.id.reportCustomInterval);
        customIntervalEditText.setEnabled(shouldEnable);
    }

    private void setupIntervalSelection() {
        Spinner intervalSpinner = findViewById(R.id.interval_spinner);
        EditText customIntervalEditText = findViewById(R.id.reportCustomInterval);
        // Spinner
        String[] intervals = {"-------", "Daily", "Weekly", "Monthly", "Yearly", "Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, intervals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalSpinner.setAdapter(adapter);
        // Pre-select
        int selectedPosition = 0;
        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i].equals(selectedInterval)) {
                selectedPosition = i;
                break;
            }
        }
        intervalSpinner.setSelection(selectedPosition);
        // Edit text visible
        if ("Others".equals(selectedInterval)) {
            customIntervalEditText.setVisibility(View.VISIBLE);
            customIntervalEditText.setText(otherRepeatInterval);
        } else {
            customIntervalEditText.setVisibility(View.GONE);
        }
        // listener
        intervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (intervals[position].equals("Others")) {
                    customIntervalEditText.setVisibility(View.VISIBLE);
                } else {
                    customIntervalEditText.setVisibility(View.GONE);
                }
                selectedInterval = intervals[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    // Populate Notification
    private void populateNotification() {

        NotificationReportAPI notificationReportAPI = RetrofitClientInstance.getRetrofitInstance().create(NotificationReportAPI.class);
        Call<NotificationReportDTO> callNotificationReport = notificationReportAPI.getNotification(templateDTOIntent);

        callNotificationReport.enqueue(new Callback<NotificationReportDTO>() {
            @Override
            public void onResponse(Call<NotificationReportDTO> call, Response<NotificationReportDTO> response) {
                if (response.isSuccessful()) {
                    NotificationReportDTO responseNotiReport = response.body();
                    notificationID = responseNotiReport.getNotificationDTO().getNotificationID();
                    reportID = responseNotiReport.getReportStatusDTO().getReportID();
                    setupPhoneCheckBoxes(responseNotiReport.getNotificationDTO());
                    processNotification(responseNotiReport.getNotificationDTO());
                    processReport(responseNotiReport.getReportStatusDTO());
                }
            }

            @Override
            public void onFailure(Call<NotificationReportDTO> call, Throwable t) {

            }
        });

    }

    private void processNotification(NotificationDTO responseNotification) {
        setNotificationField(responseNotification.getNotificationFlag());
        Log.d("FlagTest", "Notification Flag??: " + responseNotification.getNotificationFlag().toString());
        SwitchCompat notificationSwitch = findViewById(R.id.notificationSwitchCompat);
        notificationSwitch.setChecked(responseNotification.getNotificationFlag());

        if (responseNotification.getNotificationFlag()) {
            // interval time
            EditText notificationInterval = findViewById(R.id.notificationInterval);
            ZonedDateTime notificationRepeatDateTime = TimeUtil.convertToLocalTimeZone(responseNotification.getRepeatStartDate(), responseNotification.getRepeatStartTime());
            String notificationLocalTime = notificationRepeatDateTime.toLocalTime().toString();

            notificationInterval.setText(!notificationLocalTime.isEmpty() ? notificationLocalTime : null);
            // Chips selection
            if (responseNotification.getRepeatDays() != null) {
                String chipSelection = responseNotification.getRepeatDays();
                String[] chipList = chipSelection.split(",");

                for (String chip : chipList) {
                    switch (chip) {
                        case "Mon":
                            Chip mon = findViewById(R.id.chipMonday);
                            mon.setChecked(true);
                            break;
                        case "Tue":
                            Chip tue = findViewById(R.id.chipTuesday);
                            tue.setChecked(true);
                            break;
                        case "Wed":
                            Chip wed = findViewById(R.id.chipWednesday);
                            wed.setChecked(true);
                            break;
                        case "Thu":
                            Chip thu = findViewById(R.id.chipThursday);
                            thu.setChecked(true);
                            break;
                        case "Fri":
                            Chip fri = findViewById(R.id.chipFriday);
                            fri.setChecked(true);
                            break;
                        case "Sat":
                            Chip sat = findViewById(R.id.chipSaturday);
                            sat.setChecked(true);
                            break;
                        case "Sun":
                            Chip sun = findViewById(R.id.chipSunday);
                            sun.setChecked(true);
                            break;
                    }
                }
            }
        }
    }

    private void processReport(ReportStatusDTO responseReport) {
        setReportField(responseReport.getReportFlag());
        SwitchCompat reportSwitch = findViewById(R.id.reportSwitchCompat);
        reportSwitch.setChecked(responseReport.getReportFlag());
        Log.d("FlagTest", "Report Flag??: " + responseReport.getReportFlag().toString());

        if (responseReport.getReportFlag()) {
            // Start Date
            EditText reportStatusDate = findViewById(R.id.reportStatusStartDate);
            reportStatusDate.setText(responseReport.getRepeatStartDate() != null ? responseReport.getRepeatStartDate() : null);
            // time
            EditText reportTime = findViewById(R.id.reportStatusTime);
            reportTime.setText(responseReport.getRepeatStartTime() != null ? responseReport.getRepeatStartTime() : null);

            if (responseReport.getRepeatIntervalType() == null) {
                selectedInterval = "-------";
            } else {
                selectedInterval = responseReport.getRepeatIntervalType();

                if (selectedInterval.equals("Others")) {
                    otherRepeatInterval = responseReport.getRepeatInterval();
                }
            }
            setupIntervalSelection();
        }
    }

    private void setupPhoneCheckBoxes(NotificationDTO retrievedNotification) {
        CheckBox smsCheckbox = findViewById(R.id.smsCheckbox);
        CheckBox whatappsCheckbox = findViewById(R.id.whatappsCheckbox);
        EditText phone = findViewById(R.id.notificationPhone);

        if (retrievedNotification.getWhatsAppFlag() != null) {
            whatappsCheckbox.setChecked(retrievedNotification.getWhatsAppFlag());
        }
        if (retrievedNotification.getSmsFlag() != null) {
            smsCheckbox.setChecked(retrievedNotification.getSmsFlag());
        }

        if (phone.getText() == null) {
            smsCheckbox.setEnabled(false);
            whatappsCheckbox.setEnabled(false);
        } else {
            smsCheckbox.setEnabled(true);
            whatappsCheckbox.setEnabled(true);
        }
    }

    private void setUpDateTimeDialog() {
        EditText startDateEditText = findViewById(R.id.reportStatusStartDate);
        EditText timeEditText = findViewById(R.id.reportStatusTime);
        EditText notificationTime = findViewById(R.id.notificationInterval);

        // notification time
        notificationTime.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    NotificationSetting.this,
                    (view, hourOfDay, minute1) -> {
                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                        Log.d("Timer", "Ntoficaiotn: " + formattedTime);
                        notificationTime.setText(formattedTime);
                    },
                    hour,
                    minute,
                    true
            );
            timePickerDialog.show();
        });

        // Date report
        startDateEditText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(NotificationSetting.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String formattedMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                    String formattedDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                    startDateEditText.setText(year + "-" + formattedMonth + "-" + formattedDay);
                }
            }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });

        // Time report
        timeEditText.setOnClickListener(v -> {
            if (!startDateEditText.getText().toString().trim().isEmpty()) {
                // Get the current time
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(NotificationSetting.this, (view, hourOfDay, minute1) -> {
                    // Set the selected time to the EditText
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                    timeEditText.setText(formattedTime);
                    Log.d("Timer", "Report: " + timeEditText.getText());
                }, hour, minute, true);
                timePickerDialog.show();
            } else {
                startDateEditText.setError("Please select a start date first");
            }
        });
    }


    private void setupSaveCancel() {
        Button saveNotification = findViewById(R.id.saveNotification);
        Button cancelNotification = findViewById(R.id.cancelNotification);

        cancelNotification.setOnClickListener(v -> finish());

        // Save button
        saveNotification.setOnClickListener(v -> {
            if (validateFields()) {
                saveNotificationReport();
                finish();
            } else {
                Toast.makeText(NotificationSetting.this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Validate all the fields
    private boolean validateFields() {
        SwitchCompat notificationSwitch = findViewById(R.id.notificationSwitchCompat);
        SwitchCompat reportSwitch = findViewById(R.id.reportSwitchCompat);

        if (notificationSwitch.isChecked()) {
            EditText timeInterval = findViewById(R.id.notificationInterval);
            ChipGroup chipGroup = findViewById(R.id.chipGroupDays);

            boolean hasValidTime = !TextUtils.isEmpty(timeInterval.getText());
            if (!hasValidTime) {
                timeInterval.setError("Time cannot be empty");
            } else {
                timeInterval.setError(null);
            }

            boolean hasSelectedChip = !chipGroup.getCheckedChipIds().isEmpty();
            if (!hasSelectedChip) {
                Toast.makeText(this, "Select at least one day", Toast.LENGTH_SHORT).show();
            }

            if (!hasValidTime || !hasSelectedChip) {
                return false;
            }
        }

        if (reportSwitch.isChecked()) {
            EditText startDate = findViewById(R.id.reportStatusStartDate);
            EditText reportTime = findViewById(R.id.reportStatusTime);
            Spinner reportInterval = findViewById(R.id.interval_spinner);

            boolean isValidDate = !TextUtils.isEmpty(startDate.getText());
            if (!isValidDate) {
                startDate.setError("Date cannot be empty");
            } else {
                startDate.setError(null);
            }

            boolean isValidTime = !TextUtils.isEmpty(reportTime.getText());
            if (!isValidTime) {
                reportTime.setError("Time cannot be empty");
            } else {
                reportTime.setError(null);
            }

            boolean isIntervalValid = reportInterval.getSelectedItemPosition() != 0;
            if (!isIntervalValid) {
                Toast.makeText(this, "Select a valid report interval", Toast.LENGTH_SHORT).show();
            }

            if (reportInterval.getSelectedItemPosition() == 5) {
                EditText reportOthersInterval = findViewById(R.id.reportCustomInterval);
                isIntervalValid = !TextUtils.isEmpty(reportOthersInterval.getText());
                if (!isIntervalValid) {
                    reportOthersInterval.setError("Interval cannot be empty");
                } else {
                    reportOthersInterval.setError(null);
                }
            }

            if (!isValidDate || !isValidTime || !isIntervalValid) {
                return false;
            }
        }
        return true;
    }

    private void saveNotificationReport() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            if(alarmManager != null && !alarmManager.canScheduleExactAlarms()){

                Intent alarmPermissionIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                requestAlarmPermission.launch(alarmPermissionIntent);
                return;
            }
        }

        // create NotificationDTO
        NotificationDTO notificationUpdate = new NotificationDTO();

        SwitchCompat notificationSwitch = findViewById(R.id.notificationSwitchCompat);
        CheckBox smsCheckbox = findViewById(R.id.smsCheckbox);
        CheckBox whatappsCheckbox = findViewById(R.id.whatappsCheckbox);
        ChipGroup chipGroup = findViewById(R.id.chipGroupDays);
        EditText notificationTime = findViewById(R.id.notificationInterval);

        StringBuilder chipDays = new StringBuilder();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            if (chipGroup.getChildAt(i) != null) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    if (chipDays.length() > 0) chipDays.append(",");
                    chipDays.append(chip.getText());
                }
            }
        }
        String chipDaysString = chipDays.toString();

        notificationUpdate.setNotificationID(notificationID);
        notificationUpdate.setTemplateID(templateDTOIntent.getTemplateID());
        notificationUpdate.setNotificationFlag(notificationSwitch.isChecked());
        notificationUpdate.setSmsFlag(smsCheckbox.isChecked());
        notificationUpdate.setWhatsAppFlag(whatappsCheckbox.isChecked());
        notificationUpdate.setRepeatDays(chipDaysString);

        if(notificationTime.getText() != null || !notificationTime.getText().toString().isEmpty()){
            LocalDateTime notiLocalDateTime = TimeUtil.convertToUTC(LocalDate.now(), LocalTime.parse(notificationTime.getText().toString()));
            notificationUpdate.setRepeatStartDate(notiLocalDateTime.toLocalDate().toString());
            notificationUpdate.setRepeatStartTime(notiLocalDateTime.toLocalTime().toString());
        }

        // create ReportStatusDTO
        ReportStatusDTO reportUpdate = new ReportStatusDTO();

        SwitchCompat reportSwitch = findViewById(R.id.reportSwitchCompat);
        EditText reportStatusStartDate = findViewById(R.id.reportStatusStartDate);
        EditText reportStatusTime = findViewById(R.id.reportStatusTime);

        reportUpdate.setNotificationID(notificationID);
        reportUpdate.setReportID(reportID);
        reportUpdate.setReportFlag(reportSwitch.isChecked());
        reportUpdate.setRepeatIntervalType(selectedInterval);

        if(reportStatusStartDate.getText() != null && reportStatusTime.getText() != null && !reportStatusStartDate.getText().toString().isEmpty() && !reportStatusTime.getText().toString().isEmpty()){
            LocalDateTime reportDateTime = TimeUtil.convertToUTC(LocalDate.parse(reportStatusStartDate.getText().toString()), LocalTime.parse(reportStatusTime.getText().toString()));
            reportUpdate.setRepeatStartDate(reportDateTime.toLocalDate().toString());
            reportUpdate.setRepeatStartTime(reportDateTime.toLocalTime().toString());
        }


        if (selectedInterval.equals("Others")) {
            EditText customIntervalEditText = findViewById(R.id.reportCustomInterval);
            String customInterval = customIntervalEditText.getText().toString().trim();
            reportUpdate.setRepeatInterval(customInterval);
        } else if (selectedInterval.equals("-------")) {
            reportUpdate.setRepeatInterval(null);
        } else if (selectedInterval.equals("Daily")) {
            reportUpdate.setRepeatInterval(String.valueOf(24 * 60));
        } else if (selectedInterval.equals("Weekly")) {
            reportUpdate.setRepeatInterval(String.valueOf(7 * 24 * 60));
        } else if (selectedInterval.equals("Monthly")) {
            reportUpdate.setRepeatInterval(String.valueOf(30 * 24 * 60));
        } else if (selectedInterval.equals("Yearly")) {
            reportUpdate.setRepeatInterval(String.valueOf(365 * 24 * 60));
        }

        NotificationReportDTO notificationReportUpdate = new NotificationReportDTO();
        notificationReportUpdate.setNotificationDTO(notificationUpdate);
        notificationReportUpdate.setReportStatusDTO(reportUpdate);

        NotificationReportAPI notificationReportAPI = RetrofitClientInstance.getRetrofitInstance().create(NotificationReportAPI.class);
        Call<String> callNotiUpdate = notificationReportAPI.updateNotification(notificationReportUpdate);

        NotificationAlarmManager notificationAlarmManager = new NotificationAlarmManager(getApplicationContext());
        callNotiUpdate.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Response success
                if (response.isSuccessful()) {
                    // notificaiton compat??
                    if (notificationUpdate.getNotificationFlag()) {
                        notificationAlarmManager.setUpAlarmManager(notificationUpdate);
                    } else {
                        notificationAlarmManager.deleteAlarmManager(notificationUpdate);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
