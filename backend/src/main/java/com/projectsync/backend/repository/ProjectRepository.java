package com.projectsync.backend.repository;

import com.projectsync.backend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // No additional code needed now, JpaRepository already provides save, findAll, findById, deleteById
}