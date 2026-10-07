# OSPUAYE — Backend de práctica profesionalizante

API REST desarrollada en el marco de la Tecnicatura Universitaria en Programación de la UTN, Facultad Regional Mendoza, para la experiencia de pasantía con OSPUAYE. Incluye gestión de beneficiarios, grupos familiares, usuarios y solicitudes de prestaciones de oftalmología y ortopedia.

## Tecnologías

Java 17, Spring Boot 3.5.3, Spring Web, Spring Data JPA/Hibernate, Spring Security, JWT (JJWT), MySQL, Gradle y Lombok. Las versiones y dependencias se encuentran en `build.gradle`.

## Alcance implementado

- Registro e inicio de sesión; emisión y validación de JWT.
- Controladores REST para usuarios, beneficiarios, médicos, roles y grupos familiares.
- Modelos y servicios para pedidos de oftalmología y ortopedia, documentación e historial de movimientos.
- Persistencia relacional mediante repositorios JPA.

El repositorio representa la implementación de una práctica profesionalizante; no constituye una garantía de preparación para producción.

## Estructura

La raíz contiene un proyecto Gradle completo: `build.gradle`, `gradlew`, `gradle/` y `src/`. En `src/main/java/` se encuentran controladores, servicios, repositorios, entidades y seguridad.

También existe otro proyecto Gradle dentro de `BackendOspuaye/`. Comparte gran parte del código con la raíz, pero difiere en el manejo de documentos y en su configuración. Se conserva su estructura original; las instrucciones siguientes corresponden al proyecto de la raíz.

## Ejecución local

Requisitos: JDK 17, MySQL y una base local de desarrollo. El Gradle Wrapper está incluido. Usar únicamente datos de prueba.

```bash
git clone https://github.com/Juani17/backendPasantias.git
cd backendPasantias
```

Configurar la conexión mediante las variables de entorno estándar de Spring Boot `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD`. La URL debe usar el formato JDBC de MySQL y apuntar a la base local. Exportar estas variables en la terminal o configurarlas en el IDE; este proyecto no carga automáticamente archivos `.env`.

```bash
./gradlew bootRun
```

En PowerShell: `.\gradlew.bat bootRun`. Consultar `src/main/resources/application.properties` para los demás ajustes. La configuración original puede actualizar el esquema mediante Hibernate: utilizar una base descartable de desarrollo.

## Repositorios relacionados

- [Frontend OSPUAYE](https://github.com/Fbarraco1/Ospuaye-Front).
- [backendOspuaye](https://github.com/Juani17/backendOspuaye): variante con código propio y archivos de despliegue Docker. No es una copia idéntica de este repositorio.

## Equipo y contexto

Desarrollo: Francisco Barraco y Juan Emilio Frery. Proyecto de práctica profesionalizante UTN–OSPUAYE. Se conserva el contexto y la autoría de la documentación original.

El uso, distribución o reutilización del proyecto está sujeto a la autorización de la organización y de sus autores.
