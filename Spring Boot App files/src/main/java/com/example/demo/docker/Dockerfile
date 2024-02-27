# Use a base image with Java 17 installed
FROM openjdk:17-oracle

# Set the working directory in the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
COPY Spring-Boot-App.jar /app

# Expose the port that the Spring Boot application will run on
EXPOSE 8080

# Define the command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "Spring-Boot-App.jar"]
