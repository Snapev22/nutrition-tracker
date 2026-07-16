package br.edu.ifsp.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final Properties props = carregarProperties();

    private ConnectionFactory() {
    }

    private static Properties carregarProperties() {
        Properties properties = new Properties();
        try (InputStream input = ConnectionFactory.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "Arquivo application.properties não encontrado no classpath."
                );
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao carregar configurações do banco.", e);
        }
        return properties;
    }

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
        );
    }
}
