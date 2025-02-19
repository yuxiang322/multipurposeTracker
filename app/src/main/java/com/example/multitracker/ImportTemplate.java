package com.example.multitracker;

import static com.example.multitracker.commonUtil.GlobalConstant.userID;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.api.ImportTemplateAPI;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.ImportTemplateDTO;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportTemplate extends AppCompatActivity {

    private EditText shareCode;
    private Button importButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_template);

        shareCode = findViewById(R.id.importShareCode);
        importButton = findViewById(R.id.importTemplate);

        importButtonSetup();
    }

    private void importButtonSetup() {
        importButton.setOnClickListener(v -> {
            String shareCodeToImport = shareCode.getText().toString().trim();

            if(!shareCodeToImport.isEmpty()){
                importTemplateRequest(shareCodeToImport);
            }else{
                Toast.makeText(ImportTemplate.this, "Please enter a share code", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void importTemplateRequest(String shareCodeImport) {
        ImportTemplateDTO importTemplateDetails = new ImportTemplateDTO();
        importTemplateDetails.setShareCode(shareCodeImport);
        importTemplateDetails.setUserUID(userID);
        Log.d("ImportTemplate", "UserID????" + userID);

        ImportTemplateAPI importApi = RetrofitClientInstance.getRetrofitInstance().create(ImportTemplateAPI.class);
        Call<String> request = importApi.importTemplate(importTemplateDetails);

        Log.d("ImportTemplate", "Request body: " + new Gson().toJson(importTemplateDetails));
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    if(response.body().equals("Invalid Code")){
                        Toast.makeText(ImportTemplate.this,"Invalid sharecode. Enter valid sharecode.", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ImportTemplate.this,"Templated imported.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(ImportTemplate.this,"Import Failure. Error: " + response.body(), Toast.LENGTH_LONG).show();
                }
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
