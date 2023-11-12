package lk.ijse.dep11.leon_pos.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String code;
    private String description;
    private int qty;
    private BigDecimal unitPrice;

    @Override
    public String toString() {
        return code;
    }
}
