FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} backendospuaye.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "backendospuaye.jar"]
