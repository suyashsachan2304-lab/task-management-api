package com.isl.taskmanagement.application.mapper;

import com.isl.taskmanagement.application.dto.request.CreateTaskRequest;
import com.isl.taskmanagement.application.dto.request.UpdateTaskRequest;
import com.isl.taskmanagement.application.dto.response.TaskResponse;
import com.isl.taskmanagement.domain.entity.Task;
import com.isl.taskmanagement.domain.enums.TaskStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TaskMapper {

    public Task toEntity(
            CreateTaskRequest request
    ) {

        return Task.builder()
                .id(UUID.randomUUID().toString())
                .title(request.getTitle())
                .description(
                        request.getDescription()
                )
                .status(
                        request.getStatus() == null
                                ? TaskStatus.PENDING
                                : request.getStatus()
                )
                .dueDate(
                        request.getDueDate()
                )
                .build();
    }

    public void updateEntity(
            Task task,
            UpdateTaskRequest request
    ) {

        task.setTitle(
                request.getTitle()
        );

        task.setDescription(
                request.getDescription()
        );

        task.setStatus(
                request.getStatus() == null
                        ? TaskStatus.PENDING
                        : request.getStatus()
        );

        task.setDueDate(
                request.getDueDate()
        );
    }

    public TaskResponse toResponse(
            Task task
    ) {

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(
                        task.getDescription()
                )
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .createdAt(
                        task.getCreatedAt()
                )
                .updatedAt(
                        task.getUpdatedAt()
                )
                .build();
    }
}