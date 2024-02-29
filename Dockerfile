# base image with Java 17 and Maven installed
FROM maven:3.8.4-openjdk-17 AS builder

# working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the Maven project (this will also compile Java files and package the Spring Boot application)
RUN mvn clean package

# Use a base image with Java 17 installed for the final image
FROM openjdk:17-oracle

# Set the working directory in the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
COPY --from=builder /app/target/Spring-Boot-App.jar /app/Spring-Boot-App.jar

# Copy the static files (HTML, CSS, JS) into the container
COPY --from=builder /app/src/main/resources/public /app/public

# Expose the port that the Spring Boot application will run on
EXPOSE 8080

# Define the command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "Spring-Boot-App.jar"]
