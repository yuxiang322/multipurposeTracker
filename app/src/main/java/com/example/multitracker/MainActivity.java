package com.example.multitracker;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.api.MainPageAPI;
import com.example.multitracker.commonUtil.PasswordEncryption;
import com.example.multitracker.commonUtil.RetrofitClientInstanceJson;
import com.example.multitracker.commonUtil.RetrofitClientInstancePlain;
import com.example.multitracker.dto.LoginRequestDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
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
                Intent intent = new Intent(MainActivity.this, RegistrationPage.class);
                startActivity(intent);
            }
        });
    }

    private void setupLogin() {
        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameEditText = findViewById(R.id.username);
                EditText passwordEditText = findViewById(R.id.password);
                String username = String.valueOf(usernameEditText.getText());
                String password = String.valueOf(passwordEditText.getText());

                if (!username.isEmpty() && !password.isEmpty()) {
                    validateLogin(username, password);
                } else {
                    Toast.makeText(MainActivity.this, "Fill up the required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validateLogin(String username, String password) {

        // Encrypt password
        PasswordEncryption passwordEncryption = new PasswordEncryption();
        String encryptedPassword = passwordEncryption.encrypt(password);

        // Call API
        LoginRequestDTO loginRequest = new LoginRequestDTO(username, encryptedPassword);
        MainPageAPI loginService = RetrofitClientInstanceJson.getRetrofitInstance().create(MainPageAPI.class);
        Call<String> call = loginService.loginUser(loginRequest);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    //Authenticate token with firebase
                    firebaseAuthentication(token);
                } else {
                    // Handle login failure
                    Toast.makeText(MainActivity.this, "Login failed: Invalid Username/Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                            // Redirect user to homepage
                            Intent intent = new Intent(MainActivity.this, HomePage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // sign in fails
                            Log.w("TAG", "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}