package com.example.multitracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.dto.RetrieveTableDetailsDTO;

import java.util.ArrayList;
import java.util.List;

public class PreviewExport  extends AppCompatActivity {

    private List<RetrieveTableDetailsDTO> templateFullDetails = new ArrayList<>();
    private final String csvFileName = "templateDetails.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_export);

        setupIntent();

        // preview csv

        // export button setup

        // back button
    }

    private void setupIntent(){
        Intent getIntent = getIntent();
        List<RetrieveTableDetailsDTO> previewData = getIntent.getParcelableArrayListExtra("previewData");

        if (previewData != null) {
            templateFullDetails.addAll(previewData);
        }
    }

    private void previewOfData(){
        // generate CSV --> tempalteFullDetails
        // save private internal storage
        // preview
    }
    private void setupExportCsv(){
        // api call backend
        // Send CSV
        // multipartfile + Useruid?

        // backend CSV -> get user email -> csv
        // success --> delete CSV from internal storage
    }

    private void setUpBackButton(){
        // on click
        // delete CSV
        // back to preview activity
    }


}
