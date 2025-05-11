#!/bin/bash

# Start Docker services
echo "Starting Docker services..."
docker-compose up -d

# Wait for MySQL to be ready
echo "Waiting for MySQL to be ready..."
while ! docker exec mysqldb mysqladmin ping -h localhost --silent; do
  sleep 5
done
echo "MySQL is ready."

# Wait for the backend to be ready
echo "Waiting for the backend to be ready..."
while ! curl -s http://localhost:8080/actuator/health | grep '"status":"UP"' > /dev/null; do
  sleep 5
done
echo "The backend is ready."

# Start the frontend
echo "Starting the frontend..."
cd nurse-track-frontend
npm install

npm run dev