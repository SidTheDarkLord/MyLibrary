package ru.web.db;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * Спомощью этого класса создается соединение с базой данных
 */
public class Database {

    private Connection conn;
    private InitialContext ic;
    private DataSource ds;

    /*
    * метод, возвращающий объект соединения
    */
    public Connection getConnection() {
        try {
            ic = new InitialContext();
            ds = (DataSource) ic.lookup("jdbc/Library");
            if(conn == null) {
                conn = ds.getConnection();
            }
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return conn;
    }

}
