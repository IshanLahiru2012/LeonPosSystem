package lk.ijse.dep11.leon_pos.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private String code;
    private String description;
    private int qty;
    private BigDecimal unitPrice;
    private transient JFXButton btnDelete;
}
