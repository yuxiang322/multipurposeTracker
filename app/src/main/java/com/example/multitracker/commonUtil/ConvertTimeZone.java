package com.example.multitracker.commonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class ConvertTimeZone {
    public static LocalDateTime convertToUTC(LocalDate localDate, LocalTime localTime) {
        if (localDate != null && localTime != null) {
            LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

            ZonedDateTime localZonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
            ZonedDateTime utcZonedDateTime = localZonedDateTime.withZoneSameInstant(ZoneOffset.UTC);

            return utcZonedDateTime.toLocalDateTime();
        }
        return null;
    }

    public static ZonedDateTime convertToLocalTimeZone(String localDateString, String localTimeString){

        if(localDateString != null && localTimeString != null){
            LocalDate localDate = LocalDate.parse(localDateString);
            LocalTime localTime = LocalTime.parse(localTimeString);
            LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

            ZonedDateTime utcZoneDateTime = localDateTime.atZone(ZoneOffset.UTC);

            return utcZoneDateTime.withZoneSameInstant(ZoneId.systemDefault());
        }
        return null;
    }
}
