# Build stage with Java 17
# Build stage with Maven and OpenJDK 17
FROM maven:3.9.4-openjdk-17-slim AS build
WORKDIR /app

# Copy your POM and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage with OpenJDK 17
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/nomPrenomClasseExamen-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

