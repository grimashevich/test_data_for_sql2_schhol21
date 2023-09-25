package models;

import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RecordStringList {
    private final Class<? extends AbstractRecord> clazz;
    private final String tableName;
    private final List<String> fieldNameList;
    private final List<List<String>> values;

    public RecordStringList(Class<? extends AbstractRecord> clazz, String tableName) {
        this.clazz = clazz;
        this.tableName = tableName;
        fieldNameList = Arrays
                .stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        values = new LinkedList<>();
    }

    public void addValues(List<String> record) {
        values.add(record);
    }
}
