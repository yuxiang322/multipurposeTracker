package com.example.multitracker;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yuku.ambilwarna.AmbilWarnaDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.api.TableManagementAPI;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.HeaderDetailsDTO;
import com.example.multitracker.dto.TableCreationDTO;
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
        TextView columnsError = findViewById(R.id.columnQuanText);

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
                columnsError.setError(null);
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
        TextView columnsError = findViewById(R.id.columnQuanText);

        save.setOnClickListener(v -> {
            boolean isValid = true;

            List<HeaderDetailsDTO> headerDetailsDTOList = new ArrayList<>();

            String tableNameFlag = tableName.getText().toString().trim();
            if (tableNameFlag.isEmpty()) {
                tableName.setError("Required");
                return;
            }

            LinearLayout parent = findViewById(R.id.tableFields);

            if(parent.getChildCount() == 0){
                columnsError.setError("Required");
                return;
            }

            for (int i = 0; i < parent.getChildCount(); i++) {
                View headerColumnView = parent.getChildAt(i);

                TextView headerNameTextView = headerColumnView.findViewById(R.id.headerName);
                String headerName = headerNameTextView.getText().toString().trim();
                TextView textColour = headerColumnView.findViewById(R.id.headerTextColour);
                TextView backgroundFillColour = headerColumnView.findViewById(R.id.headerFillColour);
                CheckBox boldFlag = headerColumnView.findViewById(R.id.headerBoldCheck);

                int testRGB = textColour.getCurrentTextColor();
                String textRGB = rgbConversion(testRGB);
                int fillColour = 0;
                String fillRGB = null;

                if (headerName.isEmpty()) {
                    headerNameTextView.setError("Column name is required");
                    isValid = false;
                    break;
                }

                Drawable background = backgroundFillColour.getBackground();
                if (background instanceof ColorDrawable) {
                    ColorDrawable colorDrawable = (ColorDrawable) background;
                    fillColour = colorDrawable.getColor();
                    fillRGB = rgbConversion(fillColour);

                } else if (background instanceof GradientDrawable) {
                    GradientDrawable gradientDrawable = (GradientDrawable) background;
                    fillColour = gradientDrawable.getColor().getDefaultColor();
                    fillRGB = rgbConversion(fillColour);
                } else {
                    return;
                }

                HeaderDetailsDTO tempHeaderDetails = new HeaderDetailsDTO();
                tempHeaderDetails.setHeaderName(headerName);
                tempHeaderDetails.setHeaderTextColour(textRGB);
                tempHeaderDetails.setHeaderFillColour(fillRGB);
                tempHeaderDetails.setTextBold(boldFlag.isChecked());

                headerDetailsDTOList.add(tempHeaderDetails);
            }

            if (isValid) {
                tableCreation(headerDetailsDTOList);
                finish();
            } else {
                Toast.makeText(CreateTable.this, "Please fix the errors and try again.", Toast.LENGTH_SHORT).show();
            }
            headerDetailsDTOList.clear();
        });
    }

    // Api call
    private void tableCreation(List<HeaderDetailsDTO> headerDetailsDTOList) {

        TableCreationDTO tableCreationDTO = new TableCreationDTO();
        TemplateTablesDTO templateTable = new TemplateTablesDTO();
        EditText tableName = findViewById(R.id.tableName);

        templateTable.setTableName(tableName.getText().toString());
        templateTable.setTemplateID(templateID);

        tableCreationDTO.setHeaderDetailsList(headerDetailsDTOList);
        tableCreationDTO.setTemplateTables(templateTable);

        TableManagementAPI createTableApi = RetrofitClientInstance.getRetrofitInstance().create(TableManagementAPI.class);
        Call<String> call = createTableApi.tableCreation(tableCreationDTO);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseText = response.body();
                    Toast.makeText(CreateTable.this, responseText, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

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
        } else {
            colorRGB = "RGB(" + red + ", " + green + ", " + blue + ")";
        }

        return colorRGB;
    }

    // Cancel
    private void cancelButton() {
        Button cancel = findViewById(R.id.cancelSaveTemplate);
        cancel.setOnClickListener(v -> {
            finish();
        });
    }

}
