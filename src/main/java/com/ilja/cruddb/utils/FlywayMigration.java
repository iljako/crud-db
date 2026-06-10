package com.ilja.cruddb.utils;

import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FlywayMigration {

    public static void migrate() {
        Properties props = loadProperties();

        Flyway flyway = Flyway.configure()
                .dataSource(
                        props.getProperty("db.url"),
                        props.getProperty("db.user"),
                        props.getProperty("db.password")
                )
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
        System.out.println("Flyway: миграции успешно применены.");
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream is = FlywayMigration.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (is == null) {
                throw new RuntimeException("application.properties не найден в classpath");
            }
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки application.properties", e);
        }
        return props;
    }
}
