@echo off

:: Start Docker services
echo Starting Docker services...
docker-compose up -d

:: Wait for MySQL to be ready
echo Waiting for MySQL to be ready...
:wait_mysql
docker exec mysqldb mysqladmin ping -h localhost --silent >nul
if %errorlevel% neq 0 (
    timeout 5 >nul
    goto wait_mysql
)
echo MySQL is ready.

:: Wait for the backend to be ready
echo Waiting for the backend to be ready...
:wait_backend
curl -s http://localhost:8080/actuator/health | findstr "UP" >nul
if %errorlevel% neq 0 (
    timeout 5 >nul
    goto wait_backend
)
echo The backend is ready.

:: Start the frontend
echo Starting the frontend...
cd nurse-track-frontend
npm install
npm run watch:sass
npm run dev