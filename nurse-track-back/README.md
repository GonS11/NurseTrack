# NurseTrack Backend

The **NurseTrack Backend** is the foundational engine of the NurseTrack application, providing the robust RESTful API that powers all functionalities related to managing nursing shifts, schedules, and vacations. Built with enterprise-grade Java technologies, it ensures secure, scalable, and efficient data handling.

---

## 🚀 **Project Overview**

This repository contains the backend application for NurseTrack. It handles all business logic, data persistence, and API endpoints, serving as the central hub for the frontend and any other potential clients. It's designed to be reliable and performant, managing complex data interactions and user authentication.

---

## ✨ **Key Features & Technologies**

The backend is developed using a powerful and well-established technology stack, ensuring stability, security, and maintainability:

-   **[Spring Boot](https://spring.io/projects/spring-boot):** The leading framework for building standalone, production-ready Spring applications. It simplifies the setup of robust RESTful APIs with its convention-over-configuration approach.
-   **[MySQL](https://www.mysql.com/):** A widely used open-source relational database management system that serves as the primary data store for all application data, including users, shifts, and requests.
-   **[Spring Data JPA](https://spring.io/projects/spring-data-jpa):** Part of the Spring Data family, it simplifies database access layers by reducing boilerplate code and providing powerful repository abstractions.
-   **[Hibernate](https://hibernate.org/):** The most popular Object-Relational Mapping (ORM) framework in Java, used by Spring Data JPA to map Java objects to database tables.
-   **[Spring Security](https://spring.io/projects/spring-security):** A robust and highly customizable authentication and access-control framework. It's tightly integrated with **JWT (JSON Web Tokens)** for stateless, token-based authentication and authorization.
-   **[MapStruct](https://mapstruct.org/):** A code generator that greatly simplifies the implementation of data object mappings (e.g., between entities and DTOs) by automating the process and generating highly performant code.
-   **[Lombok](https://projectlombok.org/):** A library that helps reduce boilerplate code for Java classes (e.g., getters, setters, constructors) through annotations, making code cleaner and more concise.

---

## 📁 **Project Structure**

The backend application follows a standard layered architecture, promoting clear separation of concerns:

-   **`src/main/java/com/nursetrack`:** The root package for the backend Java source code.
    -   **`config/`:** Contains core configuration classes, including CORS settings, Spring Security configurations, and application-specific properties.
    -   **`controller/`:** Defines the RESTful API endpoints, handling incoming HTTP requests and delegating business logic to service layers.
    -   **`dto/`:** Houses **Data Transfer Objects (DTOs)**, which are used to define the structure of data sent to and from the API, ensuring clean data contracts.
    -   **`entity/`:** Contains **JPA Entities**, representing the database tables and defining the application's domain model.
    -   **`exception/`:** Custom exception classes for handling specific application errors.
    -   **`mapper/`:** MapStruct mappers responsible for converting between Entity and DTO objects.
    -   **`repository/`:** Defines Spring Data JPA repositories for performing CRUD (Create, Read, Update, Delete) operations on entities.
    -   **`security/`:** Classes related to authentication and authorization, including JWT token handling and user details services.
    -   **`service/`:** Implements the core business logic, orchestrating operations and interacting with repositories.
    -   **`util/`:** General utility classes.
    -   **`NurseTrackApplication.java`:** The main Spring Boot application class, responsible for bootstrapping the application.

---

## 🚀 **Getting Started**

To run the NurseTrack Backend locally, it's designed to be easily managed with Docker Compose, which also sets up its MySQL database dependency.

### **Prerequisites:**

-   **Java Development Kit (JDK)** (v21) - *Required if you want to run/debug outside of Docker or build the JAR manually.*
-   **Docker** and **Docker Compose** (Docker Desktop or a similar Docker environment is recommended).

### **Steps to Run:**

1.  **Clone the main repository:**
    If you haven't already, clone the NurseTrack GitHub repository and navigate to its root directory.

    ```bash
    git clone [https://github.com/GonS11/NurseTrack.git](https://github.com/GonS11/NurseTrack.git)
    cd NurseTrack
    ```

2.  **Start Backend and Database Services (using Docker Compose):**
    This single command will build the Docker image for the backend application (if not already built), create the necessary containers, and start both the Spring Boot backend and the MySQL database in detached mode (`-d`).

    ```bash
    docker-compose up -d
    ```

3.  **Verify Backend Readiness:**
    It's crucial to wait for the backend service to be fully operational before the frontend attempts to connect. You can monitor its health by accessing the Spring Boot Actuator endpoint in your browser or via `curl`.

    ```bash
    # Open this URL in your browser:
    http://localhost:8080/actuator/health
    # Or use curl from your terminal:
    curl http://localhost:8080/actuator/health
    ```
    The backend is ready when the response displays: `"status": "UP"`.

4.  **Access the Backend API:**
    Once the backend is running, its API will be accessible at:

    ```
    http://localhost:8080
    ```
    You can then proceed to set up and run the [NurseTrack Frontend](https://github.com/GonS11/NurseTrack/tree/main/nurse-track-frontend) to interact with this API.

---

## ⚙️ **Configuration**

Backend configurations are primarily managed in `application.properties` (or `application.yml`) files within `src/main/resources/`. Database connection details and other environment-specific settings are handled via environment variables, typically configured in your `docker-compose.yml` for local development.

---

## 📦 **Building the Project (Optional, for JAR/War)**

If you need to build the Spring Boot application into a standalone JAR file (e.g., for direct deployment without Docker), you can use Maven (assuming you have it installed).

1.  **Navigate to the Backend Directory:**
    ```bash
    cd NurseTrack # (If you are in the root)
    # You might need to navigate into the specific backend folder if your project structure dictates it,
    # e.g., cd nurse-track-backend (if it's a multi-module project)
    ```

2.  **Build with Maven:**
    ```bash
    ./mvnw clean install # On Linux/macOS
    # Or for Windows:
    # mvnw clean install
    ```
    This will generate a JAR file in the `target/` directory.
