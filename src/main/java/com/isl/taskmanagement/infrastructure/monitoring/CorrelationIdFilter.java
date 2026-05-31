package com.isl.taskmanagement.infrastructure.monitoring;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CorrelationIdFilter
        extends OncePerRequestFilter {

    public static final String CORRELATION_ID =
            "correlationId";

    public static final String HEADER_NAME =
            "X-Correlation-Id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String correlationId =
                String.valueOf(
                        System.currentTimeMillis()
                );

        try {

            MDC.put(
                    CORRELATION_ID,
                    correlationId
            );

            request.setAttribute(
                    CORRELATION_ID,
                    correlationId
            );

            response.setHeader(
                    HEADER_NAME,
                    correlationId
            );

            filterChain.doFilter(
                    request,
                    response
            );

        } finally {

            MDC.remove(
                    CORRELATION_ID
            );
        }
    }
}