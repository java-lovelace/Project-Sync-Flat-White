package com.projectsync.backend.service;

import com.projectsync.backend.model.Project;
import com.projectsync.backend.repository.ProjectRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        validateProject(project);
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project updatedProject) {
        return projectRepository.findById(id).map(existing -> {
            validateProject(updatedProject);
            existing.setName(updatedProject.getName());
            existing.setDescription(updatedProject.getDescription());
            existing.setStatus(updatedProject.getStatus());
            existing.setResponsible(updatedProject.getResponsible());
            return projectRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    private void validateProject(Project project) {
        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new ConstraintViolationException("Project name cannot be empty", null);
        }
        if (project.getStatus() == null || project.getStatus().trim().isEmpty()) {
            throw new ConstraintViolationException("Project status cannot be empty", null);
        }
        if (project.getResponsible() == null || project.getResponsible().trim().isEmpty()) {
            throw new ConstraintViolationException("Project responsible cannot be empty", null);
        }
    }
}