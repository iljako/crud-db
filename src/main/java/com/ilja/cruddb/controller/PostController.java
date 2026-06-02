package com.ilja.cruddb.controller;

import com.ilja.cruddb.model.Post;
import com.ilja.cruddb.model.PostStatus;
import com.ilja.cruddb.service.PostService;

import java.util.List;

public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public Post create(String content, PostStatus status) {
        return service.create(content, status);
    }

    public Post getById(Long id) {
        return service.getById(id).orElse(null);
    }

    public List<Post> getAll() {
        return service.getAll();
    }

    public List<Post> getByWriterId(Long writerId) {
        return service.getByWriterId(writerId);
    }

    public boolean update(Long id, String content, PostStatus status) {
        return service.update(id, content, status);
    }

    public boolean delete(Long id) {
        return service.delete(id);
    }

    public boolean addLabel(Long postId, Long labelId) {
        //return service.addLabel(postId, labelId);
        return false;
    }

    public boolean removeLabel(Long postId, Long labelId) {
        //return service.removeLabel(postId, labelId);
        return false;
    }
}