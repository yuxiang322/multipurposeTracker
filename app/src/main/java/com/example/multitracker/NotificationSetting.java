package com.example.multitracker;

import static com.example.multitracker.commonUtil.GlobalConstant.userID;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.multitracker.api.NotificationAPI;
import com.example.multitracker.api.RepeatStatusAPI;
import com.example.multitracker.api.UserDetailsAPI;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.NotificationDTO;
import com.example.multitracker.dto.RepeatStatusDTO;
import com.example.multitracker.dto.TemplateDTO;
import com.example.multitracker.dto.UserDetailsDTO;

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
        // Get intent
        setupIntent();
        // initial interval selection
        setupIntervalSelection();
        // Get user details
        setUpUserDetails();
        // Populate notification
        populateNotification();
        // Set up switch listener
        setupSwitchListener();
        // Set up date and time
        setUpDateTimeDialog();
        // Save and cancel
        setupSaveCancel();
    }
    private void setupIntent() {
        Intent intent = getIntent();
        templateDTOIntent = intent.getParcelableExtra("templateObject");
    }
    private void setupSwitchListener() {
        SwitchCompat notificationSwitch = findViewById(R.id.notificationSwitchCompat);
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    setFieldsEnabledStatus(false);
                } else {
                    setFieldsEnabledStatus(true);
                }
            }
        });
    }
    private void setFieldsEnabledStatus(boolean shouldEnable) {

        EditText repeatStatusStartDate = findViewById(R.id.repeatStatusStartDate);
        repeatStatusStartDate.setEnabled(shouldEnable);

        EditText repeatStatusTime = findViewById(R.id.repeatStatusTime);
        repeatStatusTime.setEnabled(shouldEnable);

        CheckBox emailCheckbox = findViewById(R.id.emailCheckbox);
        emailCheckbox.setEnabled(shouldEnable);

        CheckBox smsCheckbox = findViewById(R.id.smsCheckbox);
        smsCheckbox.setEnabled(shouldEnable);

        CheckBox whatappsCheckbox = findViewById(R.id.whatappsCheckbox);
        whatappsCheckbox.setEnabled(shouldEnable);

        Spinner intervalSpinner = findViewById(R.id.interval_spinner);
        intervalSpinner.setEnabled(shouldEnable);

        EditText customIntervalEditText = findViewById(R.id.custom_interval_edittext);
        customIntervalEditText.setEnabled(shouldEnable);
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
    private void setUpDateTimeDialog() {
        EditText startDateEditText = findViewById(R.id.repeatStatusStartDate);
        EditText timeEditText = findViewById(R.id.repeatStatusTime);
        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        // Time
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startDateEditText.getText().toString().trim().isEmpty()) {
                    // Get the current time
                    final Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(NotificationSetting.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            // Set the selected time to the EditText
                            timeEditText.setText(hourOfDay + ":" + String.format("%02d", minute));
                        }
                    }, hour, minute, true); // Use 24-hour format
                    timePickerDialog.show();
                } else {
                    startDateEditText.setError("Please select a start date first");
                }
            }
        });
    }
    private void populateNotification() {
        NotificationAPI notificationAPI = RetrofitClientInstance.getRetrofitInstance().create(NotificationAPI.class);
        Call<NotificationDTO> notificationDTOCall = notificationAPI.getNotification(templateDTOIntent);
        notificationDTOCall.enqueue(new Callback<NotificationDTO>() {
            @Override
            public void onResponse(Call<NotificationDTO> call, Response<NotificationDTO> response) {
                if (response.isSuccessful()) {
                    SwitchCompat notificationSwitch = findViewById(R.id.notificationSwitchCompat);
                    NotificationDTO retrieveNotification = response.body();

                    if (retrieveNotification != null && retrieveNotification.getNotificationFlag() != null) {

                        notificationID = retrieveNotification.getNotificationID();
                        notificationSwitch.setChecked(retrieveNotification.getNotificationFlag());
                        // Set up email phone checkboxes
                        setupEmailPhoneCheckBoxes(retrieveNotification);
                        if(!retrieveNotification.getNotificationFlag()){
                            setFieldsEnabledStatus(false);
                        }
                        // Get Repeat Status using the notification id. APi request
                        populateRepeatStatus(retrieveNotification);
                    }
                    Toast.makeText(NotificationSetting.this, "Notification processed ", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    // else response 404 do not populate.
                    setFieldsEnabledStatus(false);
                    Toast.makeText(NotificationSetting.this, "NULL no notifications ", Toast.LENGTH_LONG).show();
                } else {
                    // error message.
                }
            }
            @Override
            public void onFailure(Call<NotificationDTO> call, Throwable t) {

            }
        });
    }
    private void setupEmailPhoneCheckBoxes(NotificationDTO retrievedNotification) {
        CheckBox smsCheckbox = findViewById(R.id.smsCheckbox);
        CheckBox whatappsCheckbox = findViewById(R.id.whatappsCheckbox);
        CheckBox emailCheckbox = findViewById(R.id.emailCheckbox);
        EditText email = findViewById(R.id.notificationEmail);
        EditText phone = findViewById(R.id.notificationPhone);

        if (retrievedNotification.getEmailFlag() != null) {
            emailCheckbox.setChecked(retrievedNotification.getEmailFlag());
        }
        if (retrievedNotification.getWhatsAppFlag() != null) {
            whatappsCheckbox.setChecked(retrievedNotification.getWhatsAppFlag());
        }
        if (retrievedNotification.getSmsFlag() != null) {
            smsCheckbox.setChecked(retrievedNotification.getSmsFlag());
        }
        // Email and Phone check
        if (email.getText() == null) {
            emailCheckbox.setEnabled(false);
        } else {
            emailCheckbox.setEnabled(true);
        }

        if (phone.getText() == null) {
            smsCheckbox.setEnabled(false);
            whatappsCheckbox.setEnabled(false);
        } else {
            smsCheckbox.setEnabled(true);
            whatappsCheckbox.setEnabled(true);
        }
    }
    private void populateRepeatStatus(NotificationDTO notificationID) {
        RepeatStatusAPI repeatStatusAPI = RetrofitClientInstance.getRetrofitInstance().create(RepeatStatusAPI.class);
        Call<RepeatStatusDTO> repeatStatusDTOCall = repeatStatusAPI.getRepeatStatus(notificationID);
        repeatStatusDTOCall.enqueue(new Callback<RepeatStatusDTO>() {
            @Override
            public void onResponse(Call<RepeatStatusDTO> call, Response<RepeatStatusDTO> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        RepeatStatusDTO repeatStatusPopulate = response.body();

                        repeatID = repeatStatusPopulate.getRepeatID();
                        EditText repeatDate = findViewById(R.id.repeatStatusStartDate);
                        repeatDate.setText(repeatStatusPopulate.getRepeatStartDate());
                        EditText repeatTime = findViewById(R.id.repeatStatusTime);
                        repeatTime.setText(repeatStatusPopulate.getRepeatStartTime());

                        if (repeatStatusPopulate.getRepeatIntervalType() == null) {
                            selectedInterval = "-------";
                        }else{
                            selectedInterval = repeatStatusPopulate.getRepeatIntervalType();

                            if (selectedInterval.equals("Others")) {
                                otherRepeatInterval = repeatStatusPopulate.getRepeatInterval();
                            }
                        }
                        setupIntervalSelection();
                    }
                    Toast.makeText(NotificationSetting.this, "Repeat Status processed ", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    // else response 404 do not populate.
                    Toast.makeText(NotificationSetting.this, "Null no repeat Status ", Toast.LENGTH_LONG).show();
                } else {
                    // error message.
                }
            }
            @Override
            public void onFailure(Call<RepeatStatusDTO> call, Throwable t) {

            }
        });
    }
    private void setupSaveCancel() {
        Button saveNotification = findViewById(R.id.saveNotification);
        Button cancelNotification = findViewById(R.id.cancelNotification);
        cancelNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Save button
        saveNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    saveNotificationData();

                } else {
                    Toast.makeText(NotificationSetting.this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
    // Validate all the fields
    private boolean validateFields() {
        EditText customIntervalField = findViewById(R.id.custom_interval_edittext);
        Spinner intervalSpinner = findViewById(R.id.interval_spinner);
        EditText repeatStatusStartDate = findViewById(R.id.repeatStatusStartDate);
        EditText repeatStatusTime = findViewById(R.id.repeatStatusTime);
        // Clear
        customIntervalField.setError(null);
        repeatStatusStartDate.setError(null);
        repeatStatusTime.setError(null);

        if ("Others".equals(intervalSpinner.getSelectedItem().toString()) &&
                customIntervalField.getText().toString().trim().isEmpty()) {
            customIntervalField.setError("Please enter a custom interval");
            return false;
        }
        if (repeatStatusStartDate.getText().toString().trim().isEmpty()) {
            repeatStatusStartDate.setError("Please select a start date");
            return false;
        }
        if (repeatStatusTime.getText().toString().trim().isEmpty()) {
            repeatStatusTime.setError("Please select a time");
            return false;
        }

        return true;
    }
    private void saveNotificationData() {
        CheckBox smsCheckbox = findViewById(R.id.smsCheckbox);
        CheckBox whatappsCheckbox = findViewById(R.id.whatappsCheckbox);
        CheckBox emailCheckbox = findViewById(R.id.emailCheckbox);
        SwitchCompat notificationSwitch = findViewById(R.id.notificationSwitchCompat);

        // Create the DTO for notification data
        NotificationDTO notificationDTO = new NotificationDTO();

        Log.d("NotificationID", "Notif ID??? Save" + notificationID);
        notificationDTO.setNotificationID(notificationID);
        notificationDTO.setSmsFlag(smsCheckbox.isChecked());
        notificationDTO.setWhatsAppFlag(whatappsCheckbox.isChecked());
        notificationDTO.setEmailFlag(emailCheckbox.isChecked());
        notificationDTO.setNotificationFlag(notificationSwitch.isChecked());

        NotificationAPI notificationAPI = RetrofitClientInstance.getRetrofitInstance().create(NotificationAPI.class);
        Call<String> notificationAPICall = notificationAPI.updateNotification(notificationDTO);
        notificationAPICall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseText = response.body();
                    saveRepeatStatusData();
                    // Toast
                } else {
                    // Error
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void saveRepeatStatusData() {
        EditText repeatStatusStartDate = findViewById(R.id.repeatStatusStartDate);
        EditText repeatStatusTime = findViewById(R.id.repeatStatusTime);
        RepeatStatusDTO repeatStatusDTO = new RepeatStatusDTO();
        // Start date, time, interval type
        repeatStatusDTO.setRepeatID(repeatID);
        repeatStatusDTO.setNotificationID(notificationID);
        repeatStatusDTO.setRepeatStartDate(repeatStatusStartDate.getText().toString());
        repeatStatusDTO.setRepeatStartTime(repeatStatusTime.getText().toString());
        repeatStatusDTO.setRepeatIntervalType(selectedInterval);
        // Repeat Interval
        if (selectedInterval.equals("Others")) {
            // Get customIntervalEditText
            EditText customIntervalEditText = findViewById(R.id.custom_interval_edittext);
            String customInterval = customIntervalEditText.getText().toString().trim();
            repeatStatusDTO.setRepeatInterval(customInterval);
        } else if (selectedInterval.equals("-------")) {
            repeatStatusDTO.setRepeatInterval(null);
        } else if (selectedInterval.equals("Daily")) {
            repeatStatusDTO.setRepeatInterval(String.valueOf(24 * 60));
        } else if (selectedInterval.equals("Weekly")) {
            repeatStatusDTO.setRepeatInterval(String.valueOf(7 * 24 * 60));
        } else if (selectedInterval.equals("Monthly")) {
            repeatStatusDTO.setRepeatInterval(String.valueOf(30 * 24 * 60));
        } else if (selectedInterval.equals("Yearly")) {
            repeatStatusDTO.setRepeatInterval(String.valueOf(365 * 24 * 60));
        }
        // call api
        RepeatStatusAPI repeatStatusAPI = RetrofitClientInstance.getRetrofitInstance().create(RepeatStatusAPI.class);
        Call<String> repeatStatusCall = repeatStatusAPI.updateRepeatStatus(repeatStatusDTO);
        repeatStatusCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseText = response.body();
                    // Toast
                } else {
                    // Error
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void setupIntervalSelection() {
        Spinner intervalSpinner = findViewById(R.id.interval_spinner);
        EditText customIntervalEditText = findViewById(R.id.custom_interval_edittext);
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
}
