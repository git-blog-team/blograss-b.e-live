FROM openjdk:17-jdk-slim AS builder
COPY blograss-b.e-live/gradlew .
COPY blograss-b.e-live/gradle gradle
COPY blograss-b.e-live/build.gradle .
COPY blograss-b.e-live/settings.gradle .
COPY blograss-b.e-live/src src
COPY application.properties application.properties
RUN chmod +x ./gradlew
RUN apt-get update && apt-get install -y findutils
RUN ./gradlew bootJar

FROM openjdk:17-jdk-slim
COPY --from=builder build/libs/*.jar app.jar
COPY --from=builder application.properties application.properties

EXPOSE 8080
ENTRYPOINT ["java","-Dspring.config.location=file:/application.properties","-jar","/app.jar"]