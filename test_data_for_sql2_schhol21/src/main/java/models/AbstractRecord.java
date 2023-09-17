package models;

import java.util.HashMap;
import java.util.Map;

public class AbstractRecord {
    protected static Map<String, Long> idSequences = new HashMap<>();

    public static long getNextId(Class<?> clazz) {
        long nextId;
        String clazzName = clazz.getName();
        if (! idSequences.containsKey(clazzName)) {
            idSequences.put(clazzName, 1L);
        }
        nextId = idSequences.get(clazzName);
        idSequences.replace(clazzName, nextId + 1);
        return nextId;
    }

    public String getTableName() {
        return getClass().getSimpleName().toLowerCase();
    }
}
