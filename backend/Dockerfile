FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /data
COPY --from=build /app/target/taskeasy-1.0.0.jar /app/taskeasy.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/taskeasy.jar", "--taskeasy.file=/data/tasks.json"]
