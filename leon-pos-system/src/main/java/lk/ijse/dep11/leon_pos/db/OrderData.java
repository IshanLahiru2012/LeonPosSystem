package lk.ijse.dep11.leon_pos.db;

import lk.ijse.dep11.leon_pos.tm.OrderItem;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderData {
    private static final PreparedStatement STM_EXISTS_BY_CUSTOMER_ID;
    private static final PreparedStatement STM_EXISTS_BY_ITEM_CODE;
    private static final PreparedStatement STM_GET_LAST_ID;
    private static final PreparedStatement STM_INSERT_ORDER;
    private static final PreparedStatement STM_INSERT_ORDER_ITEM;
    private static final PreparedStatement STM_UPDATE_STOCK;
    private static final PreparedStatement STM_FIND;

    static {
        try {
            Connection connection = ConnectionDataSource.getInstance().getConnection();
            STM_EXISTS_BY_CUSTOMER_ID = connection.prepareStatement("SELECT * FROM \"order\" WHERE customer_id = ?");
            STM_EXISTS_BY_ITEM_CODE = connection.prepareStatement("SELECT * FROM order_item WHERE item_code = ?");
            STM_GET_LAST_ID = connection.prepareStatement("SELECT id FROM \"order\" ORDER BY id DESC FETCH FIRST ROWS ONLY");
            STM_INSERT_ORDER = connection.prepareStatement("INSERT INTO \"order\" (id, date, customer_id) VALUES (?,?,?)");
            STM_INSERT_ORDER_ITEM = connection.prepareStatement(
                    "INSERT INTO order_item (order_id, item_code, qty, unit_price) VALUES (?,?,?,?)");
            STM_UPDATE_STOCK = connection.prepareStatement("UPDATE item SET qty = qty - ? WHERE code = ?");
            STM_FIND = connection.prepareStatement("SELECT o.*, c.name, CAST(order_total.total AS DECIMAL(8,2))\n" +
                    "FROM \"order\" AS o\n" +
                    "         INNER JOIN customer AS c ON o.customer_id = c.id\n" +
                    "        INNER JOIN\n" +
                    "(SELECT o.id, SUM(qty * unit_price) AS total\n" +
                    "FROM \"order\" AS o\n" +
                    "         INNER JOIN order_item AS oi ON oi.order_id = o.id GROUP BY o.id) AS order_total\n" +
                    "ON o.id = order_total.id\n" +
                    "WHERE o.id LIKE ? OR CAST(o.date AS VARCHAR(20)) LIKE ? OR o.customer_id LIKE ? OR c.name LIKE ? " +
                    "ORDER BY o.id");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void insertOrder(String orderId, Date orderDate,
                                   String customerId,List<OrderItem> orderItemList) throws SQLException {
        ConnectionDataSource.getInstance().getConnection().setAutoCommit(false);
        try {
            STM_INSERT_ORDER.setString(1, orderId);
            STM_INSERT_ORDER.setDate(2, orderDate);
            STM_INSERT_ORDER.setString(3, customerId);
            STM_INSERT_ORDER.executeUpdate();

            for (OrderItem orderItem : orderItemList) {
                STM_INSERT_ORDER_ITEM.setString(1, orderId);
                STM_INSERT_ORDER_ITEM.setString(2, orderItem.getCode());
                STM_INSERT_ORDER_ITEM.setInt(3, orderItem.getQty());
                STM_INSERT_ORDER_ITEM.setBigDecimal(4, orderItem.getUnitPrice());
                STM_INSERT_ORDER_ITEM.executeUpdate();

                STM_UPDATE_STOCK.setInt(1, orderItem.getQty());
                STM_UPDATE_STOCK.setString(2, orderItem.getCode());
                STM_UPDATE_STOCK.executeUpdate();
            }
            ConnectionDataSource.getInstance().getConnection().commit();
        }catch (Throwable e){
            ConnectionDataSource.getInstance().getConnection().rollback();
            throw new SQLException();
        }finally {
            ConnectionDataSource.getInstance().getConnection().setAutoCommit(true);
        }
    }
}
