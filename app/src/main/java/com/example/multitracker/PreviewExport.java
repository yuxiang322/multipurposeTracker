package com.example.multitracker;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageButton;
import android.os.Environment;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.multitracker.commonUtil.ColorUtil;
import com.example.multitracker.dto.HeaderDetailsDTO;
import com.example.multitracker.dto.RetrieveTableDetailsDTO;
import com.example.multitracker.dto.TemplateDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PreviewExport extends AppCompatActivity {

    private List<RetrieveTableDetailsDTO> templateFullDetails = new ArrayList<>();
    TemplateDTO templateInfo;
    private String excelFileName;
    private File excelFileObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_export);

        setupIntent();
        // preview xlsx
        GeneratePreviewData();
        // Export, Preview button setup
        setupPreviewExportXlsx();

        // back button
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
                setEnabled(true);
                // delete file
                if (excelFileObj.exists()) {
                    boolean deleted = excelFileObj.delete();
                    Log.d("FileDeletion", "Excel file deleted: " + deleted);
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private void setupIntent() {
        Log.d("previewOfData", "Size of TableDetails BEFORE INTENT ADD: " + templateFullDetails.size());
        Intent getIntent = getIntent();
        List<RetrieveTableDetailsDTO> previewData = getIntent.getParcelableArrayListExtra("previewData");
        templateInfo = getIntent.getParcelableExtra("previewTemplateData");
        excelFileName = (templateInfo != null ? templateInfo.getTemplateName() : null) + "_" + (templateInfo != null ? templateInfo.getTemplateID() : 0) + ".xlsx";

        if (previewData != null) {
            Log.d("previewOfData", "Size of PREVIEWDATA: " + previewData.size());
            templateFullDetails.addAll(previewData);
            Log.d("previewOfData", "TableData PREVIEWDATA: " + previewData.get(3).getTableDetails().getJsonData());
        }
    }

    private void setupPreviewExportXlsx() {
        ImageButton previewButton = findViewById(R.id.previewExcelButton);
        ImageButton downloadButton = findViewById(R.id.downloadExcelButton);

        previewButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", excelFileObj);
            intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Open with"));
        });

        downloadButton.setOnClickListener(v -> {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, excelFileName);
            contentValues.put(MediaStore.Downloads.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            contentValues.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
            if (uri != null) {
                try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {

                    FileInputStream fileInputStream = new FileInputStream(excelFileObj);

                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }

                    fileInputStream.close();
                    outputStream.close();
                    Toast.makeText(this, "File saved to Download", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to save file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void GeneratePreviewData() {
        // generate xlsx --> tempalteFullDetails
        // save private internal storage
        // preview intent for excel app
        try (Workbook workbook = new XSSFWorkbook()) {
            // Table name
            XSSFFont fontTableName = (XSSFFont) workbook.createFont();
            fontTableName.setFontHeightInPoints((short) 20);
            fontTableName.setBold(true);
            XSSFCellStyle styleTableName = (XSSFCellStyle) workbook.createCellStyle();
            styleTableName.setFont(fontTableName);

            Log.d("previewOfData", "/nStart processing templateFullDetails...");
            Log.d("previewOfData", "Size of TableDetails: " + templateFullDetails.size());
            for (RetrieveTableDetailsDTO tableDetails : templateFullDetails) {
                int rowCounter = 0;
                int countHeaderDetails = tableDetails.getHeaderDetailsList().size();

                Log.d("previewOfData", "Processing table: " + tableDetails.getTemplateTables().getTableName());

                String sheetName = tableDetails.getTemplateTables().getTableName();

                int counter = 1;
                while (workbook.getSheet(sheetName) != null) {
                    sheetName = tableDetails.getTemplateTables().getTableName() + "_" + counter++;
                }

                Log.d("previewOfData", "Creating sheet with name: " + sheetName + "Counter: " + counter);

                Sheet sheet = workbook.createSheet(sheetName);

                // 1st row Table Name
                Row tableRow = sheet.createRow(rowCounter);
                Cell tableNameCell = tableRow.createCell(rowCounter);
                tableNameCell.setCellValue(tableDetails.getTemplateTables().getTableName());
                tableNameCell.setCellStyle(styleTableName);
                if (countHeaderDetails > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, countHeaderDetails - 1));
                }
                rowCounter++;

                // 2nd row and column header details
                Row headerRow = sheet.createRow(rowCounter);
                List<HeaderDetailsDTO> headerDetails = new ArrayList<>(tableDetails.getHeaderDetailsList());

                for (int i = 0; i < countHeaderDetails; i++) {

                    String headerName = headerDetails.get(i).getHeaderName();
                    // font
                    XSSFFont fontHeaderDetails = (XSSFFont) workbook.createFont();
                    fontHeaderDetails.setFontHeightInPoints((short) 14);
                    // bold
                    fontHeaderDetails.setBold(headerDetails.get(i).getTextBold());
                    // Text Colour
                    String textRGBString = headerDetails.get(i).getHeaderTextColour();
                    int[] textRGB = ColorUtil.extractRGB(textRGBString);
                    XSSFColor textColor = ColorUtil.getXSSFColor(textRGB);
                    fontHeaderDetails.setColor(textColor);

                    // Cell Style
                    XSSFCellStyle styleHeaderDetails = (XSSFCellStyle) workbook.createCellStyle();
                    styleHeaderDetails.setFont(fontHeaderDetails);
                    // Fill colour
                    String fillRGBString = headerDetails.get(i).getHeaderFillColour();
                    int[] fillRGB = ColorUtil.extractRGB(fillRGBString);
                    XSSFColor fillColor = ColorUtil.getXSSFColor(fillRGB);
                    styleHeaderDetails.setFillForegroundColor(fillColor);
                    styleHeaderDetails.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                    Cell headerCell = headerRow.createCell(i);
                    headerCell.setCellValue(headerName);
                    headerCell.setCellStyle(styleHeaderDetails);
                }

                // 3 row on jsondata
                String jsonData = tableDetails.getTableDetails().getJsonData();
                ObjectMapper mapper = new ObjectMapper();
                try {
                    Map<String, List<String>> jsonDataMap = mapper.readValue(jsonData, new TypeReference<LinkedHashMap<String, List<String>>>() {
                    });
                    Log.d("MapJson", "success?? " + mapper.writeValueAsString(jsonDataMap));

                    int rowSize = jsonDataMap.values().stream()
                            .mapToInt(List::size)
                            .max()
                            .orElse(0);

                    if (rowSize > 0) {
                        for (int i = 0; i < rowSize; i++) {
                            int cellCounter = 0;
                            Row dataRow = sheet.createRow(rowCounter++);

                            for (Map.Entry<String, List<String>> entry : jsonDataMap.entrySet()) {
                                List<String> value = entry.getValue();

                                String columnData = value.get(i);
                                if (columnData == null) {
                                    columnData = "null";
                                }
                                Cell dataCell = dataRow.createCell(cellCounter++);
                                dataCell.setCellValue(columnData);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // ini file
            FileOutputStream fileOutputStream;
            File file = new File(getFilesDir(), excelFileName);
            excelFileObj = file;
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
