package com.projectsync.backend.repository;

import com.projectsync.backend.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void save_ShouldPersistProject() {
        // Creamos un proyecto de prueba
        Project project = new Project("Repo Test", "Descripción de prueba", "Active", "Alice");

        // Lo guardamos en la base de datos H2
        Project saved = projectRepository.save(project);

        // Verificamos que se haya generado un ID automáticamente
        assertNotNull(saved.getId(), "Id should autogenerate at save);
    }

    @Test
    void findById_ShouldReturnProject() {
        Project project = new Project("Find Test", "Descripción", "Active", "Bob");
        Project saved = projectRepository.save(project);

        // Buscamos por ID
        Optional<Project> found = projectRepository.findById(saved.getId());

        // Comprobamos que lo encontramos y los datos son correctos
        assertTrue(found.isPresent(), "The project should be found by ID");
        assertEquals("Find Test", found.get().getName(), "The name must match the saved one.");
    }

    @Test
    void deleteById_ShouldRemoveProject() {
        Project project = new Project("Delete Test", "Descripción", "Active", "Charlie");
        Project saved = projectRepository.save(project);

        // Eliminamos el proyecto
        projectRepository.deleteById(saved.getId());

        // Verificamos que ya no exista
        Optional<Project> found = projectRepository.findById(saved.getId());
        assertFalse(found.isPresent(), "The project should be removed from the database");
    }
}
