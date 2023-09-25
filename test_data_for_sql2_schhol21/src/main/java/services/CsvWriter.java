package services;

import models.RecordStringList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CsvWriter {
    public static void writeCsv(List<RecordStringList> list, String dirName) throws IOException {
        Path dirPath = Paths.get(dirName);
        if (Files.notExists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        for (RecordStringList rsl: list) {
            String filename = dirPath + "/" +  rsl.getTableName() + ".csv";
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
                bw.write(getSqlInsertHead(rsl));
                bw.newLine();
                int i = 0;
                for (List<String> values: rsl.getValues()) {
                    bw.write(String.join(",", values));
                    i++;
                    if (i < rsl.getValues().size()) {
                        bw.newLine();
                    }
                }
            }
        }
    }

    private static String getSqlInsertHead(RecordStringList rsl) {
        return String.join(",", rsl.getFieldNameList());
    }
}
