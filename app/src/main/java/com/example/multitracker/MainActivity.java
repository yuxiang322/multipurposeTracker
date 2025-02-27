package com.example.multitracker;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.api.MainPageAPI;
import com.example.multitracker.commonUtil.GlobalConstant;
import com.example.multitracker.commonUtil.PasswordEncryption;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.LoginRequestDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Redirect user to homepage
//        Intent intent = new Intent(MainActivity.this, HomePage.class);
//        startActivity(intent);
        mAuth = FirebaseAuth.getInstance();
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        // set up login api
        setupLogin();
        // set up register
        setupRegistration();
    }
    private void setupRegistration() {
        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCredentials();
                Intent intent = new Intent(MainActivity.this, RegistrationPage.class);
                startActivity(intent);
            }
        });
    }
    private void setupLogin() {
        Button loginButton = findViewById(R.id.login);
        Button registerButton = findViewById(R.id.register);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(usernameEditText.getText());
                String password = String.valueOf(passwordEditText.getText());

                if (!username.isEmpty() && !password.isEmpty()) {

                    // Hide button and show progress
                    loginButton.setVisibility(View.GONE);
                    registerButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    validateLogin(username, password);
                    clearCredentials();
                } else {
                    Toast.makeText(MainActivity.this, "Fill up the required fields", Toast.LENGTH_LONG).show();
                }
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
        Call<String> call = loginService.loginUser(loginRequest);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                    //Authenticate token with firebase
                    firebaseAuthentication(token);
                } else {
                    // Log error body
                    String errorBody = null;
                    try {
                        errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                    } catch (IOException e) {
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
            public void onFailure(Call<String> call, Throwable t) {
                // Re-enable the button and hide progress
                loginButton.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void firebaseAuthentication(String firebaseToken) {
        mAuth.signInWithCustomToken(firebaseToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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
                    }
                });
    }

    private void clearCredentials(){
        usernameEditText.setText("");
        passwordEditText.setText("");
    }
}