package com.example.multitracker;

import static com.example.multitracker.commonUtil.GlobalConstant.userID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.commonUtil.GlobalConstant;
import com.example.multitracker.dto.TemplateDTO;

public class TemplateDetails extends AppCompatActivity {

    private TemplateDTO templateDTOnotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_detail);

        // Retrieval of template table details
        setupTemplateIntent();
        // Set up button view Save, Cancel
        initialUIState();
        // Edit button set up
        setupEditButtonListener();
        // Save cancel button listener
        saveCancelButtonListener();
        // Set up notification listener
        notificationButtonListener();
        // Add table button
        addTableButton();

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initialUIState(){
        Button saveButton = findViewById(R.id.saveTemplate);
        Button cancelButton = findViewById(R.id.cancelSaveTemplate);
        Button addTable = findViewById(R.id.addTable);
        saveButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        addTable.setVisibility(View.GONE);
    }
    private void enableButtons(){
        Button saveButton = findViewById(R.id.saveTemplate);
        Button cancelButton = findViewById(R.id.cancelSaveTemplate);
        Button addTable = findViewById(R.id.addTable);
        saveButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        addTable.setVisibility(View.VISIBLE);
    }
    private void stateEditTemplate(Boolean editTextFlag){
        EditText templateName = findViewById(R.id.templateName);
        EditText templateDescription = findViewById(R.id.templateDescription);
        templateName.setEnabled(editTextFlag);
        templateDescription.setEnabled(editTextFlag);
    }
    private void setupEditButtonListener(){
        Button editButton = findViewById(R.id.editTemplate);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableButtons();
                // Enable Edit text template name and description
                stateEditTemplate(true);
            }
        });
    }

    // Add table button
    private void addTableButton(){
        Button addTable = findViewById(R.id.addTable);

        addTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // New intent

                // Pass template id

                Intent intent = new Intent(TemplateDetails.this,CreateTable.class);
                startActivity(intent);
            }
        });
    }

    private void saveCancelButtonListener(){
        Button saveButton = findViewById(R.id.saveTemplate);
        Button cancelButton = findViewById(R.id.cancelSaveTemplate);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ??????
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initial UI state
                initialUIState();
                stateEditTemplate(false);
                // Reload text if there are changes made. Populate
                setupTemplateIntent(); //maybe
            }
        });
    }
    private void notificationButtonListener(){
        Button notiButton = findViewById(R.id.topRightNotificationButton);
        notiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TemplateDetails.this,NotificationSetting.class);
                intent.putExtra("templateObject", templateDTOnotification);
                startActivity(intent);
            }
        });
    }

    private void populateTables(TemplateDTO templateDTO){
        // Template dto details to get list of tables

        // populate into the scroll view

    }

    private void setupTemplateIntent(){
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("TEMPLATE_KEY")) {
            TemplateDTO templateDTO = intent.getParcelableExtra("TEMPLATE_KEY");
            if (templateDTO != null) {
                templateDTOnotification = templateDTO;
                displayTemplateDetails(templateDTO);
                // Populate
                populateTables(templateDTO);
            }
        }
    }
    private void displayTemplateDetails(TemplateDTO templateDTO) {
        EditText templateNameEditText = findViewById(R.id.templateName);
        EditText descriptionEditText = findViewById(R.id.templateDescription);

        templateNameEditText.setText(templateDTO.getTemplateName());
        descriptionEditText.setText(templateDTO.getTemplateDescription());
    }
}
