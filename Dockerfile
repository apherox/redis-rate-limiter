# Start with a Maven image with JDK 21
FROM maven:3.9.5-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the entire project and build it
COPY src ./src
RUN mvn clean package -DskipTests

# Use a lightweight JRE-based image to run the application
FROM eclipse-temurin:21-jre

# Set the working directory in the container
WORKDIR /app

# Copy the built application from the builder stage
COPY --from=builder /app/target/redis-rate-limiter-1.0-SNAPSHOT.jar /app/redis-rate-limiter.jar

# Copy the entire resources directory to the working directory
COPY src/main/resources /app/resources

# Set the correct permissions for the resources directory
RUN chmod -R 644 /app/resources

# Expose the port that Jetty will run on
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/redis-rate-limiter.jar"]

