package com.example.multitracker;

import static com.example.multitracker.commonUtil.GlobalConstant.userID;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.multitracker.api.UserDetailsAPI;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.NotificationDTO;
import com.example.multitracker.dto.TemplateDTO;
import com.example.multitracker.dto.UserDetailsDTO;
import com.google.android.material.chip.Chip;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationSetting extends AppCompatActivity {
    private TemplateDTO templateDTOIntent;
    private String selectedInterval;
    private String otherRepeatInterval;
    private String userEmail;
    private String userPhone;
    private int repeatID;
    private int notificationID;

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

    private void setNotificationField(boolean shouldEnable) { // call it AT api response for details

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

    private void setReportField(boolean shouldEnable) {  // call it AT api response for details
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
        // 1 API now
        // TempalteDTOintent
        // Backend --> returns NotificationReportDTO
        // success
        // based on NotificationFlag and ReportFlag setReportField(flag) setNoptificaiton(flag)
        // populate fields
        // notification --> split iterate set chips --> setupPhoneCheckBoxes(NotificationDTO retrievedNotification)
        // reportStatus
        //if (repeatStatusPopulate.getRepeatIntervalType() == null) {
//                            selectedInterval = "-------";
//                        }else{
//                            selectedInterval = repeatStatusPopulate.getRepeatIntervalType();
//
//                            if (selectedInterval.equals("Others")) {
//                                otherRepeatInterval = repeatStatusPopulate.getRepeatInterval();
//                            }
//                        }
//                        setupIntervalSelection();

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

        startDateEditText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(NotificationSetting.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    startDateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });

        // Time
        timeEditText.setOnClickListener(v -> {
            if (!startDateEditText.getText().toString().trim().isEmpty()) {
                // Get the current time
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(NotificationSetting.this, (view, hourOfDay, minute1) -> {
                    // Set the selected time to the EditText
                    timeEditText.setText(hourOfDay + ":" + String.format("%02d", minute1));
                }, hour, minute, true); // Use 24-hour format
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
            finish();
        });
    }
//===========================================================================================================================

    // Validate all the fields
    private boolean validateFields() {

        // if Notification Flag enabled
        // check interval time and at least 1 selected chip

        // if report flag enabled
        // start date
        // time
        // interval

        return true;
    }

    private void saveNotificationReport() {
        // create NotificationDTO


        // create ReportStatusDTO
        //        if (selectedInterval.equals("Others")) {
//            // Get customIntervalEditText
//            EditText customIntervalEditText = findViewById(R.id.custom_interval_edittext);
//            String customInterval = customIntervalEditText.getText().toString().trim();
//            repeatStatusDTO.setRepeatInterval(customInterval);
//        } else if (selectedInterval.equals("-------")) {
//            repeatStatusDTO.setRepeatInterval(null);
//        } else if (selectedInterval.equals("Daily")) {
//            repeatStatusDTO.setRepeatInterval(String.valueOf(24 * 60));
//        } else if (selectedInterval.equals("Weekly")) {
//            repeatStatusDTO.setRepeatInterval(String.valueOf(7 * 24 * 60));
//        } else if (selectedInterval.equals("Monthly")) {
//            repeatStatusDTO.setRepeatInterval(String.valueOf(30 * 24 * 60));
//        } else if (selectedInterval.equals("Yearly")) {
//            repeatStatusDTO.setRepeatInterval(String.valueOf(365 * 24 * 60));
//        }


        // Response success
        //if Flag True
        // setup system notification based on the Notification day and time
        // AlarmManager with uid UserUID+TemplateID
        // Else
        // Delete alarmmanger based on uid UserUID+TemplateID
    }
}
