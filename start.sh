#!/bin/bash

# Start Docker services (MySQL and Backend)
docker-compose up -d

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