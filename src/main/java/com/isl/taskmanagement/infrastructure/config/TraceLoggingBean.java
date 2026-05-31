package com.isl.taskmanagement.infrastructure.config;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;

public class TraceLoggingBean {

    private final Tracer tracer;

    public TraceLoggingBean(
            Tracer tracer
    ) {
        this.tracer = tracer;
    }

    public String getCurrentTraceId() {

        Span span =
                tracer.currentSpan();

        if (span == null) {
            return null;
        }

        return span.context()
                .traceId();
    }

    public String getCurrentSpanId() {

        Span span =
                tracer.currentSpan();

        if (span == null) {
            return null;
        }

        return span.context()
                .spanId();
    }
}