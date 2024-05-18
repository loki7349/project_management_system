package com.example.demo.controllers;

import com.example.demo.models.Project;
import com.example.demo.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    private Project project;

    @BeforeEach
    void setUp() {
        project = new Project(1L, "Project 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(10));
    }

    @Test
    void createProject() throws Exception {
        Mockito.when(projectService.createProject(any(Project.class))).thenReturn(project);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Project 1\",\"description\":\"Description 1\",\"startDate\":\"2024-05-17\",\"endDate\":\"2024-05-27\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(project.getName())))
                .andDo(print());
    }

    @Test
    void getAllProjects() throws Exception {
        Mockito.when(projectService.getAllProjects()).thenReturn(Arrays.asList(project));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(project.getName())))
                .andDo(print());
    }

    @Test
    void getProjectById() throws Exception {
        Mockito.when(projectService.getProjectById(anyLong())).thenReturn(Optional.of(project));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(project.getName())))
                .andDo(print());
    }

    @Test
    void updateProject() throws Exception {
        Mockito.when(projectService.updateProject(anyLong(), any(Project.class))).thenReturn(project);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Project\",\"description\":\"Updated Description\",\"startDate\":\"2024-05-17\",\"endDate\":\"2024-05-27\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Project")))
                .andDo(print());
    }

    @Test
    void deleteProject() throws Exception {
        Mockito.doNothing().when(projectService).deleteProject(anyLong());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
