# NurseTrack Frontend

The **NurseTrack Frontend** is the user-facing application built to provide an intuitive and efficient experience for managing nursing shifts, schedules, and vacations. Developed with a modern stack, it ensures a responsive, type-safe, and highly maintainable codebase.

---

## 🚀 **Project Overview**

This repository contains the frontend application for NurseTrack, responsible for rendering the user interface and interacting with the backend API. It offers a rich and dynamic experience tailored for administrators, supervisors, and nurses, enabling seamless schedule viewing, request management, and communication.

---

## ✨ **Key Features & Technologies**

This project leverages a powerful combination of cutting-edge technologies to deliver a robust and fluid user experience:

-   **[Vue 3](https://vuejs.org/):** The core progressive JavaScript framework for building dynamic and reactive user interfaces. We utilize the `<script setup>` syntax for enhanced developer experience and cleaner component code.
-   **[TypeScript](https://www.typescriptlang.org/):** Provides static type-checking to JavaScript, significantly improving code quality, readability, and maintainability, especially in larger applications.
-   **[Pinia](https://pinia.vuejs.org/):** The official state management library for Vue, offering a simple, intuitive, and performant way to manage application-wide data.
-   **[Axios](https://axios-http.com/):** A promise-based HTTP client used for making requests to the NurseTrack backend API, ensuring efficient and reliable data fetching.
-   **[Vite](https://vitejs.dev/):** A next-generation frontend tooling that provides an incredibly fast development server with Hot Module Replacement (HMR) and an optimized build process.
-   **[Sass (SCSS)](https://sass-lang.com/):** A powerful CSS preprocessor that extends CSS with features like variables, nested rules, mixins, and functions, leading to more organized and maintainable stylesheets.

---

## 📁 **Project Structure**

The frontend application is structured to promote modularity, reusability, and clear separation of concerns:

-   **`src/`:** The main directory containing all source code for the frontend application.
    -   **`assets/`:** Houses static assets such as images, icons, fonts, and global Sass files.
    -   **`components/`:** Contains reusable Vue components that can be composed to build views.
    -   **`router/`:** Defines the application's navigation routes using Vue Router.
    -   **`services/`:** Centralizes logic for interacting with the backend API (e.g., API calls via Axios).
    -   **`stores/`:** Implements global state management using Pinia modules.
    -   **`types/`:** Stores TypeScript interface and type definitions for data structures.
    -   **`utils/`:** Provides utility functions and helper methods.
    -   **`views/`:** Contains the main application views or pages, composed of various components.
    -   **`App.vue`:** The root Vue component that orchestrates the entire application.
    -   **`main.ts`:** The entry point of the application, responsible for mounting the Vue app and initializing plugins.

---

## 🖼️ **Screenshots**

*(Coming soon! This section will feature visual examples of the NurseTrack frontend in action.)*

---

## 🚀 **Getting Started**

To run the NurseTrack Frontend locally, ensure you have set up the [NurseTrack Backend](https://github.com/GonS11/NurseTrack) first, as this frontend relies on its API.

### **Prerequisites:**

-   **Node.js** (v16 or higher)
-   The **NurseTrack Backend** running and accessible (typically at `http://localhost:8080`).

### **Steps to Run:**

1.  **Navigate to the Frontend Directory:**
    From the root `NurseTrack` repository, change your directory to the frontend project:

    ```bash
    cd nurse-track-frontend
    ```

2.  **Install Dependencies:**
    Install all the required Node.js packages:

    ```bash
    npm install
    ```

3.  **Run in Development Mode:**
    Start the Vite development server. This will compile your Vue components, TypeScript, and Sass, and provide live reloading.

    ```bash
    npm run dev
    ```

4.  **Access the Application:**
    Once the development server is running, open your web browser and navigate to:

    ```
    http://localhost:5173
    ```

---

## 📦 **Build for Production**

To create a production-ready build of the frontend application:

```bash
npm run build
