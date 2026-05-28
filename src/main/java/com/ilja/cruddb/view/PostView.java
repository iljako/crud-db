package com.ilja.cruddb.view;

import com.ilja.cruddb.controller.PostController;
import com.ilja.cruddb.model.Post;
import com.ilja.cruddb.model.PostStatus;

import java.util.List;
import java.util.Scanner;

public class PostView {
    private final PostController controller;
    private final Scanner scanner;

    public PostView(PostController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void run() {
        while (true) {
            System.out.println("\n=== Post Menu ===");
            System.out.println("1. List all");
            System.out.println("2. Get by ID");
            System.out.println("3. Get by Writer ID");
            System.out.println("4. Create");
            System.out.println("5. Update");
            System.out.println("6. Delete");
            System.out.println("7. Add Label");
            System.out.println("8. Remove Label");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> showAll();
                case "2" -> getById();
                case "3" -> getByWriter();
                case "4" -> create();
                case "5" -> update();
                case "6" -> delete();
                case "7" -> addLabel();
                case "8" -> removeLabel();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void showAll() {
        List<Post> posts = controller.getAll();
        System.out.println("\n=== Posts ===");
        if (posts.isEmpty()) {
            System.out.println("No posts found");
            return;
        }
        for (Post p : posts) System.out.printf("%d. [%s] %s%n", p.getId(), p.getStatus(), p.getContent());
    }

    private void getById() {
        System.out.print("Enter post ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Post post = controller.getById(id);
            if (post == null) System.out.println("Post not found");
            else System.out.printf("Post: %d | %s | %s%n", post.getId(), post.getStatus(), post.getContent());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID");
        }
    }

    private void getByWriter() {
        System.out.print("Enter writer ID: ");
        try {
            Long wid = Long.parseLong(scanner.nextLine());
            List<Post> posts = controller.getByWriterId(wid);
            System.out.println("\n=== Posts by Writer ===");
            if (posts.isEmpty()) {
                System.out.println("No posts found");
                return;
            }
            for (Post p : posts) System.out.printf("%d. [%s] %s%n", p.getId(), p.getStatus(), p.getContent());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID");
        }
    }

    private void create() {
        System.out.print("Enter content: ");
        String content = scanner.nextLine();
        System.out.print("Enter status (ACTIVE/UNDER_REVIEW/DELETED): ");
        String statusStr = scanner.nextLine().toUpperCase();
        if (!content.isBlank()) {
            try {
                PostStatus status = PostStatus.valueOf(statusStr);
                Post post = controller.create(content, status);
                System.out.println("Created! ID: " + post.getId());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status");
            }
        } else System.out.println("Content cannot be empty");
    }

    private void update() {
        System.out.print("Enter post ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Enter new content: ");
            String content = scanner.nextLine();
            System.out.print("Enter new status: ");
            String statusStr = scanner.nextLine().toUpperCase();
            if (!content.isBlank()) {
                try {
                    PostStatus status = PostStatus.valueOf(statusStr);
                    boolean ok = controller.update(id, content, status);
                    System.out.println(ok ? "Updated!" : "Not found");
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid status");
                }
            } else System.out.println("Content cannot be empty");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID");
        }
    }

    private void delete() {
        System.out.print("Enter post ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            boolean ok = controller.delete(id);
            System.out.println(ok ? "Deleted!" : "Not found");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID");
        }
    }

    private void addLabel() {
        System.out.print("Enter post ID: ");
        System.out.print("Enter label ID: ");
        try {
            Long pid = Long.parseLong(scanner.nextLine());
            Long lid = Long.parseLong(scanner.nextLine());
            boolean ok = controller.addLabel(pid, lid);
            System.out.println(ok ? "Label added!" : "Post or Label not found");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID");
        }
    }

    private void removeLabel() {
        System.out.print("Enter post ID: ");
        System.out.print("Enter label ID: ");
        try {
            Long pid = Long.parseLong(scanner.nextLine());
            Long lid = Long.parseLong(scanner.nextLine());
            boolean ok = controller.removeLabel(pid, lid);
            System.out.println(ok ? "Label removed!" : "Post not found");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID");
        }
    }
}