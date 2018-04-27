package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // DataBase doesn't support LocalDate.MIN/MAX
    public static final LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    public static final LocalDate MAX_DATE = LocalDate.of(3000, 1, 1);

    private DateTimeUtil() {
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseLocalDate(String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    public static LocalTime parseLocalTime(String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }

    public static class DateFormatter implements Formatter<LocalDate>{

        @Override
        public LocalDate parse(String text, Locale locale) throws ParseException {
            return parseLocalDate(text);
        }

        @Override
        public String print(LocalDate object, Locale locale) {
            return object.toString();
        }
    }

    public static class TimeFormatter implements Formatter<LocalTime>{

        @Override
        public LocalTime parse(String text, Locale locale) throws ParseException {
            return parseLocalTime(text);
        }

        @Override
        public String print(LocalTime object, Locale locale) {
            return object.toString();
        }
    }

    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Format {
    }
}
