package lk.ijse.dep11.leon_pos.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    private String orderId;
    private String orderDate;
    private String customerId;
    private String customerName;
    private BigDecimal orderTotal;
}
