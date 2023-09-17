package services;

import models.AbstractRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ImportFromCsv {
    public static <T extends AbstractRecord> List<T> importFromFile(String csvFilename, Class<T> clazz)
            throws Exception {
        List<T> recordsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(getResourceFilePath(csvFilename)))) {

            String line;
            String[] values;
            T record;
            reader.readLine(); // Пропускаем заголовок

            while ((line = reader.readLine()) != null) {
                values = line.split(",");
                record = clazz.getDeclaredConstructor().newInstance();

                for (int i = 0; i < values.length; i++) {
                    Field field = clazz.getDeclaredFields()[i];
                    field.setAccessible(true);

                    if (field.getType() == int.class) {
                        field.set(record, Integer.parseInt(values[i]));
                    } else if (field.getType() == long.class) {
                        field.set(record, Long.parseLong(values[i]));
                    } else if (field.getType() == String.class) {
                        field.set(record, values[i]);
                    } else if (field.getType() == LocalDate.class) {
                        field.set(record, LocalDate.parse(values[i]));
                    } else if (field.getType() == LocalTime.class) {
                        field.set(record, LocalTime.parse(values[i]));
                    }
                    else {
                        throw new RuntimeException("Unexpected field type " + field.getType().getName()  +
                                " in class " + clazz.getName());
                    }
                }
                recordsList.add(record);
            }
        }
        return recordsList;
    }

    public static String getResourceFilePath(String resourceName) {
        ClassLoader classLoader = ImportFromCsv.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceName);

        if (resourceUrl != null) {
            try {
                return Paths.get(resourceUrl.toURI()).toAbsolutePath().toString();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }
    }
}
