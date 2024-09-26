package com.example.multitracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.api.TemplatesAPI;
import com.example.multitracker.commonUtil.GlobalConstant;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.TemplateDTO;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTemplate extends AppCompatActivity {
    private EditText nameEditText, descriptionEditText;
    private Button addTemplateButton, cancelTemplateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_template);

        // Initialize the EditText and Button views
        initViews();

        setupListeners();
    }

    private void initViews() {
        nameEditText = findViewById(R.id.templateName);
        descriptionEditText = findViewById(R.id.templateDescription);
        addTemplateButton = findViewById(R.id.addTemplate);
        cancelTemplateButton = findViewById(R.id.cancelTemplate);
    }

    private void setupListeners(){
        // Set onClickListener for the "Add" button
        addTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndAddTemplate();
            }
        });

        // Set onClickListener for the "Cancel" button
        cancelTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFieldsAndFinish();
            }
        });
    }

    private void validateAndAddTemplate() {
        // Get the input from EditText fields
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        // Validate the input fields
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            nameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(description)) {
            descriptionEditText.setError("Description is required");
            descriptionEditText.requestFocus();
            return;
        }

        // If validation passes, show a success message and proceed
        Toast.makeText(this, "Template added successfully", Toast.LENGTH_SHORT).show();

        // Implement the logic to call the API and add the template
        // Create the TemplateDTO object to send in the API request
        TemplateDTO templateRequest = new TemplateDTO();
        templateRequest.setTemplateName(name);
        templateRequest.setTemplateDescription(description);
        templateRequest.setUserUID(GlobalConstant.userID); // Replace with actual user ID

        // Convert the current time to UTC and format it as a string
        LocalDateTime dateCreated = LocalDateTime.now(ZoneOffset.UTC);
       // Set the dateCreated string in the DTO
        templateRequest.setDateCreated(dateCreated);

        // Use Retrofit to make the API call
        TemplatesAPI createApiService = RetrofitClientInstance.getRetrofitInstance().create(TemplatesAPI.class);
        Call<String> call = createApiService.createTemplates(templateRequest);

        // Enqueue the call to run asynchronously
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle the success response
                    String apiResponse = response.body();
                    Toast.makeText(CreateTemplate.this, apiResponse, Toast.LENGTH_SHORT).show();
                    // Close the activity or navigate back after successful creation
                    finish();
                } else {
                    // Handle the failure response
                    Toast.makeText(CreateTemplate.this, "Failed to add template", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle the error
                Toast.makeText(CreateTemplate.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFieldsAndFinish() {
        // Clear the input fields and finish the activity
        nameEditText.setText("");
        descriptionEditText.setText("");
        finish();
    }
}
