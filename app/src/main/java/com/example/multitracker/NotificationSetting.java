package com.example.multitracker;

import static com.example.multitracker.commonUtil.GlobalConstant.userID;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.multitracker.api.NotificationReportAPI;
import com.example.multitracker.api.UserDetailsAPI;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.NotificationDTO;
import com.example.multitracker.dto.NotificationReportDTO;
import com.example.multitracker.dto.ReportStatusDTO;
import com.example.multitracker.dto.TemplateDTO;
import com.example.multitracker.dto.UserDetailsDTO;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.checkerframework.checker.units.qual.C;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
    private LocalDate reportDate;
    private LocalTime reportTime;
    private LocalTime notificationLocalTime;

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

        if (responseNotification.getNotificationFlag()) {
            // interval time
            EditText notificationInterval = findViewById(R.id.notificationInterval);
            notificationInterval.setText(responseNotification.getRepeatStartTime());
            // Chips selection
            String chipSelection = responseNotification.getRepeatDays();
            String[] chipList = chipSelection.split(",");

            for (String chip : chipList) {
                switch (chip) {
                    case "mon":
                        Chip mon = findViewById(R.id.chipMonday);
                        mon.setChecked(true);
                        break;
                    case "tue":
                        Chip tue = findViewById(R.id.chipTuesday);
                        tue.setChecked(true);
                        break;
                    case "wed":
                        Chip wed = findViewById(R.id.chipWednesday);
                        wed.setChecked(true);
                        break;
                    case "thu":
                        Chip thu = findViewById(R.id.chipThursday);
                        thu.setChecked(true);
                        break;
                    case "fri":
                        Chip fri = findViewById(R.id.chipFriday);
                        fri.setChecked(true);
                        break;
                    case "sat":
                        Chip sat = findViewById(R.id.chipSaturday);
                        sat.setChecked(true);
                        break;
                    case "sun":
                        Chip sun = findViewById(R.id.chipSunday);
                        sun.setChecked(true);
                        break;
                }
            }
        }

    }

    private void processReport(ReportStatusDTO responseReport) {
        setReportField(responseReport.getReportFlag());

        if (responseReport.getReportFlag()) {
            // Start Date
            EditText reportStatusDate = findViewById(R.id.reportStatusStartDate);
            reportStatusDate.setText(responseReport.getRepeatStartDate());
            // time
            EditText reportTime = findViewById(R.id.reportStatusTime);
            reportTime.setText(responseReport.getRepeatStartTime());

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
                        // Set local time variable
                        notificationLocalTime = LocalTime.of(hourOfDay, minute1);
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
                    startDateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    // Set local date variable
                    reportDate = LocalDate.of(year, monthOfYear+1, dayOfMonth);
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
                    // Set local time variable
                    reportTime = LocalTime.of(hourOfDay, minute1);
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

    public LocalDateTime convertToUTC(LocalDate localDate, LocalTime localTime) {
        if (localDate != null && localTime != null) {
            LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

            ZonedDateTime localZonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
            ZonedDateTime utcZonedDateTime = localZonedDateTime.withZoneSameInstant(ZoneOffset.UTC);

            return utcZonedDateTime.toLocalDateTime();
        }
        return null;
    }

    private void saveNotificationReport() {
        // create NotificationDTO
        NotificationDTO notificationUpdate = new NotificationDTO();

        SwitchCompat notificationSwitch = findViewById(R.id.notificationSwitchCompat);
        CheckBox smsCheckbox = findViewById(R.id.smsCheckbox);
        CheckBox whatappsCheckbox = findViewById(R.id.whatappsCheckbox);
        ChipGroup chipGroup = findViewById(R.id.chipGroupDays);

        StringBuilder chipDays = new StringBuilder();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            if (chipGroup.getChildAt(i) != null) {
                if (chipDays.length() > 0) chipDays.append(",");
                chipDays.append(((Chip) chipGroup.getChildAt(i)).getText());
            }
        }
        String chipDaysString = chipDays.toString();

        notificationUpdate.setNotificationID(notificationID);
        notificationUpdate.setTemplateID(templateDTOIntent.getTemplateID());
        notificationUpdate.setNotificationFlag(notificationSwitch.isChecked());
        notificationUpdate.setSmsFlag(smsCheckbox.isChecked());
        notificationUpdate.setWhatsAppFlag(whatappsCheckbox.isChecked());
        notificationUpdate.setRepeatDays(chipDaysString);

        LocalDateTime notiLocalDateTime = convertToUTC(LocalDate.now(), notificationLocalTime);
        notificationUpdate.setRepeatStartDate(notiLocalDateTime.toLocalDate().toString());
        notificationUpdate.setRepeatStartTime(notiLocalDateTime.toLocalTime().toString());

        // create ReportStatusDTO
        ReportStatusDTO reportUpdate = new ReportStatusDTO();

        SwitchCompat reportSwitch = findViewById(R.id.reportSwitchCompat);

        reportUpdate.setNotificationID(notificationID);
        reportUpdate.setReportID(reportID);
        reportUpdate.setReportFlag(reportSwitch.isChecked());
        reportUpdate.setRepeatIntervalType(selectedInterval);

        LocalDateTime reportDateTime = convertToUTC(reportDate, reportTime);
        reportUpdate.setRepeatStartDate(reportDateTime.toLocalDate().toString());
        reportUpdate.setRepeatStartTime(reportDateTime.toLocalTime().toString());

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
