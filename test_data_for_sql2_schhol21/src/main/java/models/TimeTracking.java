package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeTracking extends AbstractRecord{
    private long id;
    private String peer;
    private LocalDate event_date;
    private LocalTime event_time;
    private int state;

    @Override
    public String getTableName() {
        return "time_tracking";
    }
}
