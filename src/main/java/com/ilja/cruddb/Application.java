package com.ilja.cruddb;

import com.ilja.cruddb.utils.FlywayMigration;

public class Application {
    static void main(String[] args) {
        FlywayMigration.migrate();
        ApplicationContext.run();
    }
}