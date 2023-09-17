package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Locale;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task  extends AbstractRecord{
    private String title;
    private String parent_task;
    private int max_xp;

    public boolean isCheckedByVerter() {
        return title.length() > 1
                && title.toLowerCase(Locale.ROOT).startsWith("c")
                && title.charAt(1) >= '0'
                && title.charAt(1) <= '9';
    }
}

/*
CREATE TABLE tasks
(
    title       VARCHAR(64) PRIMARY KEY,
    parent_task VARCHAR(64),
    max_xp      INT NOT NULL CHECK ( max_xp >= 0 ),
    FOREIGN KEY (parent_task) REFERENCES tasks (title)
);
COMMENT ON TABLE tasks IS 'Таблица Tasks';
COMMENT ON COLUMN tasks.title IS 'Название задания';
COMMENT ON COLUMN tasks.parent_task IS 'Название задания, являющегося условием входа';
COMMENT ON COLUMN tasks.max_xp IS 'Максимальное количество XP';
-- Триггер на проверку, что существует только одно запись с parent_task IS NULL
* */