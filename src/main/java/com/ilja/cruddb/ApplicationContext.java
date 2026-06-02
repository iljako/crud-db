package com.ilja.cruddb;

import com.ilja.cruddb.modules.AppModule;
import com.ilja.cruddb.modules.AppModuleFactory;
import com.ilja.cruddb.modules.ModuleType;

import java.util.Scanner;

public class ApplicationContext {
    public static void run() {
        Scanner scanner = new Scanner(System.in);
        AppModuleFactory factory = new AppModuleFactory(scanner);

        while (true) {
            System.out.println("\n=== Main Menu ===");

            for (ModuleType type : ModuleType.values()) {
                if (type != ModuleType.EXIT) {
                    System.out.println(type.getCode() + ". " + type.getDescription());
                }
            }
            System.out.println(ModuleType.EXIT.getCode() + ". " + ModuleType.EXIT.getDescription());

            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            ModuleType selectedType = ModuleType.fromCode(choice);

            if (selectedType == null) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            if (selectedType == ModuleType.EXIT) {
                System.out.println("Exit!");
                scanner.close();
                break;
            }

            try {
                AppModule module = factory.createModule(selectedType);
                module.run();
            } catch (Exception e) {
                System.err.println("Error running module: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
