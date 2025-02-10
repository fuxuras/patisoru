# Build stage
FROM maven:3.9.9-eclipse-temurin-23-alpine AS build
WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml .
COPY src ./src

# Build and verify templates are included
RUN mvn clean package -DskipTests
RUN jar tf target/*.jar | grep "templates/auth/login.html"

# Run stage
FROM eclipse-temurin:23-jre-noble

WORKDIR /app

# Copy the built artifact from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]