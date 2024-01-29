package com.starleken.authorizationserver.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public abstract class DateUtils {

    public static Date getCurrentDateWithAdditionalMinutes(int additionalMinutes){
        LocalDateTime now = LocalDateTime.now();
        Instant time = now.plusMinutes(additionalMinutes).atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(time);
    }

    public static Date getCurrentDateWithAdditionalDays(int additionalDays){
        LocalDateTime now = LocalDateTime.now();
        Instant time = now.plusDays(additionalDays).atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(time);
    }
}
