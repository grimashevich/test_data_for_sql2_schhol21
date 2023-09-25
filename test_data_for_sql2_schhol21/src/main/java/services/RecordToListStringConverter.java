package services;

import models.AbstractRecord;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RecordToListStringConverter {
    public static<T extends AbstractRecord> List<String> covert(T record) throws IllegalAccessException {
        Class<?> clazz = record.getClass();
        Field[] clazzFields = clazz.getDeclaredFields();
        List<String> result = new ArrayList<>(clazzFields.length);

        for (Field field: clazzFields) {
            String value;
            field.setAccessible(true);

            if (field.getType() == int.class || field.getType() == long.class || field.getType() == String.class) {
                value = field.get(record).toString();
            } else if (field.getType() == LocalDate.class) {
                LocalDate date = (LocalDate) field.get(record);
                value = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            } else if ((field.getType()) == LocalTime.class) {
                LocalTime time = (LocalTime) field.get(record);
                value = time.format(DateTimeFormatter.ISO_LOCAL_TIME);
            } else {
                throw new RuntimeException("Unknown field type");
            }
            result.add(value);
        }
        return result;
    }
}
