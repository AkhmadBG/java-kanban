package utils;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public final class AppConstants {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyy");
    public static final LocalDateTime DEFAULT_DATE_TO_EPIC_START = LocalDateTime.of(3000, Month.JANUARY, 1, 0, 0);
    public static final LocalDateTime DEFAULT_DATE_TO_EPIC_END = LocalDateTime.of(1000, Month.JANUARY, 1, 0, 0);

}
