# NurseTrack

**NurseTrack** is a robust web application engineered to streamline the intricate management of shifts, schedules, and vacations for nursing staff. Our goal is to provide an intuitive and efficient platform that optimizes planning and organization within healthcare environments, empowering users to easily view their schedules and submit vacation requests.

---

## 🚀 **Project Description**

NurseTrack serves as a pivotal tool for enhancing time management and fostering seamless communication across healthcare teams. It offers administrators powerful capabilities to oversee nursing staff schedules, while nurses benefit from easy access to their shifts, the ability to request changes, and comprehensive vacation management.

### **Key Features:**

- **Shift Management:** Intuitive tools to view, assign, and modify shifts for nursing personnel.
- **Vacation Requests:** A simplified process for nurses to request and manage their vacation days, with clear approval workflows.
- **Shift Swaps:** Integrated functionality allowing colleagues to propose and arrange shift exchanges.
- **Role-Based Dashboards:** Customized interfaces providing relevant information and functionalities tailored to each user's role (administrator, supervisor, or nurse).
- **Notifications:** Real-time alerts for crucial updates, including status changes in requests and schedule modifications.

---

## 🛠️ **Technologies Used**

NurseTrack leverages a modern, robust, and scalable technology stack to ensure efficient development, high performance, and a seamless user experience.

### **Backend:**

- [Spring Boot](https://spring.io/projects/spring-boot)
- MySQL
- JWT (JSON Web Tokens)

### **Frontend:**

- [Vue 3](https://vuejs.org/)
- TypeScript
- Vite
- Sass

### **Infrastructure:**

- Docker
- Docker Compose

---

## 📚 **Project Structure**

### **Backend:**

- `src/main/java/com/nurse_track_back`:
  - `auth/`
  - `config/`
  - `domain/`
  - `exceptions/`
  - `handlers/`
  - `repositories/`
  - `services/`
  - `utils/`
  - `validations/`
  - `web/`

### **Frontend:**

- `src/`:
  - `api/`
  - `assets/`
  - `router/`
  - `services/`
  - `stores/`
  - `styles/`
  - `types/`
  - `utils/`
  - `views/`

---

## Screenshots

### Login Page

![Login Page](https://github.com/user-attachments/assets/4edd3299-8022-45e7-ab77-0600dfae269e)

### Admin Dashboard

![Admin Dashboard](https://github.com/user-attachments/assets/f319da0b-830a-4d1f-a5cb-07ab43a1a76f)

---

## 🌟 **How to Run the Project**

> ⚠️ **Important:** The backend and database are containerized using Docker. Make sure you have **Docker** installed (Docker Desktop for Windows/macOS, or Docker Engine on Linux).
> The frontend is **not dockerized** and must be run separately using `npm run dev`.

### **Prerequisites:**

- Node.js (v16 or higher)
- Java Development Kit (JDK) v21
- Docker & Docker Compose

### **Docker Installation (Linux):**

If you're running on Linux, follow these steps to install Docker Engine and Docker Compose. For Windows or macOS, please use Docker Desktop.

1.  **Install Docker Engine:**

    ```bash
    # Update your package index
    sudo apt-get update
    # Install Docker's dependencies
    sudo apt-get install ca-certificates curl
    # Create a directory for Docker's GPG key
    sudo install -m 0755 -d /etc/apt/keyrings
    # Download and add Docker's official GPG key
    sudo curl -fsSL [https://download.docker.com/linux/ubuntu/gpg](https://download.docker.com/linux/ubuntu/gpg) -o /etc/apt/keyrings/docker.asc
    sudo chmod a+r /etc/apt/keyrings/docker.asc
    # Add the Docker repository to Apt sources
    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] [https://download.docker.com/linux/ubuntu](https://download.docker.com/linux/ubuntu) \
      $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
      sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    # Update the package index again with the Docker repository
    sudo apt-get update
    # Install Docker Engine, containerd, and Docker Compose
    sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
    ```

2.  **Manage Docker as a non-root user (optional but recommended):**
    To run Docker commands without `sudo`, add your user to the `docker` group.

    ```bash
    sudo usermod -aG docker $USER
    # Log out and log back in for the changes to take effect, or run:
    newgrp docker
    ```

3.  **Verify Docker installation:**

    ```bash
    docker run hello-world
    ```

    If the installation is successful, you'll see a message confirming Docker is working.

### **Steps to Run:**

1.  **Clone the repository:**

    ```bash
    git clone [https://github.com/GonS11/NurseTrack.git](https://github.com/GonS11/NurseTrack.git)
    cd NurseTrack
    ```

2.  **Start Backend and Database Services (using Docker Compose):**

    ```bash
    docker-compose up -d
    ```

    - To stop the services:

    ```bash
    docker-compose down
    ```

3.  **Verify Backend Readiness:**
    It's crucial to wait for the backend service to be fully operational before starting the frontend. You can monitor its health by accessing the Spring Boot Actuator endpoint.

    ```bash
    # You can also use curl http://localhost:8080/actuator/health
    # Or just open this URL in your browser:
    http://localhost:8080/actuator/health
    ```

    The backend is ready when the response displays: `"status": "UP"`.

4.  **Run the frontend (not dockerized):**
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

    - **Frontend (User Interface):** [http://localhost:5173](http://localhost:5173)
    - **Backend (API Base URL):** [http://localhost:8080](http://localhost:8080)
