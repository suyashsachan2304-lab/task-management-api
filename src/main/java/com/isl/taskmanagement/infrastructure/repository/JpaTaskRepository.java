package com.isl.taskmanagement.infrastructure.repository;

import com.isl.taskmanagement.domain.entity.Task;
import com.isl.taskmanagement.domain.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTaskRepository
        extends JpaRepository<Task, String> {

    Page<Task> findByStatus(
            TaskStatus status,
            Pageable pageable
    );
}