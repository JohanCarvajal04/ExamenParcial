# ---------- Etapa 1: Build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests
RUN jar tf target/*.jar | grep -i flyway || echo ">>> FLYWAY NO ESTÁ EN EL JAR <<<"

# ---------- Etapa 2: Runtime ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Usuario no root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
