FROM openjdk:21-jdk-slim

WORKDIR /app

# Copia el JAR (asegúrate de que la ruta sea correcta)
COPY target/*.jar app.jar

# Agrega espera para MySQL (opcional)
RUN apt-get update && apt-get install -y curl

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]