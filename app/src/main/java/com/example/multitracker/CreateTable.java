package com.example.multitracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateTable extends AppCompatActivity {

    private int numberOfColumns;
    private TextView columnsQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_table);

        columnsQuantity = findViewById(R.id.quantityText);
        numberOfColumns = Integer.parseInt(columnsQuantity.getText().toString());
        // Button listener
        columnsSetup();
        cancelButton();
    }

    private void columnsSetup(){
        Button minus = findViewById(R.id.buttonMinus);
        Button plus = findViewById(R.id.buttonPlus);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOfColumns > 0){
                    numberOfColumns -= 1;
                }
                updateColumns();
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfColumns += 1;
                updateColumns();
            }
        });
    }

    private void updateColumns(){
        String columnsAmount = String.valueOf(numberOfColumns);
        columnsQuantity.setText(columnsAmount);
        columnSetup();
    }

    private void columnSetup(){

        // update the number of input columns for user columnScrollView.tableFields.create_table_fields.xml
        // based on reading from quantity text
        LinearLayout headerLinearLayout = findViewById(R.id.tableFields);
        headerLinearLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for(int i=0; i < numberOfColumns; i++){
            View columnView = inflater.inflate(R.layout.create_table_fields, headerLinearLayout, false);

            TextView textColour = columnView.findViewById(R.id.headerTextColour);
            TextView fillColour = columnView.findViewById(R.id.headerFillColour);

            textColour.setOnClickListener(v -> {
                // colour picker
                int colourCode = colourPicker();
                //textColour.setTextColor(colourCode);
                //fillColour.setTextColor(colourCode);
            });

            fillColour.setOnClickListener(v -> {
                // colour picker
                int colourCode = colourPicker();
                //fillColour.setBackgroundColor(colourCode);
            });


            headerLinearLayout.addView(columnView);
        }

    }

    // colour picker
    private int colourPicker(){
        int test = 0;

        // color picker popup
        

        Toast.makeText(CreateTable.this, "Test Click", Toast.LENGTH_SHORT).show();
        return test;
    }

    // Save
    private void saveTable(){

        // get child linear
        // Loop
        // Validate columns
        // Ensure Name only
        // retrofit api call to backend
        //

    }

    // Cancel
    private void cancelButton(){
        Button cancel = findViewById(R.id.cancelSaveTemplate);

        cancel.setOnClickListener(v -> {
            finish();
        });
    }

}
