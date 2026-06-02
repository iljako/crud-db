package com.ilja.cruddb.utils;

import com.ilja.cruddb.config.DatabaseConfig;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.sql.DataSource;
import java.sql.Connection;

public class LiquibaseMigration {
    private static final String CHANGELOG_PATH = "db/changelog/db.changelog-master.xml";

    private static DataSource dataSource = null;

    private LiquibaseMigration() {

    }

    public static void migrate() {
        if (dataSource == null) {
            dataSource = DatabaseConfig.getDataSource();
        }
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(CHANGELOG_PATH,
                    new ClassLoaderResourceAccessor(), database);

            liquibase.update("");

            System.out.println("Database migration completed successfully");

        } catch (Exception e) {
            throw new RuntimeException("Database migration failed", e);
        }
    }
}
