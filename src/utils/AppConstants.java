package utils;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public final class AppConstants {

    public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyy");
    public final static LocalDateTime DEFAULT_DATE_TO_EPIC_START = LocalDateTime.of(3000, Month.JANUARY, 1, 0, 0);
    public final static LocalDateTime DEFAULT_DATE_TO_EPIC_END = LocalDateTime.of(1000, Month.JANUARY, 1, 0, 0);

}
