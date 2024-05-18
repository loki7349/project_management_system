package com.example.demo.services;

import com.example.demo.models.Project;
import com.example.demo.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        project = new Project(1L, "Project 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(10));
    }

    @Test
    void createProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        Project createdProject = projectService.createProject(project);
        assertNotNull(createdProject);
        assertEquals(project.getName(), createdProject.getName());
    }

    @Test
    void getAllProjects() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project));
        List<Project> projects = projectService.getAllProjects();
        assertFalse(projects.isEmpty());
        assertEquals(1, projects.size());
    }

    @Test
    void getProjectById() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        Optional<Project> foundProject = projectService.getProjectById(1L);
        assertTrue(foundProject.isPresent());
        assertEquals(project.getName(), foundProject.get().getName());
    }

    @Test
    void updateProject() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        Project updatedProject = projectService.updateProject(1L, project);
        assertNotNull(updatedProject);
        assertEquals(project.getName(), updatedProject.getName());
    }

    @Test
    void deleteProject() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        doNothing().when(projectRepository).delete(any(Project.class));
        projectService.deleteProject(1L);
        verify(projectRepository, times(1)).delete(project);
    }
}
