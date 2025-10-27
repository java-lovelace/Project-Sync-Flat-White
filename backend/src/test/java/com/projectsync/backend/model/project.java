package com.projectsync.backend.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    @Test
    void onCreate_ShouldSetCreatedAtAndUpdatedAt() {
        Project project = new Project("Project A", "Description", "Active", "Alice");

        project.onCreate();

        assertNotNull(project.getCreatedAt(), "createdAt no debería ser null");
        assertNotNull(project.getUpdatedAt(), "updatedAt no debería ser null");
        assertEquals(project.getCreatedAt(), project.getUpdatedAt(), "createdAt y updatedAt deberían ser iguales en creación");
    }

    @Test
    void onUpdate_ShouldUpdateUpdatedAtOnly() throws InterruptedException {
        Project project = new Project("Project B", "Description", "Active", "Bob");

        project.onCreate();
        LocalDateTime createdAt = project.getCreatedAt();

        Thread.sleep(5); // se hace una pausa para que updatedAt sea posterior a createdAt
        project.onUpdate();

        assertEquals(createdAt, project.getCreatedAt(), "createdAt no debería cambiar");
        assertTrue(project.getUpdatedAt().isAfter(createdAt), "updatedAt debería ser posterior a createdAt");
    }

    @Test
    void constructor_ShouldInitializeFieldsProperly() {
        Project project = new Project("Project C", "Desc", "Pending", "Charlie");

        assertEquals("Project C", project.getName());
        assertEquals("Desc", project.getDescription());
        assertEquals("Pending", project.getStatus());
        assertEquals("Charlie", project.getResponsible());
    }
}

