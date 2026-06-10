package com.ilja.cruddb.modules;

import com.ilja.cruddb.controller.WriterController;
import com.ilja.cruddb.repository.WriterRepository;
import com.ilja.cruddb.repository.hibernate.HibernateWriterRepositoryImpl;
import com.ilja.cruddb.service.WriterService;
import com.ilja.cruddb.view.WriterView;

import java.util.Scanner;

public class WriterModule implements AppModule {
    private final WriterView view;

    public WriterModule(Scanner scanner) {
        WriterRepository repo = new HibernateWriterRepositoryImpl();
        WriterService service = new WriterService(repo);
        WriterController controller = new WriterController(service);
        this.view = new WriterView(controller, scanner);
    }

    @Override
    public String getMenuTitle() {
        return "Manage Writers";
    }

    @Override
    public void run() {
        view.run();
    }
}
