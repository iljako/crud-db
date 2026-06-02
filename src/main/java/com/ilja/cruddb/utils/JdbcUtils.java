package com.ilja.cruddb.utils;

import com.ilja.cruddb.config.DatabaseConfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtils {
    private static volatile JdbcUtils instance;
    private static Connection connection = null;

    private JdbcUtils() {
        try {
            connection = DatabaseConfig.getDataSource().getConnection();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException {
        if (instance == null) {
            instance = new JdbcUtils();
        }
        return connection.prepareStatement(sql,  Statement.RETURN_GENERATED_KEYS);
    }

    public static Statement getStatement() throws SQLException {
        if (instance == null) {
            instance = new JdbcUtils();
        }
        return connection.createStatement();
    }
}
