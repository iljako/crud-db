package com.ilja.cruddb.controller;

import com.ilja.cruddb.model.Writer;
import com.ilja.cruddb.service.WriterService;

import java.util.List;

public class WriterController {
    private final WriterService service;

    public WriterController(WriterService service) {
        this.service = service;
    }

    public Writer create(String firstName, String lastName) {
        return service.create(firstName, lastName);
    }

    public Writer getById(Long id) {
        return service.getById(id).orElse(null);
    }

    public List<Writer> getAll() {
        return service.getAll();
    }

    public boolean update(Long id, String firstName, String lastName) {
        return service.update(id, firstName, lastName);
    }

    public boolean delete(Long id) {
        return service.delete(id);
    }
}