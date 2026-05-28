package com.ilja.cruddb.config;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    public static DataSource getDataSource() {
        Properties props = new Properties();
        try (InputStream is = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setURL(props.getProperty("db.url"));
        ds.setUser(props.getProperty("db.user"));
        ds.setPassword(props.getProperty("db.password"));
        return ds;
    }
}