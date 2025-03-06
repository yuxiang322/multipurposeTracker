package com.example.multitracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitracker.api.TableManagementAPI;
import com.example.multitracker.api.TemplatesAPI;
import com.example.multitracker.commonUtil.GlobalConstant;
import com.example.multitracker.commonUtil.MenuUtil;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.NotificationDTO;
import com.example.multitracker.dto.RetrieveTableDetailsDTO;
import com.example.multitracker.dto.TemplateDTO;
import com.example.multitracker.dto.UserUIDRequestDTO;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomePage extends AppCompatActivity {
    private List<TemplateDTO> allTemplates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);


        // Check for login(only if from NotificationBroadCastReceive)
        // intent pass TemplateID
        // get child linearlayout = tempalte id
        // perform click or handle error


        // Set up menu
        menuSetup();
        // Set up buttons
        buttonSetup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addTemplates();
        searchTemplate();
    }

    private void menuSetup() {
        ImageButton menuButton = findViewById(R.id.menuButton);
        MenuUtil.setupMenuButton(this, R.id.menuButton, R.menu.homepage_menu, new MenuUtil.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.action_notification) {
                    Toast.makeText(getApplicationContext(), "Notifications Management selected", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomePage.this, ManagementPage.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.action_signout) {
                    // Sign out from Firebase
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();
                    // clear constant
                    GlobalConstant.clearUserId();

                    Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomePage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void buttonSetup() {

        Button accountButton = findViewById(R.id.account);
        Button importButton = findViewById(R.id.templateImport);
        Button addTemplateButton = findViewById(R.id.addTemplate);

        // account
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, AccountDetails.class);
                startActivity(intent);
            }
        });
        // import/share
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, ImportTemplate.class);
                startActivity(intent);
            }
        });
        // add template
        addTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, CreateTemplate.class);
                startActivity(intent);
            }
        });
    }

    // Setup search functionality
    private void searchTemplate() {
        EditText searchTemplate = findViewById(R.id.searchTemplate);
        searchTemplate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTemplates(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filterTemplates(String keyword) {
        List<TemplateDTO> filteredTemplates = new ArrayList<>();

        for (TemplateDTO template : allTemplates) {
            if (template.getTemplateName().toLowerCase().contains(keyword.toLowerCase()) ||
                    template.getTemplateDescription().toLowerCase().contains(keyword.toLowerCase())) {
                filteredTemplates.add(template);
            }
        }
        populateScrollViewWithTemplates(filteredTemplates);
    }

    private void addTemplates() {
        TemplatesAPI templateAPI = RetrofitClientInstance.getRetrofitInstance().create(TemplatesAPI.class);
        String userUID = GlobalConstant.userID;
        UserUIDRequestDTO requestDTO = new UserUIDRequestDTO(userUID);
        Call<List<TemplateDTO>> call = templateAPI.getTemplates(requestDTO);
        call.enqueue(new Callback<List<TemplateDTO>>() {
            @Override
            public void onResponse(Call<List<TemplateDTO>> call, Response<List<TemplateDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allTemplates = response.body();
                    populateScrollViewWithTemplates(allTemplates);
                } else {
                    Toast.makeText(HomePage.this, "Failed to retrieve templates", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TemplateDTO>> call, Throwable t) {
                // Handle network failure
                Toast.makeText(HomePage.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateScrollViewWithTemplates(List<TemplateDTO> templates) {
        LinearLayout scrollViewLinearLayout = findViewById(R.id.templateLinearLayout);
        // Clear
        scrollViewLinearLayout.removeAllViews();
        // Inflate the template layout for each
        LayoutInflater inflater = LayoutInflater.from(this);

        for (TemplateDTO template : templates) {
            View templateView = inflater.inflate(R.layout.homepage_template_container, scrollViewLinearLayout, false);
            // Set margins for each view
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(16, 16, 16, 16);  // Adjust margins as needed
            templateView.setLayoutParams(layoutParams);

            TextView templateName = templateView.findViewById(R.id.templateTitle);
            TextView templateDescription = templateView.findViewById(R.id.templateDescription);
            Button deleteButton = templateView.findViewById(R.id.deleteButton);
            Button viewDetails = templateView.findViewById(R.id.viewDetailsButton);
            ImageButton shareButton = templateView.findViewById(R.id.templateShareButton);

            templateName.setText(template.getTemplateName());
            templateDescription.setText(template.getTemplateDescription());

            deleteButton.setOnClickListener(v -> {
                deleteTemplate(template);
            });

            viewDetails.setOnClickListener(v -> {
                Intent intent = new Intent(this, TemplateDetails.class);
                intent.putExtra("TEMPLATE_KEY", template);
                startActivity(intent);
            });

            shareButton.setOnClickListener(v -> {
                shareTemplate(template);
            });
            scrollViewLinearLayout.addView(templateView);
        }
    }


    // ADD delete sp
    private void deleteTemplate(TemplateDTO templateDTODeleteRequest) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Template")
                .setMessage("Are you sure you want to delete this template?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    TemplatesAPI apiService = RetrofitClientInstance.getRetrofitInstance().create(TemplatesAPI.class);
                    Call<NotificationDTO> call = apiService.deleteTemplate(templateDTODeleteRequest);
                    call.enqueue(new Callback<NotificationDTO>() {
                        @Override
                        public void onResponse(Call<NotificationDTO> call, Response<NotificationDTO> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                // Handle success message
                                Toast.makeText(HomePage.this,"Template Deleted", Toast.LENGTH_SHORT).show();
                                addTemplates();

                                NotificationDTO notificationDelete = response.body();
                                // delete
                                NotificationAlarmManager notificationAlarmManager = new NotificationAlarmManager(getApplicationContext());
                                notificationAlarmManager.deleteAlarmManager(notificationDelete);

                            } else {
                                Toast.makeText(HomePage.this, "Failed to delete template", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationDTO> call, Throwable t) {
                            Toast.makeText(HomePage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void shareTemplate(TemplateDTO templateShared) {
        TableManagementAPI tableApi = RetrofitClientInstance.getRetrofitInstance().create(TableManagementAPI.class);
        Call<List<RetrieveTableDetailsDTO>> call = tableApi.retrieveTable(templateShared);
        call.enqueue(new Callback<List<RetrieveTableDetailsDTO>>() {
            @Override
            public void onResponse(Call<List<RetrieveTableDetailsDTO>> call, Response<List<RetrieveTableDetailsDTO>> response) {
                if (response.isSuccessful()) {
                    List<RetrieveTableDetailsDTO> responseBody = response.body();
                    if (responseBody != null && !responseBody.isEmpty()) {
                        TemplateShare dialogFrag = new TemplateShare();

                        Bundle args = new Bundle();
                        args.putParcelable("shareTemplate", templateShared);
                        dialogFrag.setArguments(args);

                        dialogFrag.show(getSupportFragmentManager(), null);
                    } else {
                        Toast.makeText(HomePage.this, "Make sure template contain at least 1 Table.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RetrieveTableDetailsDTO>> call, Throwable t) {
            }
        });
    }
}
