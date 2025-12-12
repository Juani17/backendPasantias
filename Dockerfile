FROM eclipse-temurin:17-jdk-jammy

# Crear directorio de trabajo
WORKDIR /app
# Crear directorio para almacenar documentos
RUN mkdir -p /app/documentos && chmod -R 777 /app/documentos
# Copiar el JAR al contenedor
COPY build/libs/BackendOspuaye-0.0.1.jar /app/backendospuaye.jar
# Exponer el puerto
EXPOSE 9000
# Variables de entorno (opcional, también puedes definirlas en docker-compose)
ENV APP_DOCUMENTOS_DIRECTORIO=/app/documentos
# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/backendospuaye.jar"]