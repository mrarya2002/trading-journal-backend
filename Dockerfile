# Use Maven image to build the JAR
FROM maven:3.9.4-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the JAR file
RUN mvn clean package -DskipTests

# -----------------------------
# Second stage: Run the app
# -----------------------------
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the JAR from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
