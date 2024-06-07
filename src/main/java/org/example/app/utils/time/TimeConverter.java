package org.example.app.utils.time;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeConverter {

    public static String millisToDateTime(Long createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        return formatter.format(Instant.ofEpochMilli(createdAt));
    }
}
