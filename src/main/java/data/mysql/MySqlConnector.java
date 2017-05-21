package data.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by ms on 08-05-17.
 */
public class MySqlConnector {

    private String url;
    private String username;
    private String password;

    private Connection connection;

    public MySqlConnector(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

    }

    public void open() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
