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
public class Peer extends AbstractRecord{
    private String nickName;
    private LocalDate birthday;
}

/*
CREATE TABLE peers
(
    nickname VARCHAR(16) PRIMARY KEY,
    birthday DATE NOT NULL
);
COMMENT ON TABLE peers IS 'Таблица Peers';
COMMENT ON COLUMN peers.nickname IS 'Ник пира. Primary key';
COMMENT ON COLUMN peers.birthday IS 'Дата рождения';
* */