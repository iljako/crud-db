package com.ilja.cruddb;

import com.ilja.cruddb.utils.LiquibaseMigration;

public class Application {
    static void main(String[] args) {
        LiquibaseMigration.migrate();
        ApplicationContext.run();
    }
}