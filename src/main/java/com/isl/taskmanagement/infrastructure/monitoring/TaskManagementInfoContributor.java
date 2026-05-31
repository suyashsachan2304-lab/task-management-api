package com.isl.taskmanagement.infrastructure.monitoring;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class TaskManagementInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {

        builder.withDetail(
                "application",
                Map.of(
                        "name", "Task Management API",
                        "version", "1.0.0",
                        "javaVersion", "21",
                        "framework", "Spring Boot 3",
                        "timestamp", LocalDateTime.now().toString()
                )
        );
    }
}