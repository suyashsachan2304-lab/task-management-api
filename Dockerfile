# ======================================
# Build Stage
# ======================================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ======================================
# Runtime Stage
# ======================================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder \
/workspace/target/task-management-1.0.0.jar \
app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s \
            --timeout=10s \
            --start-period=40s \
            --retries=5 \
CMD wget --quiet --tries=1 --spider \
http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-jar","app.jar"]