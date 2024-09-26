package com.example.multitracker.commonUtil;

import android.app.Activity;
import android.content.Context;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class MenuUtil {

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem item);
    }

    public static void setupMenuButton(final Activity activity, int buttonId, final int menuResId, final OnMenuItemClickListener listener) {
        ImageButton menuButton = activity.findViewById(buttonId);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(activity, view, menuResId, listener);
            }
        });
    }

    private static void showPopupMenu(final Activity activity, View view, int menuResId, final OnMenuItemClickListener listener) {
        PopupMenu popup = new PopupMenu(activity, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(menuResId, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return listener.onMenuItemClick(item);
            }
        });
        popup.show();
    }
}
