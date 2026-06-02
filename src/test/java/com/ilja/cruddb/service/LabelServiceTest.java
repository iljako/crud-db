package com.ilja.cruddb.service;

import com.ilja.cruddb.model.Label;
import com.ilja.cruddb.repository.LabelRepository;
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
class LabelServiceTest {
    @Mock
    private LabelRepository repository;

    @InjectMocks
    private LabelService service;

    @Test
    void create_shouldSaveLabel() {
        when(repository.save(any())).thenReturn(new Label(1L, "Java"));

        Label result = service.create("Java");

        assertEquals("Java", result.getName());
        verify(repository).save(any());
    }

    @Test
    void getById_shouldReturnLabel() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Label(1L, "Java")));

        Optional<Label> result = service.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Java", result.get().getName());
    }

    @Test
    void getAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(new Label(1L, "Java")));

        List<Label> result = service.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void update_shouldReturnTrue() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Label(1L, "Java")));
        when(repository.save(any())).thenReturn(new Label(1L, "Spring"));

        boolean result = service.update(1L, "Spring");

        assertTrue(result);
    }

    @Test
    void delete_shouldReturnTrue() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Label(1L, "Java")));

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(repository).deleteById(1L);
    }
}