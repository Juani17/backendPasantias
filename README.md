# Sistema Web OSPUAYE — Backend

API REST para la gestión de reintegros de la **Obra Social de los Profesionales Universitarios del Agua y la Energía Eléctrica (OSPUAYE)**, desarrollada como Práctica Profesionalizante de la Tecnicatura Universitaria en Programación (UTN — Facultad Regional Mendoza).

El sistema centraliza la lógica de negocio, la seguridad, el acceso a los datos y la exposición de los servicios que consume el [frontend de OSPUAYE](https://github.com/Fbarraco1/Ospuaye-Front), permitiendo la gestión digital de solicitudes de reintegro por prestaciones de **Oftalmología** y **Ortopedia**.

---

## 🧩 Stack Tecnológico

- **Java** — Lenguaje principal del backend.
- **Spring Boot** — Framework para el desarrollo de la API REST.
- **Spring Data JPA / Hibernate** — Persistencia y acceso a la base de datos.
- **Spring Security** — Autenticación y autorización de usuarios (JWT).
- **MySQL** — Base de datos relacional.
- **Gradle** — Gestión de dependencias y build del proyecto.
- **Docker** — Contenerización y despliegue en entorno productivo.

---

## 👥 Roles y accesos

| Funcionalidad | Afiliado | Médico Auditor | Administrador |
|---|:---:|:---:|:---:|
| Registro / Login | ✔️ | ✔️ | ✔️ |
| Carga de reintegros | ✔️ | ❌ | ✔️ |
| Subida de documentación | ✔️ | ✔️ (solo lectura) | ✔️ |
| Evaluación médica | ❌ | ✔️ | ❌ |
| Cambio de estado (auditoría) | ❌ | ✔️ | ❌ |
| Aprobación final del reintegro | ❌ | ❌ | ✔️ |
| Registro de pago | ❌ | ❌ | ✔️ |
| Migración del padrón | ❌ | ❌ | ✔️ |
| Administración de usuarios | ❌ | ❌ | ✔️ |

---

## ⚙️ Requisitos previos

- [Java JDK](https://www.oracle.com/java/technologies/downloads/) (versión 17 o superior recomendada)
- [MySQL](https://www.mysql.com/) instalado y corriendo localmente (o acceso a una instancia remota)
- Gradle **no es necesario instalarlo aparte**: el proyecto incluye el Gradle Wrapper (`gradlew` / `gradlew.bat`)
- (Opcional) [Docker](https://www.docker.com/) si se desea levantar el entorno contenerizado

---

## 🚀 Instalación y ejecución en desarrollo

1. **Clonar el repositorio**

   ```bash
   git clone https://github.com/Juani17/backendPasantias.git
   cd backendPasantias/BackendOspuaye
   ```

2. **Configurar la base de datos**

   Crear una base de datos MySQL para el proyecto y configurar las credenciales de conexión en el archivo `src/main/resources/application.properties` (o `application.yml`), por ejemplo:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ospuaye
   spring.datasource.username=root
   spring.datasource.password=tu_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Ejecutar el proyecto con el Gradle Wrapper**

   En Linux / macOS:
   ```bash
   ./gradlew bootRun
   ```

   En Windows:
   ```bash
   gradlew.bat bootRun
   ```

4. **Verificar que la API esté corriendo**

   Por defecto el servidor levanta en `http://localhost:8080` (o el puerto configurado en `application.properties`).

---

## 📦 Build del proyecto

Para generar el `.jar` ejecutable:

```bash
./gradlew build
```

El archivo se genera dentro de `build/libs/`. Para ejecutarlo directamente:

```bash
java -jar build/libs/nombre-del-jar.jar
```

---

## 🐳 Ejecución con Docker

El proyecto puede desplegarse utilizando Docker para asegurar un entorno consistente:

```bash
docker build -t ospuaye-backend .
docker run -p 8080:8080 ospuaye-backend
```

> En el entorno productivo, el backend se despliega junto con la base de datos MySQL dentro de contenedores Docker sobre un servidor **DonWeb (IaaS)**.

---

## 🏗️ Arquitectura

El backend implementa una **arquitectura basada en API REST** bajo el modelo **cliente-servidor**, organizada en capas:

- **Controller** — Expone los endpoints REST consumidos por el frontend.
- **Service** — Contiene la lógica de negocio y las validaciones del sistema.
- **Repository** — Acceso a datos mediante Spring Data JPA.
- **Security** — Autenticación y autorización basada en JWT con Spring Security.

Esta separación mejora la organización del código, la seguridad y facilita el mantenimiento y la escalabilidad de la aplicación.

### Flujo de una solicitud de reintegro

```
Solicitado → Pendiente de Revisión Médica → En Revisión Médica → 
Observado (opcional) → Pendiente de Revisión Administrativa → 
Aprobado / Rechazado → Pagado
```

---

## 📁 Estructura del proyecto (resumen)

```
backendPasantias/
└── BackendOspuaye/
    ├── src/
    │   └── main/
    │       ├── java/          # Código fuente (controllers, services, repositories, entities)
    │       └── resources/     # application.properties, configuración
    ├── build.gradle
    ├── settings.gradle
    ├── gradlew / gradlew.bat
    └── README.md
```

---

## 🌐 Despliegue

El sistema fue desplegado en un entorno productivo utilizando:

- **Servidor:** DonWeb (IaaS sobre Ubuntu Linux)
- **Contenerización:** Docker
- **Base de datos:** MySQL

---

## 👨‍💻 Equipo de desarrollo

Proyecto desarrollado en el marco de la Práctica Profesionalizante — UTN Facultad Regional Mendoza.

- **Francisco Barraco** — Desarrollo Full Stack
- **Juan Emilio Frery** — Desarrollo Full Stack
- **Lic. Leandro Spadaro** — Tutor institucional / Dueño del Producto (OSPUAYE)
- **Ing. Diego Cornejo** — Scrum Master

Metodología de trabajo: **Scrum**, con sprints de 2 semanas, tablero Kanban en Trello y estimaciones mediante Planning Poker.

---

## 🔗 Repositorios relacionados

- Frontend: [OspuayeFront](https://github.com/Fbarraco1/Ospuaye-Front)

---

## 📄 Licencia

Este proyecto fue desarrollado con fines académicos y de práctica profesional para OSPUAYE. Su uso, distribución o reutilización está sujeto a la autorización de la organización y de los autores.
