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

    @Test
    void create_ShouldSaveAndReturnPost() {
        Post post = new Post(1L, "Content", PostStatus.ACTIVE);
        when(repository.save(any(Post.class))).thenReturn(post);
        Post result = service.create("Content", PostStatus.ACTIVE);
        assertNotNull(result);
        assertEquals("Content", result.getContent());
    }

    @Test
    void update_WhenExists_ShouldReturnTrue() {
        Post existing = new Post(1L, "Old", PostStatus.ACTIVE);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Post.class))).thenAnswer(i -> i.getArgument(0));
        boolean result = service.update(1L, "New", PostStatus.DELETED);
        assertTrue(result);
    }

    @Test
    void addLabel_ShouldCallRepository() {
        Post p = new Post(1L, "C", PostStatus.ACTIVE);
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        boolean result = service.addLabel(1L, 2L);
        assertTrue(result);
        verify(repository).addLabelToPost(1L, 2L);
    }
}