package com.example.multitracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.api.RetrieveTablesAPI;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.RetrieveTableDetailsDTO;
import com.example.multitracker.dto.TemplateDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemplateDetails extends AppCompatActivity {

    private TemplateDTO templateObject;
    private int templateID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_detail);

        // Retrieval of template table details
        setupTemplateIntent();
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
        initialUIState();
        populateTables();
    }

    private void initialUIState() {
        Button saveButton = findViewById(R.id.addTables);
        Button cancelButton = findViewById(R.id.cancelSaveTemplate);
        Button addTable = findViewById(R.id.addTable);
        Button deleteTable = findViewById(R.id.removeTable);
        saveButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        addTable.setVisibility(View.GONE);
        deleteTable.setVisibility(View.GONE);
    }

    private void enableButtons() {
        Button saveButton = findViewById(R.id.addTables);
        Button cancelButton = findViewById(R.id.cancelSaveTemplate);
        Button addTable = findViewById(R.id.addTable);
        saveButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        addTable.setVisibility(View.VISIBLE);
    }

    private void stateEditTemplate(Boolean editTextFlag) {
        EditText templateName = findViewById(R.id.templateName);
        EditText templateDescription = findViewById(R.id.templateDescription);
        templateName.setEnabled(editTextFlag);
        templateDescription.setEnabled(editTextFlag);
    }

    private void setupEditButtonListener() {
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
    private void addTableButton() {
        Button addTable = findViewById(R.id.addTable);

        addTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass template id
                Intent intent = new Intent(TemplateDetails.this, CreateTable.class);
                intent.putExtra("TEMPLATE_ID", templateID);
                startActivity(intent);
            }
        });
    }

    private void notificationButtonListener() {
        Button notiButton = findViewById(R.id.topRightNotificationButton);
        notiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TemplateDetails.this, NotificationSetting.class);
                intent.putExtra("templateObject", templateObject);
                startActivity(intent);
            }
        });
    }

    private void saveCancelButtonListener() {
        Button saveButton = findViewById(R.id.addTables);
        Button cancelButton = findViewById(R.id.cancelSaveTemplate);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Only saves Template information.
                // delete(prompt) and add Table from + button are permanent.
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initial UI state
                initialUIState();
                stateEditTemplate(false);
                // Reload text if there are changes made. Populate
                setupTemplateIntent();
            }
        });
    }

    private void populateTables() {
        RetrieveTablesAPI retrieveTableApi = RetrofitClientInstance.getRetrofitInstance().create(RetrieveTablesAPI.class);
        Call<List<RetrieveTableDetailsDTO>> tableList = retrieveTableApi.retrieveTable(templateObject);
        tableList.enqueue(new Callback<List<RetrieveTableDetailsDTO>>() {
            @Override
            public void onResponse(Call<List<RetrieveTableDetailsDTO>> call, Response<List<RetrieveTableDetailsDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    addingTables(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<RetrieveTableDetailsDTO>> call, Throwable t) {
            }
        });
    }

    private void addingTables(List<RetrieveTableDetailsDTO> currentTables) {
        LinearLayout linearTableList = findViewById(R.id.templateTableLinearLayout);
        linearTableList.removeAllViews();

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        if (!currentTables.isEmpty()) {
            for (RetrieveTableDetailsDTO table : currentTables) {
                // template_tables.xml
                View tableView = layoutInflater.inflate(R.layout.template_tables, linearTableList, false);

                TextView tableName = tableView.findViewById(R.id.templateTableName);
                CheckBox tableDeleteCheckbox = tableView.findViewById(R.id.deleteTableCheckbox);

                tableName.setText(table.getTemplateTables().getTableName());

                tableName.setOnClickListener(v -> {
                    Toast.makeText(TemplateDetails.this, "Clickable", Toast.LENGTH_LONG).show();
                    previewTableData();
                });

                tableDeleteCheckbox.setOnClickListener(v -> {
                    deleteButtonState();
                });

                linearTableList.addView(tableView);
            }
        }

    }

    private void deleteButtonState() {
        LinearLayout linearTableList = findViewById(R.id.templateTableLinearLayout);
        boolean enableDelete = false;

        for (int i = 0; i < linearTableList.getChildCount(); i++) {
            View child = linearTableList.getChildAt(i);

            CheckBox currentChildCheckbox = child.findViewById(R.id.deleteTableCheckbox);

            if (currentChildCheckbox.isChecked()) {
                enableDelete = true;
                break;
            }
        }

        Button deleteTable = findViewById(R.id.removeTable);
        if (enableDelete) {
            deleteTable.setVisibility(View.VISIBLE);
        } else {
            deleteTable.setVisibility(View.GONE);
        }
    }

    // Remove button
    private void removeTable() {
        Button removeTable = findViewById(R.id.removeTable);

        removeTable.setOnClickListener(v -> {
            // run check on current child tables checked from view templateLinearLayout
            // only remove checks
            // List of TableID
            // api delete tableDetails,Headerdetails, templatedetails Delete.
        });
    }

    // Preview of table data
    private void previewTableData() {

    }

    private void setupTemplateIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("TEMPLATE_KEY")) {
            TemplateDTO templateDTO = intent.getParcelableExtra("TEMPLATE_KEY");
            if (templateDTO != null) {
                templateObject = templateDTO;
                templateID = templateObject.getTemplateID();
                displayTemplateDetails(templateDTO);
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
