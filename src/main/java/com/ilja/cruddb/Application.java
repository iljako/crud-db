package com.ilja.cruddb;

import com.ilja.cruddb.config.DatabaseConfig;
import com.ilja.cruddb.controller.*;
import com.ilja.cruddb.repository.*;
import com.ilja.cruddb.service.*;
import com.ilja.cruddb.view.*;

import javax.sql.DataSource;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        DataSource ds = DatabaseConfig.getDataSource();
        Scanner scanner = new Scanner(System.in);

        // Writer
        var wRepo = new JdbcWriterRepositoryImpl(ds);
        var wService = new WriterService(wRepo);
        var wCtrl = new WriterController(wService);
        var wView = new WriterView(wCtrl, scanner);

        // Post
        var pRepo = new JdbcPostRepositoryImpl(ds);
        var pService = new PostService(pRepo);
        var pCtrl = new PostController(pService);
        var pView = new PostView(pCtrl, scanner);

        // Label
        var lRepo = new JdbcLabelRepositoryImpl(ds);
        var lService = new LabelService(lRepo);
        var lCtrl = new LabelController(lService);
        var lView = new LabelView(lCtrl, scanner);

        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Manage Writers");
            System.out.println("2. Manage Posts");
            System.out.println("3. Manage Labels");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> wView.run();
                case "2" -> pView.run();
                case "3" -> lView.run();
                case "0" -> { System.out.println("Exit!"); scanner.close(); return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}