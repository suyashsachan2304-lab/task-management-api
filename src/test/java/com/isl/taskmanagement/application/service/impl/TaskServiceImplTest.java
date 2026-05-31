package com.isl.taskmanagement.application.service.impl;

import com.isl.taskmanagement.application.dto.request.CreateTaskRequest;
import com.isl.taskmanagement.application.dto.request.UpdateTaskRequest;
import com.isl.taskmanagement.application.dto.response.PagedTaskResponse;
import com.isl.taskmanagement.application.dto.response.TaskResponse;
import com.isl.taskmanagement.application.mapper.TaskMapper;
import com.isl.taskmanagement.domain.entity.Task;
import com.isl.taskmanagement.domain.enums.TaskStatus;
import com.isl.taskmanagement.domain.repository.TaskRepository;
import com.isl.taskmanagement.infrastructure.exception.ResourceNotFoundException;
import com.isl.taskmanagement.infrastructure.metrics.TaskMetrics;

import com.isl.taskmanagement.application.service.impl.TaskServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskMetrics taskMetrics;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskResponse taskResponse;
    private CreateTaskRequest createRequest;
    private UpdateTaskRequest updateRequest;

    private static final String TASK_ID =
            UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {

        task = Task.builder()
                .id(TASK_ID)
                .title("Complete Spring Boot")
                .description("Finish task management api")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        taskResponse = TaskResponse.builder()
                .id(TASK_ID)
                .title("Complete Spring Boot")
                .description("Finish task management api")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        createRequest = CreateTaskRequest.builder()
                .title("Complete Spring Boot")
                .description("Finish task management api")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        updateRequest = UpdateTaskRequest.builder()
                .title("Updated Task")
                .description("Updated Description")
                .status(TaskStatus.IN_PROGRESS)
                .dueDate(LocalDate.now().plusDays(10))
                .build();
    }

    @Test
    @DisplayName("Should create task successfully")
    void createTaskSuccess() {

        when(taskMapper.toEntity(createRequest))
                .thenReturn(task);

        when(taskRepository.save(task))
                .thenReturn(task);

        when(taskMapper.toResponse(task))
                .thenReturn(taskResponse);

        TaskResponse response =
                taskService.createTask(createRequest);

        assertNotNull(response);
        assertEquals(TASK_ID, response.getId());
        assertEquals("Complete Spring Boot",
                response.getTitle());

        verify(taskRepository).save(task);
        verify(taskMetrics).incrementTaskCreated();
    }

    @Test
    @DisplayName("Should get task by id successfully")
    void getTaskByIdSuccess() {

        when(taskRepository.findById(TASK_ID))
                .thenReturn(Optional.of(task));

        when(taskMapper.toResponse(task))
                .thenReturn(taskResponse);

        TaskResponse response =
                taskService.getTaskById(TASK_ID);

        assertNotNull(response);
        assertEquals(TASK_ID, response.getId());

        verify(taskRepository)
                .findById(TASK_ID);
    }

    @Test
    @DisplayName("Should throw exception when task not found")
    void getTaskByIdNotFound() {

        when(taskRepository.findById(TASK_ID))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> taskService.getTaskById(TASK_ID)
                );

        assertTrue(
                exception.getMessage()
                        .contains("Task not found")
        );
    }

    @Test
    @DisplayName("Should update task successfully")
    void updateTaskSuccess() {

        Task updatedTask = Task.builder()
                .id(TASK_ID)
                .title("Updated Task")
                .description("Updated Description")
                .status(TaskStatus.IN_PROGRESS)
                .dueDate(LocalDate.now().plusDays(10))
                .build();

        TaskResponse updatedResponse =
                TaskResponse.builder()
                        .id(TASK_ID)
                        .title("Updated Task")
                        .description("Updated Description")
                        .status(TaskStatus.IN_PROGRESS)
                        .dueDate(LocalDate.now().plusDays(10))
                        .build();

        when(taskRepository.findById(TASK_ID))
                .thenReturn(Optional.of(task));

        doNothing()
                .when(taskMapper)
                .updateEntity(task, updateRequest);

        when(taskRepository.save(task))
                .thenReturn(updatedTask);

        when(taskMapper.toResponse(updatedTask))
                .thenReturn(updatedResponse);

        TaskResponse response =
                taskService.updateTask(
                        TASK_ID,
                        updateRequest
                );

        assertNotNull(response);
        assertEquals(
                TaskStatus.IN_PROGRESS,
                response.getStatus()
        );

        verify(taskRepository)
                .save(task);
    }

    @Test
    @DisplayName("Should throw exception when updating missing task")
    void updateTaskNotFound() {

        when(taskRepository.findById(TASK_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.updateTask(
                        TASK_ID,
                        updateRequest
                )
        );
    }

    @Test
    @DisplayName("Should delete task successfully")
    void deleteTaskSuccess() {

        when(taskRepository.findById(TASK_ID))
                .thenReturn(Optional.of(task));

        doNothing()
                .when(taskRepository)
                .delete(task);

        assertDoesNotThrow(
                () -> taskService.deleteTask(TASK_ID)
        );

        verify(taskRepository)
                .delete(task);

        verify(taskMetrics)
                .incrementTaskDeleted();
    }

    @Test
    @DisplayName("Should throw exception when deleting missing task")
    void deleteTaskNotFound() {

        when(taskRepository.findById(TASK_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.deleteTask(TASK_ID)
        );
    }

    @Test
    @DisplayName("Should return all tasks sorted by due date")
    void getAllTasksSuccess() {

        List<Task> tasks = List.of(task);

        when(taskRepository.findAllByDueDateAsc())
            .thenReturn(tasks);

        when(taskMapper.toResponse(task))
            .thenReturn(taskResponse);

        List<TaskResponse> responses =
            taskService.getAllTasks();

        assertEquals(1, responses.size());
        assertEquals(TASK_ID, responses.get(0).getId());

        verify(taskRepository)
            .findAllByDueDateAsc();
    }

    @Test
    @DisplayName("Should return paginated tasks")
    void getTasksPaginationSuccess() {

        Pageable pageable =
                PageRequest.of(
                        0,
                        10,
                        Sort.by(
                                Sort.Direction.ASC,
                                "dueDate"
                        )
                );

        Page<Task> page =
                new PageImpl<>(
                        List.of(task),
                        pageable,
                        1
                );

        when(taskRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        when(taskMapper.toResponse(task))
                .thenReturn(taskResponse);

        PagedTaskResponse response =
                taskService.getTasks(
                        0,
                        10,
                        null,
                        "ASC"
                );

        assertNotNull(response);
        assertEquals(1,
                response.getContent().size());

        assertEquals(
                1,
                response.getTotalElements()
        );
    }

    @Test
    @DisplayName("Should return paginated tasks filtered by status")
    void getTasksFilterByStatusSuccess() {

        Pageable pageable =
                PageRequest.of(
                        0,
                        10,
                        Sort.by(
                                Sort.Direction.ASC,
                                "dueDate"
                        )
                );

        Page<Task> page =
                new PageImpl<>(
                        List.of(task),
                        pageable,
                        1
                );

        when(taskRepository.findByStatus(
            eq(TaskStatus.PENDING),
            any(Pageable.class)
        )).thenReturn(page);

        when(taskMapper.toResponse(task))
                .thenReturn(taskResponse);

        PagedTaskResponse response =
                taskService.getTasks(
                        0,
                        10,
                        TaskStatus.PENDING,
                        "ASC"
                );

        assertNotNull(response);
        assertEquals(1,
                response.getContent().size());

        assertEquals(
                TaskStatus.PENDING,
                response.getContent()
                        .get(0)
                        .getStatus()
        );
    }

    @Test
    @DisplayName("Should use descending sorting")
    void getTasksDescendingSortSuccess() {

        Page<Task> page =
                new PageImpl<>(List.of(task));

        when(taskRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        when(taskMapper.toResponse(task))
                .thenReturn(taskResponse);

        PagedTaskResponse response =
                taskService.getTasks(
                        0,
                        10,
                        null,
                        "DESC"
                );

        assertNotNull(response);

        verify(taskRepository)
                .findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should default status to pending during create")
    void createTaskDefaultStatusPending() {

        CreateTaskRequest request =
                CreateTaskRequest.builder()
                        .title("Task")
                        .description("Desc")
                        .dueDate(
                                LocalDate.now()
                                        .plusDays(1)
                        )
                        .build();

        Task pendingTask =
                Task.builder()
                        .id(TASK_ID)
                        .title("Task")
                        .status(TaskStatus.PENDING)
                        .dueDate(
                                LocalDate.now()
                                        .plusDays(1)
                        )
                        .build();

        TaskResponse response =
                TaskResponse.builder()
                        .id(TASK_ID)
                        .status(TaskStatus.PENDING)
                        .build();

        when(taskMapper.toEntity(request))
                .thenReturn(pendingTask);

        when(taskRepository.save(pendingTask))
                .thenReturn(pendingTask);

        when(taskMapper.toResponse(pendingTask))
                .thenReturn(response);

        TaskResponse result =
                taskService.createTask(request);

        assertEquals(
                TaskStatus.PENDING,
                result.getStatus()
        );
    }
}