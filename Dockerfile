# Use a base image with Java installed
FROM openjdk:17-jdk-alpine

# Add a volume pointing to /tmp
VOLUME /tmp

# Copy the Spring Boot application JAR file to the container
ARG JAR_FILE=target/BKS-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} bksapp.jar
COPY src/main/resources/templates /app/templates
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=postgres
ENV DB_NAME=ebook

# Expose port 8787
EXPOSE 8787

# Set the startup command
ENTRYPOINT ["sh", "-c", "java -jar /bksapp.jar"]

