package com.isl.taskmanagement.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isl.taskmanagement.domain.entity.Task;
import com.isl.taskmanagement.domain.enums.TaskStatus;
import com.isl.taskmanagement.infrastructure.repository.JpaTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaTaskRepository jpaTaskRepository;

    private String taskId;

    @BeforeEach
    void setUp() {

        jpaTaskRepository.deleteAll();

        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setTitle("Existing Task");
        task.setDescription("Existing Description");
        task.setStatus(TaskStatus.PENDING);
        task.setDueDate(LocalDate.now().plusDays(10));

        Task saved = jpaTaskRepository.save(task);

        taskId = saved.getId();
    }

    @Test
    @DisplayName("Create task success")
    void createTaskSuccess() throws Exception {

        String request = """
                {
                  "title":"Learn Spring Boot",
                  "description":"Task API",
                  "status":"PENDING",
                  "dueDate":"2030-12-31"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/tasks")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(request)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.title")
                        .value("Learn Spring Boot"))
                .andExpect(jsonPath("$.data.createdAt",
                        notNullValue()))
                .andExpect(jsonPath("$.data.updatedAt",
                        notNullValue()));
    }

    @Test
    @DisplayName("Validation failure")
    void validationFailure() throws Exception {

        String request = """
                {
                  "title":"",
                  "description":"Task",
                  "dueDate":"2030-12-31"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/tasks")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400))
                .andExpect(jsonPath("$.message")
                        .exists());
    }

    @Test
    @DisplayName("Get task success")
    void getTaskSuccess() throws Exception {

        mockMvc.perform(
                        get("/api/v1/tasks/{id}",
                                taskId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id")
                        .value(taskId))
                .andExpect(jsonPath("$.data.createdAt")
                        .exists())
                .andExpect(jsonPath("$.data.updatedAt")
                        .exists());
    }

    @Test
    @DisplayName("Task not found")
    void taskNotFound() throws Exception {

        mockMvc.perform(
                        get("/api/v1/tasks/{id}",
                                UUID.randomUUID()
                                        .toString())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status")
                        .value(404));
    }

    @Test
    @DisplayName("Update task success")
    void updateTaskSuccess() throws Exception {

        String request = """
                {
                  "title":"Updated Task",
                  "description":"Updated",
                  "status":"DONE",
                  "dueDate":"2031-01-01"
                }
                """;

        mockMvc.perform(
                        put("/api/v1/tasks/{id}",
                                taskId)
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title")
                        .value("Updated Task"))
                .andExpect(jsonPath("$.data.status")
                        .value("DONE"));
    }

    @Test
    @DisplayName("Delete task success")
    void deleteTaskSuccess() throws Exception {

        mockMvc.perform(
                        delete("/api/v1/tasks/{id}",
                                taskId)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Pagination success")
    void paginationSuccess() throws Exception {

        for (int i = 0; i < 15; i++) {

            Task task = new Task();
            task.setId(UUID.randomUUID().toString());
            task.setTitle("Task " + i);
            task.setDescription("Desc");
            task.setStatus(TaskStatus.PENDING);
            task.setDueDate(
                    LocalDate.now().plusDays(20 + i)
            );

            jpaTaskRepository.save(task);
        }

        mockMvc.perform(
                        get("/api/v1/tasks")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content",
                        hasSize(10)))
                .andExpect(jsonPath("$.data.totalElements",
                        greaterThan(10)));
    }

    @Test
    @DisplayName("Filter by status")
    void filterByStatus() throws Exception {

        mockMvc.perform(
                        get("/api/v1/tasks")
                                .param("status",
                                        "PENDING")
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Invalid status filter returns 400")
    void invalidStatusFilterReturns400()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/tasks")
                                .param("status",
                                        "INVALID_STATUS")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400));
    }

    @Test
    @DisplayName("Invalid due date format returns 400")
    void invalidDateReturns400()
            throws Exception {

        String request = """
                {
                  "title":"Task",
                  "description":"Desc",
                  "status":"PENDING",
                  "dueDate":"31-12-2030"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/tasks")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400));
    }

    @Test
    @DisplayName("Malformed JSON returns 400")
    void malformedJsonReturns400()
            throws Exception {

        String request =
                "{ invalid json }";

        mockMvc.perform(
                        post("/api/v1/tasks")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400));
    }

    @Test
    @DisplayName("Negative page returns 400")
    void negativePageReturns400()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/tasks")
                                .param("page",
                                        "-1")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400));
    }

    @Test
    @DisplayName("Size zero returns 400")
    void sizeZeroReturns400()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/tasks")
                                .param("size",
                                        "0")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400));
    }

    @Test
    @DisplayName("Audit fields present")
    void auditFieldsPresent()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/tasks/{id}",
                                taskId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.createdAt")
                        .exists())
                .andExpect(jsonPath("$.data.updatedAt")
                        .exists());
    }
}