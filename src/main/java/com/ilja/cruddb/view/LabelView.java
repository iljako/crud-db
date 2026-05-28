package com.ilja.cruddb.view;

import com.ilja.cruddb.controller.LabelController;
import com.ilja.cruddb.model.Label;

import java.util.List;
import java.util.Scanner;

public class LabelView {
    private final LabelController controller;
    private final Scanner scanner;

    public LabelView(LabelController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void run() {
        while (true) {
            System.out.println("\n=== Label Menu ===");
            System.out.println("1. List all");
            System.out.println("2. Get by ID");
            System.out.println("3. Get by Name");
            System.out.println("4. Create");
            System.out.println("5. Update");
            System.out.println("6. Delete");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> showAll();
                case "2" -> getById();
                case "3" -> getByName();
                case "4" -> create();
                case "5" -> update();
                case "6" -> delete();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void showAll() {
        List<Label> labels = controller.getAll();
        System.out.println("\n=== Labels ===");
        if (labels.isEmpty()) {
            System.out.println("No labels found");
            return;
        }
        for (Label l : labels) System.out.printf("%d. %s%n", l.getId(), l.getName());
    }

    private void getById() {
        System.out.print("Enter label ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Label label = controller.getById(id);
            if (label == null) System.out.println("Label not found");
            else System.out.printf("Label: %d - %s%n", label.getId(), label.getName());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID");
        }
    }

    private void getByName() {
        System.out.print("Enter label name: ");
        String name = scanner.nextLine();
        Label label = controller.getByName(name);
        if (label == null) System.out.println("Label not found");
        else System.out.printf("Label: %d - %s%n", label.getId(), label.getName());
    }

    private void create() {
        System.out.print("Enter label name: ");
        String name = scanner.nextLine();
        if (!name.isBlank()) {
            Label label = controller.create(name);
            System.out.println("Created! ID: " + label.getId());
        } else System.out.println("Name cannot be empty");
    }

    private void update() {
        System.out.print("Enter label ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Enter new name: ");
            String name = scanner.nextLine();
            if (!name.isBlank()) {
                boolean ok = controller.update(id, name);
                System.out.println(ok ? "Updated!" : "Not found");
            } else System.out.println("Name cannot be empty");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID");
        }
    }

    private void delete() {
        System.out.print("Enter label ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            boolean ok = controller.delete(id);
            System.out.println(ok ? "Deleted!" : "Not found");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID");
        }
    }
}