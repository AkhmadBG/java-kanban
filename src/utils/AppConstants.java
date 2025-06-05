package utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public final class AppConstants {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
    public static final LocalDateTime DEFAULT_DATE_TO_EPIC_START = LocalDateTime.of(3000, Month.JANUARY, 1, 0, 0);
    public static final LocalDateTime DEFAULT_DATE_TO_EPIC_END = LocalDateTime.of(1000, Month.JANUARY, 1, 0, 0);
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final int PORT = 8080;

}
