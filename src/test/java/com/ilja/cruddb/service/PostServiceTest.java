package com.ilja.cruddb.service;

import com.ilja.cruddb.model.Post;
import com.ilja.cruddb.model.PostStatus;
import com.ilja.cruddb.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository repository;

    @InjectMocks
    private PostService service;

    private Post testPost;

    @BeforeEach
    void setUp() {
        testPost = new Post(1L, "Test content", PostStatus.ACTIVE);
    }

    @Test
    void create_shouldSavePost() {
        when(repository.save(any())).thenReturn(testPost);

        Post result = service.create("Test content", PostStatus.ACTIVE);

        assertEquals("Test content", result.getContent());
        assertEquals(PostStatus.ACTIVE, result.getStatus());
        verify(repository).save(any());
    }

    @Test
    void getById_shouldReturnPost() {
        when(repository.findById(1L)).thenReturn(Optional.of(testPost));

        Optional<Post> result = service.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test content", result.get().getContent());
    }

    @Test
    void getAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(testPost));

        List<Post> result = service.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void getByWriterId_shouldReturnList() {
        when(repository.findByWriterId(10L)).thenReturn(List.of(testPost));

        List<Post> result = service.getByWriterId(10L);

        assertEquals(1, result.size());
        verify(repository).findByWriterId(10L);
    }

    @Test
    void update_shouldReturnTrue() {
        when(repository.findById(1L)).thenReturn(Optional.of(testPost));
        when(repository.save(any())).thenReturn(testPost);

        boolean result = service.update(1L, "New content", PostStatus.DELETED);

        assertTrue(result);
        assertEquals("New content", testPost.getContent());
        assertEquals(PostStatus.DELETED, testPost.getStatus());
    }

    @Test
    void delete_shouldReturnTrue() {
        when(repository.findById(1L)).thenReturn(Optional.of(testPost));

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(repository).deleteById(1L);
    }
}