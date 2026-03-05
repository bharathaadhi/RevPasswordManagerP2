# Use Java 17 runtime
FROM eclipse-temurin:17-jdk

# Create working directory
WORKDIR /app

# Copy jar file into container
COPY target/revpasswordmanagerP2-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run application
CMD ["java", "-jar", "app.jar"]