FROM eclipse-temurin:17-jdk-jammy
COPY build/libs/BackendOspuaye-0.0.1.jar backendospuaye.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "backendospuaye.jar"]