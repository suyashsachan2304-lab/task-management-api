package com.isl.taskmanagement.domain.repository;

import com.isl.taskmanagement.domain.entity.Task;
import com.isl.taskmanagement.domain.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(String id);

    void delete(Task task);

    List<Task> findAllByDueDateAsc();

    Page<Task> findAll(Pageable pageable);

    Page<Task> findByStatus(
            TaskStatus status,
            Pageable pageable
    );
}