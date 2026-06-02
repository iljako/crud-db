package com.ilja.cruddb.modules;

import java.util.Scanner;

public class AppModuleFactory {
    private final Scanner scanner;

    public AppModuleFactory(Scanner scanner) {
        this.scanner = scanner;
    }

    public AppModule createModule(ModuleType type) {
        return switch (type) {
            case WRITER -> new WriterModule(scanner);
            case POST -> new PostModule(scanner);
            case LABEL -> new LabelModule(scanner);
            case EXIT -> throw new IllegalArgumentException("Exit is not a runnable module");
        };
    }
}