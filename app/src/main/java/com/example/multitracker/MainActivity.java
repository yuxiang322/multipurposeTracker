package com.example.multitracker;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.api.MainPageAPI;
import com.example.multitracker.commonUtil.EncryptedSharePreferenceUtils;
import com.example.multitracker.commonUtil.GlobalConstant;
import com.example.multitracker.commonUtil.JwtUtils;
import com.example.multitracker.commonUtil.PasswordEncryption;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.commonUtil.TimeUtil;
import com.example.multitracker.dto.JwtDTO;
import com.example.multitracker.dto.LoginRequestDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private boolean isFromNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getIntent = getIntent();
        if(getIntent != null){
            isFromNotification = getIntent.getBooleanExtra("isFromNotification", false);
        }

        // verify login session
        if(!isFromNotification){
            verifyLoginSession();
        }

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        // set up login api
        setupLogin();
        // set up register
        setupRegistration();
    }

    private void verifyLoginSession() {
        Map<String, ?> jwtTokenMap = EncryptedSharePreferenceUtils.getEncryptedSharePreference(this, GlobalConstant.jwtSPLoginFileName, null);

        if (jwtTokenMap != null && !jwtTokenMap.isEmpty()) {
            Log.d("JwtLoginTest", "=========\n Token != null, token not empty??");
            String latestToken = null;
            long latestExpiryDate = 0;
            String latestUserUID = null;

            for (Map.Entry<String, ?> entry : jwtTokenMap.entrySet()) {
                Log.d("JwtLoginTest", "Looping my map???");
                String token = (String) entry.getValue();
                Log.d("JwtLoginTest", "token value???" + token);
                long expiryDate = JwtUtils.getTokenExpiry(token);
                Log.d("JwtLoginTest", "ExpiryDate???" + new Date(expiryDate));

                if (!JwtUtils.isTokenExpired(token)) {

                    if (latestExpiryDate == 0 || expiryDate > latestExpiryDate) {
                        latestExpiryDate = expiryDate;
                        latestToken = token;
                        latestUserUID = JwtUtils.extractUserUID(token);
                        Log.d("JwtLoginTest", "LatestExpiryDate INITIAL" + new Date(latestExpiryDate));
                    }
                }
            }

            if (TimeUtil.isDateValid(latestExpiryDate) && latestUserUID != null && latestToken != null) {
                GlobalConstant.userID = latestUserUID;
                Log.d("JwtLoginTest", "Token not expired, Lastest user login: " + latestUserUID);
                redirectToHomepageEmptyIntent();
            }
        }
    }

    private void setupRegistration() {
        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(view -> {
            clearCredentials();
            Intent intent = new Intent(MainActivity.this, RegistrationPage.class);
            startActivity(intent);
        });
    }

    private void setupLogin() {
        Button loginButton = findViewById(R.id.login);
        Button registerButton = findViewById(R.id.register);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {

                // Hide button and show progress
                loginButton.setVisibility(View.GONE);
                registerButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                validateLogin(username, password);
                new Handler().postDelayed(() -> clearCredentials(), 1000);

            } else {
                Toast.makeText(MainActivity.this, "Fill up the required fields", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void validateLogin(String username, String password) {
        Button loginButton = findViewById(R.id.login);
        Button registerButton = findViewById(R.id.register);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        // Encrypt password
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        String encryptedPassword = passwordEncryption.encrypt(password);
        LoginRequestDTO loginRequest = new LoginRequestDTO(username, encryptedPassword);
        MainPageAPI loginService = RetrofitClientInstance.getRetrofitInstance().create(MainPageAPI.class);

        // modify JWT
        Call<JwtDTO> call = loginService.loginUser(loginRequest);

        call.enqueue(new Callback<JwtDTO>() {
            @Override
            public void onResponse(Call<JwtDTO> call, Response<JwtDTO> response) {
                if (response.isSuccessful()) {
                    JwtDTO responseJWT = response.body();

                    if (responseJWT != null) {
                        Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                        loginSuccessProcessing(responseJWT);
                        homePageIntentFromNotification();
                    }
                } else {
                    String errorBody = null;
                    try {
                        errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                    } catch (Exception e) {
                        Log.e("login", "Error reading error body", e);
                    }
                    Toast.makeText(MainActivity.this, "Error: " + response.code() + " - " + errorBody, Toast.LENGTH_LONG).show();
                }

                // Show button and hide progress
                loginButton.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JwtDTO> call, Throwable t) {
                // Re-enable the button and hide progress
                loginButton.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginSuccessProcessing(JwtDTO responseJWT) {
        // get useruid from token
        GlobalConstant.userID = JwtUtils.extractUserUID(responseJWT.getToken());
        Log.d("JWTtest", "Jwt TEst UserUID: " + GlobalConstant.userID);
        // store token
        EncryptedSharePreferenceUtils.storeEncryptedSharePreference(this, GlobalConstant.jwtSPLoginFileName, GlobalConstant.userID, responseJWT.getToken());
    }

    private void homePageIntentFromNotification() {
        String userUIDfromForeground = null;
        int fromNotificationTemplateID = -1;

        Intent getIntent = getIntent();

        if (getIntent != null) {
            fromNotificationTemplateID = getIntent.getIntExtra("fromNotificationAlertTemplateID", -1);
            userUIDfromForeground = getIntent.getStringExtra("fromNotificationAlertUserUID");
        }

        if (isFromNotification) {
            Intent homePageIntent = new Intent(MainActivity.this, HomePage.class);
            homePageIntent.putExtra("fromNotificationAlertTemplateID", fromNotificationTemplateID);
            homePageIntent.putExtra("userUIDfromForeground", userUIDfromForeground);
            homePageIntent.putExtra("isFromNotification", isFromNotification);
            homePageIntent.putExtra("isFromNotificationLogin", true);

            startActivity(homePageIntent);
            finish();
        } else {
            redirectToHomepageEmptyIntent();
        }
    }

    private void redirectToHomepageEmptyIntent() {
        Intent homePageIntent = new Intent(MainActivity.this, HomePage.class);
        startActivity(homePageIntent);
        finish();
    }

    private void clearCredentials() {
        usernameEditText.setText("");
        passwordEditText.setText("");
    }


    private void firebaseAuthentication(String firebaseToken) {
        mAuth.signInWithCustomToken(firebaseToken)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = mAuth.getCurrentUser();
                        GlobalConstant.userID = user.getUid();
                        Log.d("Firebase", "User ID stored: " + GlobalConstant.userID);
                        // Redirect user to homepage
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // sign in fails
                        Log.w("Firebase", "signInWithCustomToken:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                    }
                });
    }
}