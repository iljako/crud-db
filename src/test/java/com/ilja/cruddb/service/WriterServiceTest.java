package com.ilja.cruddb.service;

import com.ilja.cruddb.model.Writer;
import com.ilja.cruddb.repository.WriterRepository;
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
class WriterServiceTest {
    @Mock
    private WriterRepository repository;

    @InjectMocks
    private WriterService service;

    private Writer testWriter;

    @BeforeEach
    void setUp() {
        testWriter = new Writer(1L, "John", "Doe");
    }

    @Test
    void create_shouldSaveWriter() {
        when(repository.save(any())).thenReturn(testWriter);

        Writer result = service.create("John", "Doe");

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(repository).save(any());
    }

    @Test
    void getById_shouldReturnWriter() {
        when(repository.findById(1L)).thenReturn(Optional.of(testWriter));

        Optional<Writer> result = service.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());
    }

    @Test
    void getAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(testWriter));

        List<Writer> result = service.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void update_shouldReturnTrue() {
        when(repository.findById(1L)).thenReturn(Optional.of(testWriter));
        when(repository.save(any())).thenReturn(testWriter);

        boolean result = service.update(1L, "Jane", "Smith");

        assertTrue(result);
        assertEquals("Jane", testWriter.getFirstName());
        assertEquals("Smith", testWriter.getLastName());
    }

    @Test
    void delete_shouldReturnTrue() {
        when(repository.findById(1L)).thenReturn(Optional.of(testWriter));

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(repository).deleteById(1L);
    }
}