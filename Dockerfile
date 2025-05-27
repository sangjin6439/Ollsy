FROM openjdk:17-jdk

COPY build/libs/*SNAPSHOT.jar /app.jar
COPY .env .env

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]