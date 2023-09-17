package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferredPoints extends AbstractRecord{
    private long id;
    private String checking_peer;
    private String checked_peer;
    private int points_amount;

    @Override
    public String getTableName() {
        return "transferred_points";
    }
}
