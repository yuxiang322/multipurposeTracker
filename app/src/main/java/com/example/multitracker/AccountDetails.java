package com.example.multitracker;

import static com.example.multitracker.commonUtil.GlobalConstant.userID;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.api.UserDetailsAPI;
import com.example.multitracker.commonUtil.PasswordEncryption;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.PasswordDTO;
import com.example.multitracker.dto.UserDetailsDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountDetails extends AppCompatActivity {
    private EditText emailEditText;
    private EditText phoneEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_page);

        emailEditText = findViewById(R.id.userEmail);
        phoneEditText = findViewById(R.id.userPhone);
        // Setup buttons
        hideAllEditButtons();
        // Edit buttons listener
        setUpEmailEditButtons();
        setUpPhoneEditButtons();
        // change password butotn
        setupChangepassword();
    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpUserDetails();
    }
    // Hide button
    private void hideAllEditButtons() {
        hideEmailEditButtons();
        hidePhoneEditButtons();
    }
    // Setup email buttons
    private void setUpEmailEditButtons() {
        Button editEmail = findViewById(R.id.editEmailButton);
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmailEditButtons();
                emailEditText.setEnabled(true);
                // Setup Ok and Cancel listener
                setUpButtonsEditEmail();
            }
        });
    }
    private void setUpButtonsEditEmail() {
        Button emailOk = findViewById(R.id.emailOk);
        Button emailCancel = findViewById(R.id.emailCancel);
        emailOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
                hideEmailEditButtons();
                emailEditText.setEnabled(false);
            }
        });
        emailCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEmailEditButtons();
                emailEditText.setEnabled(false);
                setUpUserDetails();
            }
        });
    }
    // Edit email api request
    private void updateEmail() {
        String newEmail = emailEditText.getText().toString();

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUserUID(userID);
        userDetailsDTO.setEmail(newEmail);
        // Call api
        UserDetailsAPI userDetailsAPI = RetrofitClientInstance.getRetrofitInstance().create(UserDetailsAPI.class);
        Call<String> call = userDetailsAPI.changeEmail(userDetailsDTO);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle success response
                    String apiResponse = response.body();
                    Toast.makeText(AccountDetails.this, apiResponse, Toast.LENGTH_SHORT).show();
                } else {
                    // Handle failure response
                    Toast.makeText(AccountDetails.this, "Failed to change email", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle the error
                Toast.makeText(AccountDetails.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Setup phone buttons
    private void setUpPhoneEditButtons() {
        Button editPhone = findViewById(R.id.editPhoneButton);
        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoneEditButtons();
                phoneEditText.setEnabled(true);
                // Setup Ok and Cancel listener
                setUpButtonsEditPhone();
            }
        });
    }
    private void setUpButtonsEditPhone() {
        Button phoneOk = findViewById(R.id.phoneOk);
        Button phoneCancel = findViewById(R.id.phoneCancel);
        phoneOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // API request to update phone
                updatePhone();
                hidePhoneEditButtons();
                phoneEditText.setEnabled(false);
            }
        });
        phoneCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePhoneEditButtons();
                phoneEditText.setEnabled(false);
                setUpUserDetails();
            }
        });
    }
    // Edit phone api request
    private void updatePhone() {
        String newPhone = phoneEditText.getText().toString();
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUserUID(userID);
        userDetailsDTO.setPhone(newPhone);
        // Call api
        UserDetailsAPI userDetailsAPI = RetrofitClientInstance.getRetrofitInstance().create(UserDetailsAPI.class);
        Call<String> call = userDetailsAPI.changePhone(userDetailsDTO);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle success response
                    String apiResponse = response.body();
                    Toast.makeText(AccountDetails.this, apiResponse, Toast.LENGTH_SHORT).show();
                } else {
                    // Handle failure response
                    Toast.makeText(AccountDetails.this, "Failed to change phone number", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle the error
                Toast.makeText(AccountDetails.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Hide edit email
    private void hideEmailEditButtons() {
        Button emailOk = findViewById(R.id.emailOk);
        Button emailCancel = findViewById(R.id.emailCancel);
        emailOk.setVisibility(View.GONE);
        emailCancel.setVisibility(View.GONE);
    }
    // Show the buttons for edit email
    private void showEmailEditButtons() {
        Button emailOk = findViewById(R.id.emailOk);
        Button emailCancel = findViewById(R.id.emailCancel);
        emailOk.setVisibility(View.VISIBLE);
        emailCancel.setVisibility(View.VISIBLE);
    }
    // Hide edit Phone
    private void hidePhoneEditButtons() {
        Button phoneOk = findViewById(R.id.phoneOk);
        Button phoneCancel = findViewById(R.id.phoneCancel);
        phoneOk.setVisibility(View.GONE);
        phoneCancel.setVisibility(View.GONE);
    }
    // Show the buttons for edit phone
    private void showPhoneEditButtons() {
        Button phoneOk = findViewById(R.id.phoneOk);
        Button phoneCancel = findViewById(R.id.phoneCancel);
        phoneOk.setVisibility(View.VISIBLE);
        phoneCancel.setVisibility(View.VISIBLE);
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
                    // Populate page
                    if (responseUserDetails != null) {
                        populateDetails(responseUserDetails);
                        Toast.makeText(AccountDetails.this, "Details retrieved", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle the case where response is null
                        Toast.makeText(AccountDetails.this, "Failed to retrieve user details", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the case where response is not successful
                    Toast.makeText(AccountDetails.this, "Failed to retrieve user details", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UserDetailsDTO> call, Throwable t) {
                // Handle network or other errors
                Toast.makeText(AccountDetails.this, "Error retrieving user details: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Populate page
    private void populateDetails(UserDetailsDTO userDetails) {
        TextView userName = findViewById(R.id.userName);
        EditText userEmail = findViewById(R.id.userEmail);
        EditText userPhone = findViewById(R.id.userPhone);
        userName.setText(userDetails.getUserUID());
        userEmail.setText(userDetails.getEmail());
        userPhone.setText(userDetails.getPhone());
    }
    private void setupChangepassword() {
        Button changePasswordButton = findViewById(R.id.changePassword);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });
    }
    private void showChangePasswordDialog() {
        // Inflate dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.change_password_dialog, null);
        // Create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        // Find the EditTexts
        EditText newPassword = dialogView.findViewById(R.id.newPassword);
        EditText confirmPassword = dialogView.findViewById(R.id.confirmPassword);
        // Set up the dialog buttons
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        // Create the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String newPass = newPassword.getText().toString();
            String confirmPass = confirmPassword.getText().toString();
            if (newPass.equals(confirmPass)) {
                changePassword(confirmPass);
                Toast.makeText(AccountDetails.this, "Password changed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(AccountDetails.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void changePassword(String newPassword) {
        // Encrypt password
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        String encryptedPassword = passwordEncryption.encrypt(newPassword);

        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setUserUID(userID);
        passwordDTO.setPassword(encryptedPassword);

        UserDetailsAPI userDetailsAPI = RetrofitClientInstance.getRetrofitInstance().create(UserDetailsAPI.class);
        Call<String> call = userDetailsAPI.changePassword(passwordDTO);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle success response
                    String apiResponse = response.body();
                    Toast.makeText(AccountDetails.this, apiResponse, Toast.LENGTH_SHORT).show();
                } else {
                    // Handle failure response
                    Toast.makeText(AccountDetails.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle the error
                Toast.makeText(AccountDetails.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
