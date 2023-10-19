package lk.ijse.dep11.leon_pos.db;

import lk.ijse.dep11.leon_pos.tm.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CustomerData {
    private static final PreparedStatement STM_INSERT;
    private static final PreparedStatement STM_UPDATE;
    private static final PreparedStatement STM_DELETE;
    private static final PreparedStatement STM_GET_LAST_ID;
    private static final PreparedStatement STM_GET_ALL;


    static {
        try {
            Connection connection = ConnectionDataSource.getInstance().getConnection();
            STM_INSERT = connection.prepareStatement("INSERT INTO customer(id, name, address) VALUES (?,?,?)");
            STM_UPDATE = connection.prepareStatement("UPDATE customer SET name = ?,address=? WHERE id=?");
            STM_DELETE = connection.prepareStatement("DELETE FROM customer WHERE id=?");
            STM_GET_LAST_ID = connection.prepareStatement(
                    "SELECT id FROM customer ORDER BY id DESC FETCH FIRST ROW ONLY ");
            STM_GET_ALL = connection.prepareStatement("SELECT * FROM customer ORDER BY id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void insertCustomer(Customer customer) throws SQLException {
        STM_INSERT.setString(1,customer.getId());
        STM_INSERT.setString(2,customer.getName());
        STM_INSERT.setString(3,customer.getAddress());
        STM_INSERT.executeUpdate();
    }
    public static void updateCustomer(Customer customer) throws SQLException {
        STM_UPDATE.setString(1, customer.getName());
        STM_UPDATE.setString(2, customer.getAddress());
        STM_UPDATE.setString(3, customer.getId());
        STM_UPDATE.executeUpdate();
    }
    public static void deleteCustomer(String customerId) throws SQLException {
        STM_DELETE.setString(1,customerId);
        STM_DELETE.executeUpdate();
    }
    public static String getLastCustomerId() throws SQLException {
        ResultSet resultSet = STM_GET_LAST_ID.executeQuery();
        if(resultSet.next()){
            return resultSet.getString(1);
        }else {
            return null;
        }
    }
    public static List<Customer> getAllCustomer()throws SQLException{
        ResultSet resultSet = STM_GET_ALL.executeQuery();
        List<Customer> customerList = new ArrayList<>();
        while (resultSet.next()){
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");
            customerList.add(new Customer(id,name,address));
        }
        return customerList;
    }

}
