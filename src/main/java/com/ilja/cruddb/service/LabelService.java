package com.ilja.cruddb.service;

import com.ilja.cruddb.model.Label;
import com.ilja.cruddb.repository.LabelRepository;

import java.util.List;
import java.util.Optional;

public class LabelService {
    private final LabelRepository repository;

    public LabelService(LabelRepository repository) {
        this.repository = repository;
    }

    public Label create(String name) {
        return repository.save(new Label(null, name));
    }

    public Optional<Label> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<Label> getByName(String name) {
        return repository.findByName(name);
    }

    public List<Label> getAll() {
        return repository.findAll();
    }

    public boolean update(Long id, String name) {
        return repository.findById(id).map(label -> {
            label.setName(name);
            repository.save(label);
            return true;
        }).orElse(false);
    }

    public boolean delete(Long id) {
        return repository.findById(id).map(l -> {
            repository.deleteById(id);
            return true;
        }).orElse(false);
    }
}