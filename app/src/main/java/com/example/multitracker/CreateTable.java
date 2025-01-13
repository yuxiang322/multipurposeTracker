package com.example.multitracker;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.dto.HeaderDetailsDTO;
import com.example.multitracker.dto.TableDetailsDTO;
import com.example.multitracker.dto.TemplateTablesDTO;

import java.util.ArrayList;
import java.util.List;

public class CreateTable extends AppCompatActivity {

    private int numberOfColumns;
    private TextView columnsQuantity;
    private int templateID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_table);

        templateID = getIntent().getIntExtra("TEMPLATE_ID", -1);
        Toast.makeText(this, "Template ID: " + templateID, Toast.LENGTH_SHORT).show();

        columnsQuantity = findViewById(R.id.quantityText);
        numberOfColumns = Integer.parseInt(columnsQuantity.getText().toString());
        // Button listener
        columnsSetup();
        cancelButton();
        addTable();
    }

    private void columnsSetup() {
        Button minus = findViewById(R.id.buttonMinus);
        Button plus = findViewById(R.id.buttonPlus);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOfColumns > 0) {
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

    private void updateColumns() {
        String columnsAmount = String.valueOf(numberOfColumns);
        columnsQuantity.setText(columnsAmount);
        columnSetup();
    }

    private void columnSetup() {

        // update the number of input columns for user columnScrollView.tableFields.create_table_fields.xml
        // based on reading from quantity text
        LinearLayout headerLinearLayout = findViewById(R.id.tableFields);
        headerLinearLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < numberOfColumns; i++) {
            View columnView = inflater.inflate(R.layout.create_table_fields, headerLinearLayout, false);

            TextView textColour = columnView.findViewById(R.id.headerTextColour);
            TextView fillColour = columnView.findViewById(R.id.headerFillColour);

            textColour.setOnClickListener(v -> {
                new AmbilWarnaDialog(this, Color.BLACK, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int colorSelected) {
                        textColour.setTextColor(colorSelected);
                        fillColour.setTextColor(colorSelected);
                        Toast.makeText(CreateTable.this, "Color Selected: #" + Integer.toHexString(colorSelected), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                    }
                }).show();
            });
            fillColour.setOnClickListener(v -> {
                new AmbilWarnaDialog(this, Color.WHITE, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int colorSelected) {
                        fillColour.setBackgroundColor(colorSelected);
                        Toast.makeText(CreateTable.this, "Color Selected: #" + Integer.toHexString(colorSelected), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                    }
                }).show();
            });
            headerLinearLayout.addView(columnView);
        }
    }

    // Save
    private void addTable() {

        Button save = findViewById(R.id.addTables);
        EditText tableName = findViewById(R.id.tableName);

        save.setOnClickListener(v -> {
            boolean isValid = true;

            List<TemplateTablesDTO> templateTablesDTOList = new ArrayList<>();
            List<HeaderDetailsDTO> headerDetailsDTOList = new ArrayList<>();
            List<TableDetailsDTO> tableDetailsDTOList = new ArrayList<>();

            String tableNameFlag = tableName.getText().toString().trim();
            if (tableNameFlag.isEmpty()) {
                tableName.setError("Required");
                return;
            }

            LinearLayout parent = findViewById(R.id.tableFields);

            for (int i = 0; i < parent.getChildCount(); i++) {
                View headerColumnView = parent.getChildAt(i);

                TextView headerNameTextView = headerColumnView.findViewById(R.id.headerName);
                String columnName = headerNameTextView.getText().toString().trim();

                if (columnName.isEmpty()) {
                    headerNameTextView.setError("Column name is required");
                    isValid = false;
                    break;
                }

                TextView textColour = findViewById(R.id.headerTextColour);
                TextView testFilColour = findViewById(R.id.headerFillColour);

                int testRGB = textColour.getCurrentTextColor();
                String textRGB = rgbConversion(testRGB);

                int fillColour = 0;
                String fillRGB = null;
                Drawable background = testFilColour.getBackground();
                if (background instanceof ColorDrawable) {
                    ColorDrawable colorDrawable = (ColorDrawable) background;
                    fillColour = colorDrawable.getColor();
                    fillRGB = rgbConversion(fillColour);

                } else if (background instanceof GradientDrawable) {
                    GradientDrawable gradientDrawable = (GradientDrawable) background;
                    fillColour = gradientDrawable.getColor().getDefaultColor();
                    fillRGB = rgbConversion(fillColour);
                } else{
                    return;
                }

                // create object to arraylist
            }

            if (isValid) {
                // Save Template_tables()
                // Save Header_Details()
                // Save Table_Details()
            } else {
                // Clear Template_tables
                // Clear Header_Details
                // Clear Table_Details
                Toast.makeText(CreateTable.this, "Please fix the errors and try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // RGB conversion
    private String rgbConversion(int rgbInteger) {
        String colorRGB = null;
        int red = (rgbInteger >> 16) & 0xFF;
        int green = (rgbInteger >> 8) & 0xFF;
        int blue = rgbInteger & 0xFF;

        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be between 0 and 255.");
        } else{
            colorRGB = "RGB(" + red + ", " + green + ", " + blue + ")";
        }

        return colorRGB;
    }
    // Save Template_tables
    // Save Header_Details
    // Save Table_Details


    // Cancel
    private void cancelButton() {
        Button cancel = findViewById(R.id.cancelSaveTemplate);
        cancel.setOnClickListener(v -> {
            finish();
        });
    }

}
