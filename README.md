# Task Management REST API

## Overview

Production-grade Task Management REST API built using modern Spring Boot practices.

The application demonstrates:

* RESTful API Design
* Domain Driven Design (DDD)
* SOLID Principles
* Clean Architecture
* Layered Separation of Concerns
* Observability and Distributed Tracing
* Production-Oriented Logging and Metrics

---

# Features

## Core Features

* Create Task
* Get Task By ID
* Update Task
* Delete Task
* Pagination
* Filtering by Status
* Sorting by Due Date

## Validation

* Request Validation
* Enum Validation
* Pagination Validation
* Consistent Error Responses

## Architecture

* DDD Structure
* Constructor Injection
* DTO Pattern
* Service Layer Abstraction
* Repository Abstraction

## Observability

* Correlation ID Logging
* Micrometer Metrics
* Prometheus Integration
* Spring Boot Actuator
* Distributed Tracing with Zipkin

## API Enhancements

* API Versioning (`/api/v1`)
* Standard API Response Wrapper
* Swagger/OpenAPI Documentation

## Testing

* Unit Tests
* Integration Tests
* MockMvc Testing
* Mockito

## Deployment

* Docker Support
* Docker Compose Support

---

# Architecture

```text
Client
  │
  ▼
Controller Layer
  │
  ▼
Service Layer
  │
  ▼
Mapper Layer
  │
  ▼
Repository Layer
  │
  ▼
Database
```

Package Structure:

```text
com.isl.taskmanagement
├── application
│   ├── dto
│   ├── mapper
│   └── service
│
├── domain
│   ├── entity
│   ├── enums
│   └── repository
│
├── infrastructure
│   ├── config
│   ├── exception
│   ├── metrics
│   ├── response
│   └── tracing
│
├── interfaces
│   └── controller
│
└── TaskManagementApplication
```

---

# Technology Stack

| Technology      | Version              |
| --------------- | -------------------- |
| Java            | 21                   |
| Spring Boot     | 3.5                  |
| Spring Data JPA | Latest               |
| Hibernate       | 6.x                  |
| H2 Database     | Latest               |
| Maven           | 3.9+                 |
| Swagger/OpenAPI | Springdoc            |
| Actuator        | Spring Boot Actuator |
| Micrometer      | Latest               |
| Prometheus      | Latest               |
| Brave           | Latest               |
| Zipkin          | Latest               |
| Docker          | Latest               |
| Docker Compose  | Latest               |
| JUnit 5         | Latest               |
| Mockito         | Latest               |
| MockMvc         | Latest               |

---

# API Versioning

All APIs are exposed through:

```text
/api/v1/tasks
```

---

# Task Model

```json
{
  "id": "4b8f4a65-cfde-4b2d-bf2c-c6dd42e4b18f",
  "title": "Learn Spring Boot",
  "description": "Build production-grade REST API",
  "status": "PENDING",
  "dueDate": "2030-06-01",
  "createdAt": "2026-05-31T20:00:00",
  "updatedAt": "2026-05-31T20:00:00"
}
```

---

# Status Values

```text
PENDING
IN_PROGRESS
DONE
```

---

# Standard Success Response

```json
{
  "timestamp": "2026-05-31T20:00:00",
  "status": 200,
  "message": "Task retrieved successfully",
  "data": {}
}
```

---

# Error Response Format

```json
{
  "timestamp": "2026-05-31T20:00:00",
  "status": 404,
  "message": "Task not found",
  "path": "/api/v1/tasks/123",
  "correlationId": "1748770951456"
}
```

---

# Correlation ID

Every request is assigned a correlation identifier.

Header:

```text
X-Correlation-Id
```

Behavior:

* Existing value is propagated
* New value generated when absent
* Returned in response headers
* Added to application logs

Example:

```text
correlationId=1748770951456
```

---

# Distributed Tracing

The application uses:

* Micrometer Observation
* Micrometer Tracing
* Brave
* Zipkin

Every request automatically receives:

```text
traceId
spanId
correlationId
```

Example:

```text
traceId=6a1c4c089ca08ce15584cd27c0ccbeb9
spanId=5584cd27c0ccbeb9
correlationId=1780239368913
```

---

# Caching

Spring Cache is enabled.

Cached Operations:

* Get Task By Id
* Task Listing

Cache Eviction:

* Create Task
* Update Task
* Delete Task

Implementation:

```java
@EnableCaching
```

---

# Local Setup

## Prerequisites

* Java 21
* Maven 3.9+
* Docker Desktop

Verify:

```bash
java -version
mvn -version
docker --version
```

---

# Clone Repository

```bash
git clone https://github.com/<your-username>/task-management-api.git

cd task-management-api
```

---

# Build

```bash
mvn clean install
```

---

# Run Locally

```bash
mvn spring-boot:run
```

Application:

```text
http://localhost:8080
```

---

# Running Tests

```bash
mvn test
```

Full verification:

```bash
mvn verify
```

---

# Docker

Build image:

```bash
docker build -t task-management .
```

Run:

```bash
docker run -p 8080:8080 task-management
```

---

# Docker Compose

Start Application + Zipkin:

```bash
docker compose up -d
```

Stop:

```bash
docker compose down
```

---

# Swagger UI

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI Specification:

```text
http://localhost:8080/v3/api-docs
```

---

# H2 Console

```text
http://localhost:8080/h2-console
```

Connection:

```text
JDBC URL: jdbc:h2:mem:taskdb
Username: sa
Password:
```

---

# Actuator

Health:

```text
/actuator/health
```

Metrics:

```text
/actuator/metrics
```

Prometheus:

```text
/actuator/prometheus
```

---

# Zipkin

Start using Docker Compose:

```bash
docker compose up -d
```

Open:

```text
http://localhost:9411
```

Capabilities:

* Request Tracing
* Latency Analysis
* Service Dependency Visualization
* Trace Search
* Span Inspection

---

# Sample API Calls

## Create Task

```bash
curl --location 'http://localhost:8080/api/v1/tasks' \
--header 'Content-Type: application/json' \
--data '{
"title":"Learn Spring Boot",
"description":"Build Task API",
"status":"PENDING",
"dueDate":"2030-06-01"
}'
```

## Get Task

```bash
curl http://localhost:8080/api/v1/tasks/{id}
```

## Update Task

```bash
curl --request PUT \
'http://localhost:8080/api/v1/tasks/{id}'
```

## Delete Task

```bash
curl --request DELETE \
'http://localhost:8080/api/v1/tasks/{id}'
```

---

# Future Enhancements

* PostgreSQL
* JWT Authentication
* Role Based Access Control
* OpenTelemetry
* Kubernetes Deployment
* GitHub Actions CI/CD
* Elasticsearch
* Multi-Tenant Support

---