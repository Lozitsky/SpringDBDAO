package com.kirilo.spring.dao.db;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class DBConnector {
    private final String URL = "jdbc:postgresql://localhost:5432/springdb";
    private final String LOGIN = "root";
    private final String PASS = "root";
    private Connection connection;

    public DBConnector() {
        try {
            connection = getConnection(URL, LOGIN, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getMyConnection() {
        return connection;
    }
}
