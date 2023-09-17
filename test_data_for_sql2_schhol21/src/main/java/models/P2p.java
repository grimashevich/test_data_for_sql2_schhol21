package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class P2p extends AbstractRecord{
    private long id;
    private long check_id;
    private String checking_peer;
    private String state;
    private LocalTime check_time;
}
