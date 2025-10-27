package com.projectsync.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectsync.backend.model.Project;
import com.projectsync.backend.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON

    @BeforeEach
    void cleanUp() {
        projectRepository.deleteAll();
    }

    @Test
    void createProject_ShouldReturnCreatedProject() throws Exception {
        Project project = new Project("API Test", "Descripción API", "Active", "Alice");

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("API Test"))
                .andExpect(jsonPath("$.status").value("Active"));
    }

    @Test
    void getAllProjects_ShouldReturnList() throws Exception {
        projectRepository.save(new Project("P1", "D1", "Active", "Bob"));
        projectRepository.save(new Project("P2", "D2", "Pending", "Charlie"));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getProjectById_ShouldReturnProject() throws Exception {
        Project project = projectRepository.save(new Project("P3", "D3", "Active", "Dave"));

        mockMvc.perform(get("/api/projects/" + project.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("P3"));
    }

    @Test
    void updateProject_ShouldModifyProject() throws Exception {
        Project project = projectRepository.save(new Project("P4", "D4", "Pending", "Eve"));
        Project updated = new Project("P4 Updated", "D4 Updated", "Active", "Eve");

        mockMvc.perform(put("/api/projects/" + project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("P4 Updated"))
                .andExpect(jsonPath("$.status").value("Active"));
    }

    @Test
    void deleteProject_ShouldReturnNoContent() throws Exception {
        Project project = projectRepository.save(new Project("P5", "D5", "Active", "Frank"));

        mockMvc.perform(delete("/api/projects/" + project.getId()))
                .andExpect(status().isNoContent());
    }

}
