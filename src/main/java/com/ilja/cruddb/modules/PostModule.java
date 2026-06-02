package com.ilja.cruddb.modules;

import com.ilja.cruddb.controller.PostController;
import com.ilja.cruddb.repository.PostRepository;
import com.ilja.cruddb.repository.jdbc.JdbcPostRepositoryImpl;
import com.ilja.cruddb.service.PostService;
import com.ilja.cruddb.view.PostView;

import java.util.Scanner;

public class PostModule implements AppModule {
    private final PostView view;

    public PostModule(Scanner scanner) {
        PostRepository repo = new JdbcPostRepositoryImpl();
        PostService service = new PostService(repo);
        PostController controller = new PostController(service);
        this.view = new PostView(controller, scanner);
    }

    @Override
    public String getMenuTitle() {
        return "Manage Posts";
    }

    @Override
    public void run() {
        view.run();
    }
}
