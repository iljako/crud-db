package com.ilja.cruddb.controller;

import com.ilja.cruddb.model.Label;
import com.ilja.cruddb.service.LabelService;

import java.util.List;

public class LabelController {
    private final LabelService service;

    public LabelController(LabelService service) {
        this.service = service;
    }

    public Label create(String name) {
        return service.create(name);
    }

    public Label getById(Long id) {
        return service.getById(id).orElse(null);
    }

    public Label getByName(String name) {
        return service.getByName(name).orElse(null);
    }

    public List<Label> getAll() {
        return service.getAll();
    }

    public boolean update(Long id, String name) {
        return service.update(id, name);
    }

    public boolean delete(Long id) {
        return service.delete(id);
    }
}