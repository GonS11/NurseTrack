@echo off

:: Start Docker services (MySQL and Backend)
docker-compose up -d

:: Wait for the backend to be ready
echo Waiting for the backend to be ready...
:wait
curl -s http://localhost:8080/actuator/health | findstr "UP" >nul
if %errorlevel% neq 0 (
    timeout 5 >nul
    goto wait
)
echo The backend is ready.

:: Start the frontend
echo Starting the frontend...
cd nurse-track-frontend
npm install
npm run dev