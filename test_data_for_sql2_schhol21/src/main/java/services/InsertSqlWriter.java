package services;

import models.RecordStringList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InsertSqlWriter {
    public static void writeInserts(List<RecordStringList> list,  String filename) throws IOException {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (int l = 0; l < list.size(); l++) {
                RecordStringList rsl = list.get(l);
                Field[] fields = rsl.getClazz().getDeclaredFields();
                bw.write(getSqlInsertHead(rsl));
                bw.newLine();
                int i = 0;
                for (List<String> values: rsl.getValues()) {
                    bw.write("(");
                    bw.write(IntStream.range(0, fields.length)
                            .mapToObj(j -> fields[j].getType() != long.class && fields[j].getType() != int.class ?
                                        "'" + values.get(j) + "'"
                                        : values.get(j))
                            .map(s -> "'NULL'".equals(s) ?  "NULL" : s)
                            .collect(Collectors.joining(", "))
                    );
                    bw.write(")");
                    i++;
                    if (i < rsl.getValues().size()) {
                        bw.write(",");
                        bw.newLine();
                    }
                    else {
                        bw.write(";");
                        if (rsl.getFieldNameList().stream().anyMatch("id"::equals)) {
                            bw.newLine();
                            int lastId = Integer.parseInt(values.get(0));
                            bw.write("ALTER SEQUENCE " + rsl.getTableName() + "_id_seq RESTART WITH " +
                                    (lastId + 1) + ";");
                        }
                        if (rsl != list.get(list.size() - 1)) {
                            bw.newLine();
                            bw.newLine();
                        }
                    }

                }
            }
        }
    }

    private static String getSqlInsertHead(RecordStringList rsl) {
        return "INSERT INTO " + rsl.getTableName() +  " (" +
                String.join(", ", rsl.getFieldNameList()) +
                ") VALUES";
    }
}
