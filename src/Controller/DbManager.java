package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DbManager {

    private String url;
    private String username;
    private String password;

    protected Connection connection; //same as public but only for this package/all subclasses

    DbManager(String username, String password, String ip, String port, String schema) {
        this.url = "jdbc:mysql://"+ ip +":"+port+"/" + schema;
        this.username = username;
        this.password = password;

        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}

