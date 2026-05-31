package com.isl.taskmanagement.infrastructure.response;

import org.slf4j.MDC;

import java.time.LocalDateTime;

public class ApiResponse<T> {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;
    private String correlationId;

    public ApiResponse() {
    }

    public ApiResponse(
            LocalDateTime timestamp,
            int status,
            String message,
            T data,
            String correlationId
    ) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.data = data;
        this.correlationId = correlationId;
    }

    public static <T> ApiResponse<T> of(
            int status,
            String message,
            T data
    ) {
        return new ApiResponse<>(
                LocalDateTime.now(),
                status,
                message,
                data,
                MDC.get("correlationId")
        );
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getCorrelationId() {
        return correlationId;
    }
}