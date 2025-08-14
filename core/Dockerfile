# Build stage with Maven cache
FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /app

# Mount point for the Maven cache
VOLUME /root/.m2

# Copy POM first to cache dependencies
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Build and verify templates are included
RUN jar tf target/*.jar | grep "templates/auth/login.html"

# Run stage
FROM eclipse-temurin:23-jre-alpine

WORKDIR /app

# Copy the built artifact from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]