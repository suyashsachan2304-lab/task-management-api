package com.isl.taskmanagement.application.dto.response;

import com.isl.taskmanagement.domain.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponse {

    private String id;

    private String title;

    private String description;

    private TaskStatus status;

    private LocalDate dueDate;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

}