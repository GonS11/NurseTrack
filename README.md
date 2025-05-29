# NurseTrack

**NurseTrack** is a robust web application engineered to streamline the intricate management of shifts, schedules, and vacations for nursing staff. Our goal is to provide an intuitive and efficient platform that optimizes planning and organization within healthcare environments, empowering users to easily view their schedules and submit vacation requests.

---

## 🚀 **Project Description**

NurseTrack serves as a pivotal tool for enhancing time management and fostering seamless communication across healthcare teams. It offers administrators powerful capabilities to oversee nursing staff schedules, while nurses benefit from easy access to their shifts, the ability to request changes, and comprehensive vacation management.

### **Key Features:**

-   **Shift Management:** Intuitive tools to view, assign, and modify shifts for nursing personnel.
-   **Vacation Requests:** A simplified process for nurses to request and manage their vacation days, with clear approval workflows.
-   **Shift Swaps:** Integrated functionality allowing colleagues to propose and arrange shift exchanges.
-   **Role-Based Dashboards:** Customized interfaces providing relevant information and functionalities tailored to each user's role (administrator, supervisor, or nurse).
-   **Notifications:** Real-time alerts for crucial updates, including status changes in requests and schedule modifications.

---

## 🛠️ **Technologies Used**

NurseTrack leverages a modern, robust, and scalable technology stack to ensure efficient development, high performance, and a seamless user experience.

### **Backend:**

-   **[Spring Boot](https://spring.io/projects/spring-boot):** The leading framework for building high-performance, production-ready RESTful applications in Java.
-   **MySQL:** A reliable relational database management system, essential for storing and managing critical data such as user profiles, shift assignments, and request details.
-   **JWT (JSON Web Tokens):** Implemented for secure and stateless user authentication and authorization across the application.

### **Frontend:**

-   **[Vue 3](https://vuejs.org/):** A progressive JavaScript framework renowned for building interactive and high-performance user interfaces with its intuitive API and robust ecosystem.
-   **TypeScript:** Enhances frontend development with static type checking, leading to more robust and maintainable code.
-   **Vite:** A next-generation frontend tooling that provides an extremely fast development server and optimized build process for Vue applications.
-   **Sass:** Used for advanced CSS pre-processing, enabling more organized, maintainable, and dynamic stylesheets.

### **Infrastructure:**

-   **Docker:** Facilitates the deployment and management of services (backend, database) through lightweight, portable containers.
-   **Docker Compose:** An indispensable tool for orchestrating multi-container Docker applications, making local development environments easy to set up and manage.

---

## 📚 **Project Structure**

### **Backend:**

-   **`src/main/java/com/nursetrack`:** Contains all the Java source code for the backend application.
    -   **`config/`:** Houses general application configurations, including CORS settings and security configurations.
    -   **`controller/`:** Defines the REST controllers responsible for handling incoming HTTP requests and mapping them to appropriate business logic.
    -   **`service/`:** Encapsulates the core business logic and orchestrates operations between controllers and repositories.
    -   **`repository/`:** Manages interactions with the database, handling data persistence and retrieval.
    -   **`dto/`:** Contains Data Transfer Objects, used for efficient data exchange between layers.
    -   **`mappers/`:** Utilizes MapStruct for automatic mapping between entities and DTOs, reducing boilerplate code.

### **Frontend:**

-   **`src/`:** The root directory for the frontend application's source code.
    -   **`components/`:** Stores reusable UI components that can be used across different views.
    -   **`views/`:** Contains the main application views or pages.
    -   **`router/`:** Configures the application's navigation routes.
    -   **`services/`:** Manages the logic for interacting with the backend REST API.
    -   **`stores/`:** Manages the global state with Pinia.
    -   **`assets/`:** Holds static resources such as images, fonts, and global stylesheets.

---

## 🌟 **How to Run the Project**

To get NurseTrack up and running on your local machine, follow these steps:

### **Prerequisites:**

Ensure you have the following software installed:

-   **Node.js** (v16 or higher)
-   **Java Development Kit (JDK)** (v21)
-   **Docker** and **Docker Compose** (Docker Desktop or a similar Docker environment is recommended)

### **Steps to Run:**

1.  **Clone the repository:**
    Start by cloning the NurseTrack GitHub repository to your local machine and navigating into the project directory.

    ```bash
    git clone [https://github.com/GonS11/NurseTrack.git](https://github.com/GonS11/NurseTrack.git)
    cd NurseTrack
    ```

2.  **Start Backend and Database Services (using Docker Compose):**
    This command will build (if necessary) and start the backend Spring Boot application and the MySQL database in detached mode.

    ```bash
    docker-compose up -d
    ```

3.  **Verify Backend Readiness:**
    It's crucial to wait for the backend service to be fully operational before starting the frontend. You can monitor its health by accessing the Spring Boot Actuator endpoint.

    ```bash
    # You can also use curl http://localhost:8080/actuator/health
    # Or just open this URL in your browser:
    http://localhost:8080/actuator/health
    ```
    The backend is ready when the response displays: `"status": "UP"`.

4.  **Navigate to the Frontend Directory:**
    Change your current directory to the frontend project.

    ```bash
    cd nurse-track-frontend
    ```

5.  **Install Frontend Dependencies:**
    Install all the necessary npm packages for the Vue.js frontend.

    ```bash
    npm install
    ```

6.  **Run the Frontend in Development Mode:**
    This command will start the Vite development server, which includes Sass compilation, and hot-reloading for efficient frontend development.

    ```bash
    npm run dev
    ```

7.  **Access the Application:**
    Once both backend and frontend services are running, you can access NurseTrack through your web browser.

    -   **Frontend (User Interface):** [http://localhost:5173](http://localhost:5173)
    -   **Backend (API Base URL):** [http://localhost:8080](http://localhost:8080)
