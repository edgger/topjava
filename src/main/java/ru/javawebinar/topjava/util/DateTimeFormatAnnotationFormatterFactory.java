package ru.javawebinar.topjava.util;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DateTimeFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<DateTimeUtil.Format> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Arrays.asList(LocalDate.class,LocalTime.class));
    }

    @Override
    public Printer<?> getPrinter(DateTimeUtil.Format annotation, Class<?> fieldType) {
        return getAddressFormatter(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(DateTimeUtil.Format annotation, Class<?> fieldType) {
        return getAddressFormatter(annotation, fieldType);
    }

    private Formatter getAddressFormatter (DateTimeUtil.Format annotation,
                                           Class<?> fieldType) {
        if (fieldType==LocalTime.class){
            return new DateTimeUtil.TimeFormatter();
        } else {
            return new DateTimeUtil.DateFormatter();
        }
    }
}
