package com.example.multitracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.dto.HeaderDetailsDTO;
import com.example.multitracker.dto.RetrieveTableDetailsDTO;
import com.example.multitracker.dto.TableDetailsDTO;

public class TableDataManagement extends AppCompatActivity {

    private RetrieveTableDetailsDTO retrieveTableDetailsObject;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_data_management);

        Intent getTableData = getIntent();
        retrieveTableDetailsObject = getTableData.getParcelableExtra("tableData");

        // populate
        // data entry
        // save
        // removing data

        // Recycler view
        // adapter to save data
        // jsondata to a Hashmap -> header key, values list<String>


    }




}
