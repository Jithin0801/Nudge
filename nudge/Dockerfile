# Stage 1: Build the application
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copy the maven wrapper and pom.xml first
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Make the wrapper executable
RUN chmod +x mvnw

# Download dependencies (this layer will be cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy the built artifact from the build stage
COPY --from=build /app/target/nudge-1.0.0.war app.war

# Default Environment Variables (these can be overridden when running the container)
ENV DB_URL=jdbc:postgresql://localhost:5432/nudge
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=jithu
ENV ALLOWED_ORIGINS=http://localhost:3000
ENV JWT_KEYSTORE_PASSWORD=jithu123
ENV JWT_KEY_ALIAS=nudge
ENV JWT_KEY_PASSWORD=jithu123

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.war"]
