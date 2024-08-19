package com.example.multitracker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.api.MainPageAPI;
import com.example.multitracker.commonUtil.PasswordEncryption;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.RegisterRequestDTO;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        // Set up registration
        registrationSetup();
    }

    private void registrationSetup(){

        Button submitButton = findViewById(R.id.registrationSubmit);
        Button cancelButton = findViewById(R.id.registrationCancel);
        EditText usernameEditText = findViewById(R.id.registrationUsername);
        EditText passwordEditText = findViewById(R.id.registrationPassword);
        EditText nameEditText = findViewById(R.id.registrationName);
        EditText emailEditText = findViewById(R.id.registrationEmail);
        Spinner countryCodeSpinner = findViewById(R.id.registrationCountryCode);
        EditText phoneEditText = findViewById(R.id.registrationPhone);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                if (username.isEmpty()) {
                    usernameEditText.setError("Username is required");
                    usernameEditText.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    passwordEditText.setError("Password is required");
                    passwordEditText.requestFocus();
                    return;
                }

                if (name.isEmpty()) {
                    nameEditText.setError("Name is required");
                    nameEditText.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    emailEditText.setError("Email is required");
                    emailEditText.requestFocus();
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Invalid email format");
                    emailEditText.requestFocus();
                    return;
                }

                if (phone.isEmpty()) {
                    phoneEditText.setError("Phone number is required");
                    phoneEditText.requestFocus();
                    return;
                }
                String finalPhone = countryCodeSpinner.getSelectedItem().toString() + phone;
                Log.d("register", "number: " + finalPhone);
                performRegistration(username, password, name, email, finalPhone);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEditText.setText("");
                passwordEditText.setText("");
                nameEditText.setText("");
                emailEditText.setText("");
                phoneEditText.setText("");
                countryCodeSpinner.setSelection(0); // Reset spinner to the first item
                finish();
            }
        });
    }

    private void performRegistration(String username, String password, String name, String email, String phone){
        // Encrypt password
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        String encryptedPassword = passwordEncryption.encrypt(password);
        Log.d("register", "password: " + encryptedPassword);

        // Call API
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(username, encryptedPassword, name, email, phone);
        MainPageAPI registerService = RetrofitClientInstance.getRetrofitInstance().create(MainPageAPI.class);
        Call<String> call = registerService.registerUser(registerRequest);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String registrationResponse = response.body();
                    if (registrationResponse != null) {
                        Toast.makeText(RegistrationPage.this, registrationResponse, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RegistrationPage.this, "Empty response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Log error body
                    String errorBody = null;
                    try {
                        errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                    } catch (IOException e) {
                        Log.e("register", "Error reading error body", e);
                    }
                    Log.d("register", "Error response body: " + errorBody);
                    Toast.makeText(RegistrationPage.this, "Error: " + response.code() + " - " + errorBody, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(RegistrationPage.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("register", "onFailure: " + t.getMessage());
            }
        });
    }
}
