package com.example.multitracker.commonUtil;

public class GlobalConstant {
    public static String userID;

    public final static String jwtSPLoginFileName = "Jwt_Token";

    public static void clearUserId() {
        userID = null;
    }
}
