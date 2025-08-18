@echo off
echo 🚀 Productivity App API Testing Script
echo =====================================
echo.

echo Starting API Tests...
echo.

REM Test User Registration
echo 👤 Testing User Registration...
curl -X POST http://localhost:8080/users/register ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"test@example.com\",\"username\":\"testuser\",\"password\":\"password123\"}"
echo.
echo.

REM Test User Login
echo 👤 Testing User Login...
curl -X POST http://localhost:8080/users/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"test@example.com\",\"password\":\"password123\"}"
echo.
echo.

REM Test Get User
echo 👤 Testing Get User by ID...
curl -X GET http://localhost:8080/users/1
echo.
echo.

REM Test Create Goal
echo 🎯 Testing Create Goal...
curl -X POST http://localhost:8080/goals/1 ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"Test Goal\",\"description\":\"A test goal\",\"startDate\":\"2025-01-01\",\"endDate\":\"2025-12-31\",\"status\":\"ACTIVE\"}"
echo.
echo.

REM Test Get Goals
echo 🎯 Testing Get User Goals...
curl -X GET http://localhost:8080/goals/user/1
echo.
echo.

REM Test Create Checkpoint
echo ✅ Testing Create Checkpoint...
curl -X POST http://localhost:8080/checkpoints/1 ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"Test Checkpoint\",\"description\":\"A test checkpoint\",\"dueDate\":\"2025-06-30\",\"status\":\"PENDING\"}"
echo.
echo.

REM Test Get Checkpoints
echo ✅ Testing Get Checkpoints for Goal...
curl -X GET http://localhost:8080/checkpoints/goal/1
echo.
echo.

REM Test Send Buddy Request
echo 👥 Testing Send Buddy Request...
curl -X POST http://localhost:8080/buddies/send ^
  -H "Content-Type: application/json" ^
  -d "{\"senderId\":1,\"receiverId\":2}"
echo.
echo.

REM Test Get Pending Requests
echo 👥 Testing Get Pending Buddy Requests...
curl -X GET http://localhost:8080/buddies/pending/2
echo.
echo.

echo 🎉 API Testing Complete!
echo.
pause
