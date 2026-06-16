package br.edu.ifsp.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL =
            "jdbc:mysql://localhost:3306/fittech_db";

    private static final String USER =
            "root";

    private static final String PASSWORD =
            "root123";

    private ConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}
