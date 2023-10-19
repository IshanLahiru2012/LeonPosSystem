package lk.ijse.dep11.leon_pos.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionDataSource {
    private static ConnectionDataSource instance;
    private final Connection connection;
    private ConnectionDataSource(){
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("/application.properties"));
            String url = properties.getProperty("app.datasource.url");
            String username = properties.getProperty("app.datasource.username");
            String password = properties.getProperty("app.datasource.password");
            connection = DriverManager.getConnection(url,username,password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static ConnectionDataSource getInstance (){
        return (instance == null) ? (instance = new ConnectionDataSource()):instance;
    }
    public Connection getConnection(){
        return connection;
    }
}
