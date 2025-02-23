package com.example.multitracker.commonUtil;

import org.apache.poi.xssf.usermodel.XSSFColor;

public class ColorUtil {
    public static XSSFColor getXSSFColor(int[] rgb) {
        return new XSSFColor(new byte[]{
                (byte) rgb[0], (byte) rgb[1], (byte) rgb[2]
        });
    }

    public static int[] extractRGB(String rgbString) {
        rgbString = rgbString.replace("RGB(", "").replace(")", "");
        String[] rgbParts = rgbString.split(", ");
        int[] rgb = new int[3];
        rgb[0] = Integer.parseInt(rgbParts[0]);
        rgb[1] = Integer.parseInt(rgbParts[1]);
        rgb[2] = Integer.parseInt(rgbParts[2]);
        return rgb;
    }
}
