package com.ilja.cruddb.repository;

import com.ilja.cruddb.model.Post;

import java.util.List;

public interface PostRepository extends GenericRepository<Post, Long> {
    List<Post> findByWriterId(Long writerId);
}