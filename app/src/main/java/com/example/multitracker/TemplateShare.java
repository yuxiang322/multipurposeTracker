package com.example.multitracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.multitracker.api.ShareTableAPI;
import com.example.multitracker.commonUtil.RetrofitClientInstance;
import com.example.multitracker.dto.ShareTableDTO;
import com.example.multitracker.dto.TemplateDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemplateShare extends DialogFragment {

    private TemplateDTO templateShared;
    private ShareTableDTO sharedInfo;
    private DialogFragment dialogFragment;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate custom view
        View customDialogView = getActivity().getLayoutInflater().inflate(R.layout.template_share_popup, null);
        templateShared = getArguments().getParcelable("shareTemplate");

        TextView shareCode = customDialogView.findViewById(R.id.shareCodeText);

        shareCode.setText("Loading...");

        if (getArguments() != null) {
            templateShared = (TemplateDTO) getArguments().getParcelable("shareTemplate");

            shareInfoAPI(shareCode);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(customDialogView)
                .setPositiveButton("Copy", (dialog, id) -> {
                    // Copy text
                    String shareCodeText = shareCode.getText().toString();
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("shareCode", shareCodeText);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), "Share code copied to clipboard", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Back", (dialog, id) -> {
                    if (dialogFragment != null) {
                        dialogFragment.dismiss();
                    }
                });

        dialogFragment = this;
        return builder.create();
    }

    // API call
    private void shareInfoAPI(final TextView shareCode) {
        ShareTableAPI shareTableAPI = RetrofitClientInstance.getRetrofitInstance().create(ShareTableAPI.class);
        Call<ShareTableDTO> call = shareTableAPI.getShareInfo(templateShared);

        call.enqueue(new Callback<ShareTableDTO>() {
            @Override
            public void onResponse(Call<ShareTableDTO> call, Response<ShareTableDTO> response) {
                if (response.isSuccessful()) {
                    sharedInfo = response.body();
                    Toast.makeText(getActivity(), "Response Success", Toast.LENGTH_SHORT).show();

                    if (sharedInfo != null) {
                        shareCode.setText(sharedInfo.getSharingCode());
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to get share info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShareTableDTO> call, Throwable t) {
            }
        });
    }
}
