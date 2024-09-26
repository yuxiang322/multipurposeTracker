package com.example.multitracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.dto.TemplateDTO;

public class TemplateDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_detail);
        // Setup intent
        setupIntent();

    }

    private void setupIntent(){
        // Retrieve the Intent that started this Activity
        Intent intent = getIntent();

        // Check if the Intent contains the TemplateDTO object
        if (intent != null && intent.hasExtra("TEMPLATE_KEY")) {
            // Get the TemplateDTO object from the Intent
            TemplateDTO templateDTO = intent.getParcelableExtra("TEMPLATE_KEY");

            if (templateDTO != null) {
                // Use the TemplateDTO object as needed
                // For example, display its data in the UI
                displayTemplateDetails(templateDTO);
            }
        }
    }

    private void displayTemplateDetails(TemplateDTO templateDTO) {
        // Find the TextViews or other UI elements to display the details
        EditText templateNameEditText = findViewById(R.id.templateName);
        EditText descriptionEditText = findViewById(R.id.templateDescription);
        // Example of how to set data
        templateNameEditText.setText(templateDTO.getTemplateName());
        descriptionEditText.setText(templateDTO.getTemplateDescription());
    }
}
