package services;

import models.AbstractRecord;
import models.RecordStringList;

import java.util.List;

public class RecordListToRecordStringListConverter {

    public static<T extends AbstractRecord> RecordStringList convert(List<T> recordList) throws IllegalAccessException {
        if (recordList.isEmpty()) {
            return null;
        }
        RecordStringList recordStringList = new RecordStringList(recordList.get(0).getClass(),
                recordList.get(0).getTableName());
        for (T record: recordList) {
            recordStringList.addValues(RecordToListStringConverter.covert(record));
        }
        return recordStringList;
    }
}
