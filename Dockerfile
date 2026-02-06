# Multi-stage Dockerfile for Maven + Spring Boot (JDK 21)
# Builder stage: uses Maven with Eclipse Temurin JDK 21 to build the project
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Copy only the pom first for better caching
COPY pom.xml ./
# Copy source
COPY src ./src

# Build the application (skip tests to keep build fast; change if you want tests)
RUN mvn -B -DskipTests package

# Runtime stage: lightweight JRE to run the Spring Boot jar
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the built jar from the builder stage. Uses a wildcard so it works with SNAPSHOT/versioned names
COPY --from=builder /workspace/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]