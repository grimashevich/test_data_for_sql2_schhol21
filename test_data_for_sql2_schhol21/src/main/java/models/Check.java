package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Check extends AbstractRecord {
    private long id;
    private String peer;
    private String task;
    private LocalDate check_date;

    @Override
    public String getTableName() {
        return "checks";
    }
}

/*
CREATE TABLE checks
(
    id         SERIAL PRIMARY KEY,
    peer       VARCHAR(16) NOT NULL,
    task       VARCHAR(64) NOT NULL,
    check_date DATE        NOT NULL,
    FOREIGN KEY (peer) REFERENCES peers (nickname),
    FOREIGN KEY (task) REFERENCES tasks (title)
);
COMMENT ON TABLE checks IS 'Описывает проверку задания в целом';
COMMENT ON COLUMN checks.id IS 'Уникальный идентификатор. PK';
COMMENT ON COLUMN checks.peer IS 'Ник пира. Внешняя ссылка на таблицу Peers';
COMMENT ON COLUMN checks.task IS 'Название задания. Внешняя ссылка на таблицу Tasks';
COMMENT ON COLUMN checks.check_date IS 'Дата проверки';
-- При создании проверки, проверяется, что выполнен ParentTask
-- Создать функцию, которая возвращает BOOLEAN успешности проверки по ID
* */