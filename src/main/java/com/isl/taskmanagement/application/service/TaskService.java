package com.isl.taskmanagement.application.service;

import com.isl.taskmanagement.application.dto.request.CreateTaskRequest;
import com.isl.taskmanagement.application.dto.request.UpdateTaskRequest;
import com.isl.taskmanagement.application.dto.response.PagedTaskResponse;
import com.isl.taskmanagement.application.dto.response.TaskResponse;
import com.isl.taskmanagement.domain.enums.TaskStatus;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest request);

    TaskResponse getTaskById(String taskId);

    TaskResponse updateTask(
            String taskId,
            UpdateTaskRequest request
    );

    void deleteTask(String taskId);

    List<TaskResponse> getAllTasks();

    PagedTaskResponse getTasks(
            int page,
            int size,
            TaskStatus status,
            String sortDirection
    );
}