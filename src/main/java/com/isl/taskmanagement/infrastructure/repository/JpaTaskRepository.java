package com.isl.taskmanagement.infrastructure.repository;

import com.isl.taskmanagement.domain.entity.Task;
import com.isl.taskmanagement.domain.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaTaskRepository
        extends JpaRepository<Task, String> {

    Optional<Task> findByIdAndDeletedFalse(String id);

    List<Task> findByDeletedFalse(Sort sort);

    Page<Task> findByDeletedFalse(Pageable pageable);

    Page<Task> findByStatusAndDeletedFalse(
            TaskStatus status,
            Pageable pageable
    );
}
