package com.isl.taskmanagement.infrastructure.repository;

import com.isl.taskmanagement.domain.entity.Task;
import com.isl.taskmanagement.domain.enums.TaskStatus;
import com.isl.taskmanagement.domain.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepositoryAdapter
        implements TaskRepository {

    private final JpaTaskRepository jpaTaskRepository;

    public TaskRepositoryAdapter(
            JpaTaskRepository jpaTaskRepository
    ) {
        this.jpaTaskRepository = jpaTaskRepository;
    }

    @Override
    public Task save(Task task) {
        return jpaTaskRepository.save(task);
    }

    @Override
    public Optional<Task> findById(
            String id
    ) {
        return jpaTaskRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<Task> findAllByDueDateAsc() {
        return jpaTaskRepository.findByDeletedFalse(
                Sort.by(
                        Sort.Direction.ASC,
                        "dueDate"
                )
        );
    }

    @Override
    public Page<Task> findAll(
            Pageable pageable
    ) {
        return jpaTaskRepository.findByDeletedFalse(
                pageable
        );
    }

    @Override
    public Page<Task> findByStatus(
            TaskStatus status,
            Pageable pageable
    ) {
        return jpaTaskRepository.findByStatusAndDeletedFalse(
                status,
                pageable
        );
    }
}
