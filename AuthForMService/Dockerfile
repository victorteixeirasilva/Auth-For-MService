FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 2723

ENTRYPOINT ["java", "-jar", "app.jar"]