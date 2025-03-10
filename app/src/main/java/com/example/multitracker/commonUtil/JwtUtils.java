package com.example.multitracker.commonUtil;

import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.util.Date;


public class JwtUtils {
    public static String extractUserUID(String jwtToken) {
        try {
            String[] tokenParts = jwtToken.split("\\.");

            if (tokenParts.length != 3) return null;

            String payload = new String(Base64.decode(tokenParts[1], Base64.DEFAULT));
            Log.d("JWTtest", "====Token string: " + payload);

            JSONObject jsonObject = new JSONObject(payload);

            return jsonObject.optString("sub");
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isTokenExpired(String jwtContent){
        try{
            String[] tokenParts = jwtContent.split("\\.");
            if (tokenParts.length != 3) return true;

            String payload = new String(Base64.decode(tokenParts[1], Base64.DEFAULT));

            JSONObject jsonObject = new JSONObject(payload);
            String expiryDate = jsonObject.optString("exp");

            long expiryTimestamp = Long.parseLong(expiryDate) * 1000;

            Date expiredDate = new Date(expiryTimestamp);
            Log.d("JWTtest", "JWT expiry date extraction from sharepreference : " + expiredDate);

            if(expiredDate.getTime() > System.currentTimeMillis()){
                return false;
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
    }

}
