package com.isl.taskmanagement.interfaces.controller;

import com.isl.taskmanagement.application.dto.request.CreateTaskRequest;
import com.isl.taskmanagement.application.dto.request.UpdateTaskRequest;
import com.isl.taskmanagement.application.dto.response.PagedTaskResponse;
import com.isl.taskmanagement.application.dto.response.TaskResponse;
import com.isl.taskmanagement.application.service.TaskService;
import com.isl.taskmanagement.infrastructure.response.ApiResponse;
import com.isl.taskmanagement.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@Validated
@Tag(
        name = "Task Management API",
        description = "Operations related to task management"
)
public class TaskController {

    private final TaskService taskService;

    public TaskController(
            TaskService taskService
    ) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "Create task")
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody
            CreateTaskRequest request
    ) {
        TaskResponse response = taskService.createTask(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.of(
                                201,
                                "Task created successfully",
                                response
                        )
                );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by id")
    public ResponseEntity<ApiResponse<TaskResponse>> getTask(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Task retrieved successfully",
                        taskService.getTaskById(id)
                )
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable String id,
            @Valid @RequestBody
            UpdateTaskRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Task updated successfully",
                        taskService.updateTask(
                                id,
                                request
                        )
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable String id
    ) {

        taskService.deleteTask(id);

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Task deleted successfully",
                        null
                )
        );
    }

    @GetMapping
    @Operation(summary = "Get paginated tasks")
    public ResponseEntity<ApiResponse<PagedTaskResponse>>
    getTasks(

            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0")
            @Min(
                    value = 0,
                    message = "page must be greater than or equal to 0")
            int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10")
            @Positive(message = "size must be greater than 0")
            int size,

            @RequestParam(required = false)
            TaskStatus status,

            @RequestParam(defaultValue = "ASC")
            String sortDirection
    ) {

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Tasks retrieved successfully",
                        taskService.getTasks(
                                page,
                                size,
                                status,
                                sortDirection
                        )
                )
        );
    }
}