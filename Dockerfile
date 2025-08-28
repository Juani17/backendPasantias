FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/back_ospuaye-0.0.1.jar
COPY ${JAR_FILE} BackendOspuaye.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "BackendOspuaye.jar"]