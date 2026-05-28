package com.ilja.cruddb.view;

import com.ilja.cruddb.controller.WriterController;
import com.ilja.cruddb.model.Writer;

import java.util.List;
import java.util.Scanner;

public class WriterView {
    private final WriterController controller;
    private final Scanner scanner;

    public WriterView(WriterController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void run() {
        while (true) {
            System.out.println("\n=== Writer Menu ===");
            System.out.println("1. List all");
            System.out.println("2. Get by ID");
            System.out.println("3. Create");
            System.out.println("4. Update");
            System.out.println("5. Delete");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> showAll();
                case "2" -> getById();
                case "3" -> create();
                case "4" -> update();
                case "5" -> delete();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void showAll() {
        List<Writer> writers = controller.getAll();
        System.out.println("\n=== Writers ===");
        if (writers.isEmpty()) {
            System.out.println("No writers found");
            return;
        }
        for (Writer w : writers) System.out.printf("%d. %s %s%n", w.getId(), w.getFirstName(), w.getLastName());
    }

    private void getById() {
        System.out.print("Enter writer ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Writer writer = controller.getById(id);
            if (writer == null) System.out.println("Writer not found");
            else System.out.printf("Writer: %d - %s %s%n", writer.getId(), writer.getFirstName(), writer.getLastName());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID");
        }
    }

    private void create() {
        System.out.print("Enter first name: ");
        String fn = scanner.nextLine();
        System.out.print("Enter last name: ");
        String ln = scanner.nextLine();
        if (!fn.isBlank() && !ln.isBlank()) {
            Writer w = controller.create(fn, ln);
            System.out.println("Created! ID: " + w.getId());
        } else System.out.println("Name cannot be empty");
    }

    private void update() {
        System.out.print("Enter writer ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Enter new first name: ");
            String fn = scanner.nextLine();
            System.out.print("Enter new last name: ");
            String ln = scanner.nextLine();
            if (!fn.isBlank() && !ln.isBlank()) {
                boolean ok = controller.update(id, fn, ln);
                System.out.println(ok ? "Updated!" : "Not found");
            } else System.out.println("Name cannot be empty");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID");
        }
    }

    private void delete() {
        System.out.print("Enter writer ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            boolean ok = controller.delete(id);
            System.out.println(ok ? "Deleted!" : "Not found");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID");
        }
    }
}