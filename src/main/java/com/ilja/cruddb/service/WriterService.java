package com.ilja.cruddb.service;

import com.ilja.cruddb.model.Writer;
import com.ilja.cruddb.repository.WriterRepository;

import java.util.List;
import java.util.Optional;

public class WriterService {
    private final WriterRepository repository;

    public WriterService(WriterRepository repository) {
        this.repository = repository;
    }

    public Writer create(String firstName, String lastName) {
        return repository.save(new Writer(null, firstName, lastName));
    }

    public Optional<Writer> getById(Long id) {
        return repository.findById(id);
    }

    public List<Writer> getAll() {
        return repository.findAll();
    }

    public boolean update(Long id, String firstName, String lastName) {
        return repository.findById(id).map(writer -> {
            writer.setFirstName(firstName);
            writer.setLastName(lastName);
            repository.save(writer);
            return true;
        }).orElse(false);
    }

    public boolean delete(Long id) {
        return repository.findById(id).map(w -> {
            repository.deleteById(id);
            return true;
        }).orElse(false);
    }
}