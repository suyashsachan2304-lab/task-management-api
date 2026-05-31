package com.isl.taskmanagement.infrastructure.config;

import io.micrometer.tracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

    @Bean
    public TraceLoggingBean traceLoggingBean(
            Tracer tracer
    ) {
        return new TraceLoggingBean(
                tracer
        );
    }
}