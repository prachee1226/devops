# Multi-Stage Production Dockerfile for SmartDispatch 108
# Student: Prachee Wakode (23102A0020)

# ==========================================
# STAGE 1: Build Application with Maven
# ==========================================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Maven POM and source files
COPY pom.xml .
COPY src ./src

# Install Maven in Alpine builder
RUN apk add --no-cache maven && \
    mvn clean package -DskipTests=true

# ==========================================
# STAGE 2: Lightweight Runtime Container
# ==========================================
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="Prachee Wakode <23102A0020@student.institution.edu>"
LABEL description="SmartDispatch 108 Ambulance Operations Container"
LABEL version="1.0.0"

# Create dedicated non-root service user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /home/spring

# Copy repackaged JAR from builder stage
COPY --from=builder /app/target/ambulance-dispatch-dashboard-1.0.0-SNAPSHOT.jar app.jar

# Expose HTTP Port
EXPOSE 8080

# Health Check Directive
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Environment configurations
ENV SERVER_PORT=8080 \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Run Spring Boot Application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
