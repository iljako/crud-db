package com.ilja.cruddb.repository;

import com.ilja.cruddb.model.Label;

import java.util.Optional;

public interface LabelRepository extends GenericRepository<Label, Long> {
    Optional<Label> findByName(String name);
}