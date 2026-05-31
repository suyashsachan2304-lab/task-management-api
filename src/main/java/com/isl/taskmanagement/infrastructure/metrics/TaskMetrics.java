package com.isl.taskmanagement.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class TaskMetrics {

    private final Counter taskCreatedCounter;
    private final Counter taskDeletedCounter;

    public TaskMetrics(MeterRegistry meterRegistry) {

        this.taskCreatedCounter = Counter.builder("task.created.count")
                .description("Total number of created tasks")
                .register(meterRegistry);

        this.taskDeletedCounter = Counter.builder("task.deleted.count")
                .description("Total number of deleted tasks")
                .register(meterRegistry);
    }

    public void incrementTaskCreated() {
        taskCreatedCounter.increment();
    }

    public void incrementTaskDeleted() {
        taskDeletedCounter.increment();
    }
}