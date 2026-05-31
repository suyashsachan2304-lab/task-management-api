package com.isl.taskmanagement.application.service.impl;

import com.isl.taskmanagement.application.dto.request.CreateTaskRequest;
import com.isl.taskmanagement.application.dto.request.UpdateTaskRequest;
import com.isl.taskmanagement.application.dto.response.PagedTaskResponse;
import com.isl.taskmanagement.application.dto.response.TaskResponse;
import com.isl.taskmanagement.application.mapper.TaskMapper;
import com.isl.taskmanagement.application.service.TaskService;
import com.isl.taskmanagement.domain.entity.Task;
import com.isl.taskmanagement.domain.enums.TaskStatus;
import com.isl.taskmanagement.domain.repository.TaskRepository;
import com.isl.taskmanagement.infrastructure.exception.ResourceNotFoundException;
import com.isl.taskmanagement.infrastructure.metrics.TaskMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.List;

@Service
@Transactional
public class TaskServiceImpl
        implements TaskService {

    private static final Logger log =
            LoggerFactory.getLogger(
                    TaskServiceImpl.class
            );

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskMetrics taskMetrics;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            TaskMapper taskMapper,
            TaskMetrics taskMetrics
    ) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.taskMetrics = taskMetrics;
    }

    @Override
    @Caching(
        evict = {
                @CacheEvict(
                        value = "taskList",
                        allEntries = true
                )
        }
    )
    public TaskResponse createTask(
            CreateTaskRequest request
    ) {

        log.info(
                "Creating task with title={}",
                request.getTitle()
        );

        Task task =
                taskMapper.toEntity(request);

        Task saved =
                taskRepository.save(task);

        taskMetrics.incrementTaskCreated();

        log.info(
                "Task created successfully id={}",
                saved.getId()
        );

        return taskMapper.toResponse(
                saved
        );
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
        value = "taskById",
        key = "#taskId"
    )
    public TaskResponse getTaskById(
            String taskId
    ) {

        log.debug(
                "Fetching task id={}",
                taskId
        );

        Task task =
                taskRepository.findById(taskId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Task not found id={}",
                                    taskId
                            );

                            return new ResourceNotFoundException(
                                    "Task not found with id: "
                                            + taskId
                            );
                        });

        log.debug(
                "Task fetched successfully id={}",
                taskId
        );

        return taskMapper.toResponse(
                task
        );
    }

    @Override
    @Caching(
        evict = {
                @CacheEvict(
                        value = "taskById",
                        key = "#taskId"
                ),
                @CacheEvict(
                        value = "taskList",
                        allEntries = true
                )
        }
    )
    public TaskResponse updateTask(
            String taskId,
            UpdateTaskRequest request
    ) {

        log.info(
                "Updating task id={}",
                taskId
        );

        Task task =
                taskRepository.findById(taskId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Task not found for update id={}",
                                    taskId
                            );

                            return new ResourceNotFoundException(
                                    "Task not found with id: "
                                            + taskId
                            );
                        });

        taskMapper.updateEntity(
                task,
                request
        );

        Task saved =
                taskRepository.save(task);

        log.info(
                "Task updated successfully id={}",
                saved.getId()
        );

        return taskMapper.toResponse(
                saved
        );
    }

    @Override
    @Caching(
        evict = {
                @CacheEvict(
                        value = "taskById",
                        key = "#taskId"
                ),
                @CacheEvict(
                        value = "taskList",
                        allEntries = true
                )
        }
    )
    public void deleteTask(
            String taskId
    ) {

        log.info(
                "Deleting task id={}",
                taskId
        );

        Task task =
                taskRepository.findById(taskId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Task not found for delete id={}",
                                    taskId
                            );

                            return new ResourceNotFoundException(
                                    "Task not found with id: "
                                            + taskId
                            );
                        });

        taskRepository.delete(task);

        taskMetrics.incrementTaskDeleted();

        log.info(
                "Task deleted successfully id={}",
                taskId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {

        log.debug(
                "Fetching all tasks"
        );

        List<TaskResponse> tasks =
                taskRepository
                        .findAllByDueDateAsc()
                        .stream()
                        .map(taskMapper::toResponse)
                        .toList();

        log.debug(
                "Fetched {} tasks",
                tasks.size()
        );

        return tasks;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
        value = "taskList",
        key =
                "#page + '-' + #size + '-' + #status + '-' + #sortDirection"
    )
    public PagedTaskResponse getTasks(
            int page,
            int size,
            TaskStatus status,
            String sortDirection
    ) {

        log.debug(
                "Fetching tasks page={}, size={}, status={}, sortDirection={}",
                page,
                size,
                status,
                sortDirection
        );

        Sort.Direction direction =
                "DESC".equalsIgnoreCase(
                        sortDirection
                )
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                direction,
                                "dueDate"
                        )
                );

        Page<Task> taskPage =
                status == null
                        ? taskRepository.findAll(
                                pageable
                        )
                        : taskRepository.findByStatus(
                                status,
                                pageable
                        );

        log.debug(
                "Fetched {} tasks from page {}",
                taskPage.getNumberOfElements(),
                taskPage.getNumber()
        );

        return PagedTaskResponse.builder()
                .content(
                        taskPage.getContent()
                                .stream()
                                .map(
                                        taskMapper::toResponse
                                )
                                .toList()
                )
                .totalElements(
                        taskPage.getTotalElements()
                )
                .totalPages(
                        taskPage.getTotalPages()
                )
                .currentPage(
                        taskPage.getNumber()
                )
                .pageSize(
                        taskPage.getSize()
                )
                .first(
                        taskPage.isFirst()
                )
                .last(
                        taskPage.isLast()
                )
                .build();
    }
}