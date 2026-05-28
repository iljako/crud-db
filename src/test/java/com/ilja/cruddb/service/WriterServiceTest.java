package com.ilja.cruddb.service;

import com.ilja.cruddb.model.Writer;
import com.ilja.cruddb.repository.WriterRepository;
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
class WriterServiceTest {
    @Mock
    private WriterRepository repository;
    private WriterService service;

    @BeforeEach
    void setUp() {
        service = new WriterService(repository);
    }

    @Test
    void create_ShouldSaveAndReturnWriter() {
        Writer writer = new Writer(1L, "John", "Doe");
        when(repository.save(any(Writer.class))).thenReturn(writer);
        Writer result = service.create("John", "Doe");
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(repository).save(any(Writer.class));
    }

    @Test
    void getById_WhenExists_ShouldReturnWriter() {
        Writer writer = new Writer(1L, "John", "Doe");
        when(repository.findById(1L)).thenReturn(Optional.of(writer));
        Optional<Writer> result = service.getById(1L);
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void update_WhenExists_ShouldReturnTrue() {
        Writer existing = new Writer(1L, "John", "Doe");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Writer.class))).thenAnswer(i -> i.getArgument(0));
        boolean result = service.update(1L, "Johnny", "Doe Jr");
        assertTrue(result);
    }

    @Test
    void update_WhenNotExists_ShouldReturnFalse() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        assertFalse(service.update(999L, "Test", "User"));
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        Writer w = new Writer(1L, "A", "B");
        when(repository.findById(1L)).thenReturn(Optional.of(w));
        service.delete(1L);
        verify(repository).deleteById(1L);
    }
}