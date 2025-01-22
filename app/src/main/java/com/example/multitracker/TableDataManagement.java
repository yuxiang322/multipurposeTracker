package com.example.multitracker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.dto.RetrieveTableDetailsDTO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TableDataManagement extends AppCompatActivity {
    private RetrieveTableDetailsDTO retrieveTableDetailsObject;
    private Map<String, List<String>> dataMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_data_management);

        Intent getTableData = getIntent();
        retrieveTableDetailsObject = getTableData.getParcelableExtra("tableData");

        //test
        //String jsonData = "{\"Header1\":[\"a1\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\"],\"Header2\":[\"a1\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\"],\"Header3\":[\"a1\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\"],\"Header4\":[\"a1\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\"],\"Header5\":[\"a1\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\"],\"Header6\":[\"a1\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\"],\"Header7\":[\"a1\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\",\"a2\",\"a3\"]}";

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<String>>>() {
        }.getType();
        dataMap = gson.fromJson(retrieveTableDetailsObject.getTableDetails().getJsonData(), type);
        //headerDataMap = gson.fromJson(jsonData, type); //test

        populateTableLayout();
        // Add data

        // Save data
    }

    // add data
    private void addData(){
        // add button listner
        // new popup for entry
        // save/cancel changes
        // add to dataMap
    }

    // save data
    private void saveData(){
        // save button listner
        // alert dialog confirm
        // dataMap to json string
        // api call to update jsonData
    }

    private void populateTableLayout() {
        TextView tableName = findViewById(R.id.dataTableName);
        tableName.setText(retrieveTableDetailsObject.getTemplateTables().getTableName());

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.removeAllViews();

        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(Color.LTGRAY);
        TextView blankCell = new TextView(this);
        blankCell.setText("           ");
        headerRow.addView(blankCell);

        for (String header : dataMap.keySet()) {
            TextView headerCell = new TextView(this);
            headerCell.setText(header);
            headerCell.setPadding(15, 15, 15, 15);
            headerCell.setTypeface(null, Typeface.BOLD);
            headerCell.setBackgroundColor(Color.LTGRAY);
            headerRow.addView(headerCell);
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
