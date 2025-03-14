package com.example.multitracker.commonUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.util.HashMap;
import java.util.Map;

public class EncryptedSharePreferenceUtils {

    public EncryptedSharePreferenceUtils() {
    }

    public static void storeEncryptedSharePreference(Context context, String fileName, String key, String contentValue) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    fileName,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, contentValue);
            editor.apply();
            Log.d("JWTtest", "encrpyted Sp saved:  " + key);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String,?> getEncryptedSharePreference(Context context, String fileName, String key){
        try{
            Log.d("JWTtest", "======\nGetting sharepreference encrypted");

            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    fileName,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            if(key == null || key.isEmpty()){
                Log.d("JwtLoginTest", "Return .getall");
                return sharedPreferences.getAll();
            }else{
                String spValue = sharedPreferences.getString(key, null);

                if (spValue != null) {
                    Log.d("JWTtest", "Get specific sp Key: " + key + ", Value: " + spValue);
                    Map<String, Object> result = new HashMap<>();
                    result.put(key, spValue);
                    return result;
                } else {
                    return null;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteEncryptedSharePreference(Context context, String fileName, String key){
        try{
            Log.d("JWTtest", "======\nDelete sharepreference encrypted");

            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    fileName,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            SharedPreferences.Editor editor = sharedPreferences.edit();

            if(key == null || key.isEmpty()){ // sign out
                editor.clear(); // remove all kayvaluepari
            }else {
                editor.remove(key); // other files that might need
            }

            editor.apply();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
