package com.ilja.cruddb.service;

import com.ilja.cruddb.model.Label;
import com.ilja.cruddb.repository.LabelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LabelServiceTest {
    @Mock
    private LabelRepository repository;
    private LabelService service;

    @BeforeEach
    void setUp() {
        service = new LabelService(repository);
    }

    @Test
    void create_ShouldSaveAndReturnLabel() {
        Label label = new Label(1L, "Java");
        when(repository.save(any(Label.class))).thenReturn(label);
        Label result = service.create("Java");
        assertNotNull(result);
        assertEquals("Java", result.getName());
    }

    @Test
    void getByName_ShouldReturnLabel() {
        Label label = new Label(1L, "Java");
        when(repository.findByName("Java")).thenReturn(Optional.of(label));
        Optional<Label> result = service.getByName("Java");
        assertTrue(result.isPresent());
        assertEquals("Java", result.get().getName());
    }

    @Test
    void update_WhenNotExists_ShouldReturnFalse() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        assertFalse(service.update(999L, "New Name"));
    }
}