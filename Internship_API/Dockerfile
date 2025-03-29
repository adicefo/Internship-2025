# Use OpenJDK as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the target directory to the container
COPY target/Internship_API-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8081 to the outside world
EXPOSE 8081

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]