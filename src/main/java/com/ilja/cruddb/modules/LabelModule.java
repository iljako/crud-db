package com.ilja.cruddb.modules;

import com.ilja.cruddb.controller.LabelController;
import com.ilja.cruddb.repository.LabelRepository;
import com.ilja.cruddb.repository.jdbc.JdbcLabelRepositoryImpl;
import com.ilja.cruddb.service.LabelService;
import com.ilja.cruddb.view.LabelView;

import java.util.Scanner;

public class LabelModule implements AppModule {
    private final LabelView view;

    public LabelModule(Scanner scanner) {
        LabelRepository repo = new JdbcLabelRepositoryImpl();
        LabelService service = new LabelService(repo);
        LabelController controller = new LabelController(service);
        this.view = new LabelView(controller, scanner);
    }

    @Override
    public String getMenuTitle() {
        return "Manage Labels";
    }

    @Override
    public void run() {
        view.run();
    }
}