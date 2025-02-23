package com.example.multitracker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.api.TableManagementAPI;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.RetrieveTableDetailsDTO;
import com.example.multitracker.dto.TableDetailsDTO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableDataManagement extends AppCompatActivity {
    private RetrieveTableDetailsDTO retrieveTableDetailsObject;
    private Map<String, List<String>> dataMap;
    private Boolean saveFlag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_data_management);

        Intent getTableData = getIntent();
        retrieveTableDetailsObject = getTableData.getParcelableExtra("tableData");

        saveFlag = false;
        iniDataMap();
        populateTableLayout();
        // Add data
        addData();
        // Save data
        saveData();
    }

    // initialize dataMap
    private void iniDataMap() {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<String>>>() {
        }.getType();
        dataMap = gson.fromJson(retrieveTableDetailsObject.getTableDetails().getJsonData(), type);
    }

    // add data
    private void addData() {
        Button addDataButton = findViewById(R.id.tableDataAddButton);

        addDataButton.setOnClickListener(v -> {
            LayoutInflater tableDataAdd = LayoutInflater.from(this);
            View addTableDataView = tableDataAdd.inflate(R.layout.table_data_add, null);

            LinearLayout tableDataContainer = addTableDataView.findViewById(R.id.addDataContainer);

            for (String headerName : dataMap.keySet()) {
                View inputView = tableDataAdd.inflate(R.layout.table_data_inputs_field, tableDataContainer, false);

                TextView headerNameText = inputView.findViewById(R.id.tableDataAddName);
                headerNameText.setText(headerName);

                tableDataContainer.addView(inputView);
            }

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setView(addTableDataView);
            alertBuilder.setPositiveButton("Add", (dialog, which) -> {

                for (int i = 0; i < tableDataContainer.getChildCount(); i++) {
                    View tempViewDataContainer = tableDataContainer.getChildAt(i);
                    TextView tempHeaderName = tempViewDataContainer.findViewById(R.id.tableDataAddName);
                    EditText dataAddEdit = tempViewDataContainer.findViewById(R.id.tableDataAddEdit);

                    String headerKey = tempHeaderName.getText().toString();
                    String dataAdd = dataAddEdit.getText().toString();

                    List<String> tempDataList = dataMap.get(headerKey);

                    tempDataList.add(dataAdd.isEmpty() ? null : dataAdd);

                    dataMap.put(headerKey, tempDataList);
                }
                saveFlag = true;
                populateTableLayout();
                dialog.dismiss();
            });

            alertBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            });
            alertBuilder.create().show();
        });
    }

    // save data
    private void saveData() {

        Button saveData = findViewById(R.id.tableDataSaveButton);

        saveData.setOnClickListener(v -> {
            if (saveFlag) {
                // map json dataMap
                // Api call save
                Gson gson = new Gson();
                String saveDataJson = gson.toJson(dataMap);

                TableDetailsDTO tempTableDataSave = new TableDetailsDTO();
                tempTableDataSave.setJsonData(saveDataJson); // temp data
                tempTableDataSave.setTableID(retrieveTableDetailsObject.getTableDetails().getTableID());
                tempTableDataSave.setTableDetailsID(retrieveTableDetailsObject.getTableDetails().getTableDetailsID());

                TableManagementAPI tableDataSaveApi = RetrofitClientInstance.getRetrofitInstance().create(TableManagementAPI.class);
                Call<String> dataSaveCall = tableDataSaveApi.saveTableData(tempTableDataSave);

                dataSaveCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            // confirm new data to main data holder
                            retrieveTableDetailsObject.getTableDetails().setJsonData(saveDataJson);
                            iniDataMap();
                            Toast.makeText(TableDataManagement.this, "Status:" + response.body(), Toast.LENGTH_SHORT).show();
                        } else {
                            // rollback the dataMap with current data
                            iniDataMap();
                            populateTableLayout();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            } else {
                Toast.makeText(TableDataManagement.this, "No new data added", Toast.LENGTH_SHORT).show();
            }

            saveFlag = false;
        });
    }

    private void populateTableLayout() {
        TextView tableName = findViewById(R.id.dataTableName);
        tableName.setText(retrieveTableDetailsObject.getTemplateTables().getTableName());

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.removeAllViews();

        TableRow headerRow = new TableRow(this);

        TextView blankCell = new TextView(this);
        blankCell.setPadding(15, 15, 15, 15);
        blankCell.setBackgroundColor(Color.TRANSPARENT);
        blankCell.setText("           ");
        headerRow.addView(blankCell);

        int counter = 0;

        for (String header : dataMap.keySet()) {
            TextView headerCell = new TextView(this);
            headerCell.setText(header);
            headerCell.setTextColor(Color.BLACK);
            headerCell.setPadding(15, 15, 15, 15);
            headerCell.setTypeface(null, Typeface.BOLD);
            if (counter % 2 == 0) {
                headerCell.setBackgroundColor(Color.LTGRAY);
            } else {
                headerCell.setBackgroundColor(Color.TRANSPARENT);
            }
            headerRow.addView(headerCell);

            counter++;
        }
        tableLayout.addView(headerRow);

        // json list to be always same size
        int rowCount = dataMap.values().iterator().next().size();
        for (int i = 0; i < rowCount; i++) {
            TableRow dataRow = new TableRow(this);

            // delete button
            Button deleteRows = new Button(this);
            deleteRows.setText("Delete");
            deleteRows.setTextSize(8);

            int position = i;
            deleteRows.setOnClickListener(view -> {
                deleteRow(position);
                saveFlag = true;
            });
            dataRow.addView(deleteRows);

            for (String mapKey : dataMap.keySet()) {
                TextView dataCell = new TextView(this);
                dataCell.setText(dataMap.get(mapKey).get(i));
                dataCell.setGravity(Gravity.CENTER);
                dataCell.setPadding(15, 15, 15, 15);

                if (i % 2 == 0) {
                    dataRow.setBackgroundColor(Color.YELLOW);
                }
                dataRow.addView(dataCell);
            }
            tableLayout.addView(dataRow);
        }
    }

    private void deleteRow(int position) {
        for (String mapKey : dataMap.keySet()) {
            Objects.requireNonNull(dataMap.get(mapKey)).remove(position);
        }
        populateTableLayout();
    }


}
