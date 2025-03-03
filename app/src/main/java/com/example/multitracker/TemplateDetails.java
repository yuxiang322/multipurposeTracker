package com.example.multitracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.api.TableManagementAPI;
import com.example.multitracker.api.TemplatesAPI;
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
    private List<RetrieveTableDetailsDTO> currentTemplateFullDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_detail);

        // Check for login
        // intent pass TemplateID -> can auto go in?


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
        // rmove table
        removeTable();
        // preview export button setup
        previewExportSetup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialUIState();
        populateTables();
    }

    private void initialUIState() {
        Button saveButton = findViewById(R.id.saveTemplateDetails);
        Button cancelButton = findViewById(R.id.cancelSaveTemplate);
        Button addTable = findViewById(R.id.addTable);
        Button deleteTable = findViewById(R.id.removeTable);
        Button previewButton = findViewById(R.id.previewButton);
        saveButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        addTable.setVisibility(View.GONE);
        deleteTable.setVisibility(View.GONE);
        previewButton.setVisibility(View.VISIBLE);
    }

    private void enableDisableButtons() {
        Button saveButton = findViewById(R.id.saveTemplateDetails);
        Button cancelButton = findViewById(R.id.cancelSaveTemplate);
        Button addTable = findViewById(R.id.addTable);
        Button previewButton = findViewById(R.id.previewButton);
        saveButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        addTable.setVisibility(View.VISIBLE);
        previewButton.setVisibility(View.GONE);
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
                enableDisableButtons();
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
        Button saveButton = findViewById(R.id.saveTemplateDetails);
        Button cancelButton = findViewById(R.id.cancelSaveTemplate);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText templateNameEdit = findViewById(R.id.templateName);
                EditText templateDescriptionEdit = findViewById(R.id.templateDescription);

                String templateNameUpdate = templateNameEdit.getText().toString().trim();
                String templateDescriptionUpdate = templateDescriptionEdit.getText().toString().trim();

                if ((!templateNameUpdate.equals(templateObject.getTemplateName())) || !(templateDescriptionUpdate.equals(templateObject.getTemplateDescription()))) {
                    // Save templateDetails
                    TemplateDTO updateTemplateDetails = new TemplateDTO();
                    updateTemplateDetails.setTemplateID(templateID);
                    updateTemplateDetails.setTemplateName(templateNameUpdate);
                    updateTemplateDetails.setTemplateDescription(templateDescriptionUpdate);

                    TemplatesAPI saveTemplateDetails = RetrofitClientInstance.getRetrofitInstance().create(TemplatesAPI.class);
                    Call<String> saveTemplateCall = saveTemplateDetails.saveTemplateDetails(updateTemplateDetails);

                    saveTemplateCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(TemplateDetails.this, response.body(), Toast.LENGTH_SHORT).show();

                                templateNameEdit.setText(templateNameUpdate);
                                templateDescriptionEdit.setText(templateDescriptionUpdate);
                                templateObject.setTemplateName(templateNameUpdate);
                                templateObject.setTemplateDescription(templateDescriptionUpdate);
                                stateEditTemplate(false);
                                initialUIState();
                            } else {
                                Toast.makeText(TemplateDetails.this, response.body(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });

                } else {
                    Toast.makeText(TemplateDetails.this, "No changes to save for Template Name or Description", Toast.LENGTH_LONG).show();
                }
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

    // Preview export
    private void previewExportSetup(){
        Button previewButton = findViewById(R.id.previewButton);

        previewButton.setOnClickListener(v -> {
            Intent previewData = new Intent(this, PreviewExport.class);
            previewData.putParcelableArrayListExtra("previewData", (ArrayList<? extends Parcelable>) currentTemplateFullDetails);
            previewData.putExtra("previewTemplateData", templateObject);
            Log.d("previewOfData", "TEMPLATE DETAILS PAGE arraylist: " + currentTemplateFullDetails.size());
            startActivity(previewData);
        });
    }

    // Caching solve real time sync..
    private void populateTables() {
        TableManagementAPI retrieveTableApi = RetrofitClientInstance.getRetrofitInstance().create(TableManagementAPI.class);
        Call<List<RetrieveTableDetailsDTO>> tableList = retrieveTableApi.retrieveTable(templateObject);
        tableList.enqueue(new Callback<List<RetrieveTableDetailsDTO>>() {
            @Override
            public void onResponse(Call<List<RetrieveTableDetailsDTO>> call, Response<List<RetrieveTableDetailsDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    addingTables(response.body());
                    currentTemplateFullDetails.clear();
                    currentTemplateFullDetails.addAll(response.body());
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
                tableDeleteCheckbox.setTag(table.getTemplateTables().getTableID());

                tableName.setOnClickListener(v -> {
                    tableDataManagement(table);
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

    // Remove button  CACHING REQUIRED
    private void removeTable() {
        Button removeTable = findViewById(R.id.removeTable);

        removeTable.setOnClickListener(v -> {
            // Confirmation
            new AlertDialog.Builder(this)
                    .setTitle("confirm remove")
                    .setMessage("Are you sure?")
                    .setPositiveButton("yes", ((dialog, which) -> {
                        // run check on current child tables checked from view templateLinearLayout
                        // only remove checks
                        // List of TableID
                        // api delete tableDetails,Headerdetails, templatedetails Delete.
                        List<Integer> tableIDsDelete = new ArrayList<>();
                        LinearLayout tablesToDeleteParent = findViewById(R.id.templateTableLinearLayout);

                        for (int i = 0; i < tablesToDeleteParent.getChildCount(); i++) {
                            View tableToDeleteView = tablesToDeleteParent.getChildAt(i);
                            CheckBox deleteCheck = tableToDeleteView.findViewById(R.id.deleteTableCheckbox);

                            if (deleteCheck.isChecked()) {
                                tableIDsDelete.add((Integer) deleteCheck.getTag());
                            }
                        }
                        Log.d("TableDelete", tableIDsDelete.toString()); // test

                        TableManagementAPI tableDelete = RetrofitClientInstance.getRetrofitInstance().create(TableManagementAPI.class);
                        Call<String> deleteCall = tableDelete.deleteTable(tableIDsDelete);
                        deleteCall.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(TemplateDetails.this, "Deletion " + response.body(), Toast.LENGTH_SHORT).show();
                                    initialUIState();
                                    populateTables();// CACHING
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

                    }))
                    .setNegativeButton("no", ((dialog, which) -> {
                        dialog.dismiss();
                    }))
                    .show();

        });
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

    // table data
    private void tableDataManagement(RetrieveTableDetailsDTO tableData) {
        Intent passTableData = new Intent(this, TableDataManagement.class);
        passTableData.putExtra("tableData", tableData);
        startActivity(passTableData);
    }
}
