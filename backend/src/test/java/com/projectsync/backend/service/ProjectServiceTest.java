package com.projectsync.backend.service;

 import com.projectsync.backend.service.ProjectService;
 import com.projectsync.backend.model.Project;
 import com.projectsync.backend.repository.ProjectRepository;
 import org.junit.jupiter.api.AfterEach;
 import org.junit.jupiter.api.Test;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.context.SpringBootTest;
 import org.springframework.test.annotation.DirtiesContext;

 import java.util.List;
 import java.util.Optional;

 import static org.junit.jupiter.api.Assertions.*;

 @SpringBootTest
 @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
 class ProjectServiceTest {

     @Autowired
     private ProjectService projectService;

     @Autowired
     private ProjectRepository projectRepository;

     @AfterEach
     void cleanUp() {
         projectRepository.deleteAll(); // Limpiar la base entre tests
     }

     @Test
     void createProject_ShouldSaveProject() {
         Project project = new Project("Proyecto A", "Descripción A", "Active", "Alice");

         Project saved = projectService.createProject(project);

         assertNotNull(saved.getId(), "The ID should be generated");
         assertEquals("Proyecto A", saved.getName());
         assertEquals("Active", saved.getStatus());
     }

     @Test
     void getProjectById_ShouldReturnProject() {
         Project project = projectService.createProject(new Project("B", "Desc B", "Pending", "Bob"));

         Optional<Project> found = projectService.getProjectById(project.getId());

         assertTrue(found.isPresent(), "The project should be");
         assertEquals("B", found.get().getName());
     }

     @Test
     void getAllProjects_ShouldReturnAll() {
         projectService.createProject(new Project("C1", "Desc C1", "Active", "Charlie"));
         projectService.createProject(new Project("C2", "Desc C2", "Active", "Charlie"));

         List<Project> projects = projectService.getAllProjects();

         assertEquals(2, projects.size(), "I should return all projects");
     }

     @Test
     void updateProject_ShouldModifyFields() {
         Project project = projectService.createProject(new Project("D", "Desc D", "Pending", "Dave"));

         Project updatedInfo = new Project("D updated", "Desc updated", "Active", "Dave");
         Project updated = projectService.updateProject(project.getId(), updatedInfo);

         assertEquals("D updated", updated.getName());
         assertEquals("Desc updated", updated.getDescription());
         assertEquals("Active", updated.getStatus());
     }

     @Test
     void deleteProject_ShouldRemoveProject() {
         Project project = projectService.createProject(new Project("E", "Desc E", "Active", "Eve"));

         projectService.deleteProject(project.getId());

         assertFalse(projectService.getProjectById(project.getId()).isPresent(),
                 "El proyecto debería haber sido eliminado");
     }

     @Test
     void updateNonExistingProject_ShouldThrow() {
         RuntimeException exception = assertThrows(RuntimeException.class,
                 () -> projectService.updateProject(999L, new Project("X", "X", "X", "X")));

         assertEquals("Project not found", exception.getMessage());
     }

     @Test
     void deleteNonExistingProject_ShouldThrow() {
         RuntimeException exception = assertThrows(RuntimeException.class,
                 () -> projectService.deleteProject(999L));

         assertEquals("Project not found", exception.getMessage());
     }
 }
