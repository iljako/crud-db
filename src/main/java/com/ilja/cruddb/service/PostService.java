package com.ilja.cruddb.service;

import com.ilja.cruddb.model.Post;
import com.ilja.cruddb.model.PostStatus;
import com.ilja.cruddb.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Post create(String content, PostStatus status) {
        return repository.save(new Post(null, content, status));
    }

    public Optional<Post> getById(Long id) {
        return repository.findById(id);
    }

    public List<Post> getAll() {
        return repository.findAll();
    }

    public List<Post> getByWriterId(Long writerId) {
        return repository.findByWriterId(writerId);
    }

    public boolean update(Long id, String content, PostStatus status) {
        return repository.findById(id).map(post -> {
            post.setContent(content);
            post.setStatus(status);
            post.setUpdated(LocalDateTime.now());
            repository.save(post);
            return true;
        }).orElse(false);
    }

    public boolean delete(Long id) {
        return repository.findById(id).map(p -> {
            repository.deleteById(id);
            return true;
        }).orElse(false);
    }

    public boolean addLabel(Long postId, Long labelId) {
        return repository.findById(postId).map(p -> {
            repository.addLabelToPost(postId, labelId);
            return true;
        }).orElse(false);
    }

    public boolean removeLabel(Long postId, Long labelId) {
        return repository.findById(postId).map(p -> {
            repository.removeLabelFromPost(postId, labelId);
            return true;
        }).orElse(false);
    }
}