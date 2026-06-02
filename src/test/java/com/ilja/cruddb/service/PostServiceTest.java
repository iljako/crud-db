package com.ilja.cruddb.service;

import com.ilja.cruddb.model.Post;
import com.ilja.cruddb.model.PostStatus;
import com.ilja.cruddb.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository repository;
    private PostService service;

    @BeforeEach
    void setUp() {
        service = new PostService(repository);
    }


}