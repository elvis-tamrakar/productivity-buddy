# Productivity App API Endpoint Test Script
# Run this script to test all your endpoints

Write-Host "🚀 Productivity App API Testing Script" -ForegroundColor Green
Write-Host "=====================================" -ForegroundColor Green
Write-Host ""

# Base URL
$baseUrl = "http://localhost:8080"

# Test counter
$totalTests = 0
$passedTests = 0
$failedTests = 0

# Function to test an endpoint
function Test-Endpoint {
    param(
        [string]$Method,
        [string]$Url,
        [string]$Description,
        [string]$Body = $null
    )
    
    $totalTests++
    Write-Host "Testing: $Description" -ForegroundColor Yellow
    
    try {
        $headers = @{
            "Content-Type" = "application/json"
        }
        
        if ($Body) {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $headers -Body $Body
        } else {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $headers
        }
        
        Write-Host "✅ PASSED: $Description" -ForegroundColor Green
        Write-Host "   Response: $($response | ConvertTo-Json -Depth 2)" -ForegroundColor Gray
        $passedTests++
    }
    catch {
        Write-Host "❌ FAILED: $Description" -ForegroundColor Red
        Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
        $failedTests++
    }
    
    Write-Host ""
}

# Function to test endpoint that should fail (for validation testing)
function Test-EndpointShouldFail {
    param(
        [string]$Method,
        [string]$Url,
        [string]$Description,
        [string]$Body = $null
    )
    
    $totalTests++
    Write-Host "Testing: $Description (should fail)" -ForegroundColor Yellow
    
    try {
        $headers = @{
            "Content-Type" = "application/json"
        }
        
        if ($Body) {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $headers -Body $Body
        } else {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $headers
        }
        
        Write-Host "❌ FAILED: $Description should have failed but succeeded" -ForegroundColor Red
        $failedTests++
    }
    catch {
        Write-Host "✅ PASSED: $Description correctly failed as expected" -ForegroundColor Green
        Write-Host "   Expected Error: $($_.Exception.Message)" -ForegroundColor Gray
        $passedTests++
    }
    
    Write-Host ""
}

Write-Host "Starting API Tests..." -ForegroundColor Cyan
Write-Host ""

# ========================================
# USER ENDPOINTS
# ========================================
Write-Host "👤 Testing User Endpoints" -ForegroundColor Magenta
Write-Host "------------------------" -ForegroundColor Magenta

# Test user registration
$registerBody = @{
    email = "test@example.com"
    username = "testuser"
    password = "password123"
} | ConvertTo-Json

Test-Endpoint -Method "POST" -Url "$baseUrl/users/register" -Description "User Registration" -Body $registerBody

# Test user login
$loginBody = @{
    email = "test@example.com"
    password = "password123"
} | ConvertTo-Json

Test-Endpoint -Method "POST" -Url "$baseUrl/users/login" -Description "User Login" -Body $loginBody

# Test get user by ID
Test-Endpoint -Method "GET" -Url "$baseUrl/users/1" -Description "Get User by ID"

# Test update user
$updateBody = @{
    username = "updateduser"
    email = "updated@example.com"
} | ConvertTo-Json

Test-Endpoint -Method "PUT" -Url "$baseUrl/users/1" -Description "Update User" -Body $updateBody

# ========================================
# GOAL ENDPOINTS
# ========================================
Write-Host "🎯 Testing Goal Endpoints" -ForegroundColor Magenta
Write-Host "------------------------" -ForegroundColor Magenta

# Test create goal
$goalBody = @{
    title = "Test Goal"
    description = "A test goal for API testing"
    startDate = "2025-01-01"
    endDate = "2025-12-31"
    status = "ACTIVE"
} | ConvertTo-Json

Test-Endpoint -Method "POST" -Url "$baseUrl/goals/1" -Description "Create Goal" -Body $goalBody

# Test get user goals
Test-Endpoint -Method "GET" -Url "$baseUrl/goals/user/1" -Description "Get User Goals"

# Test get goal by ID
Test-Endpoint -Method "GET" -Url "$baseUrl/goals/1" -Description "Get Goal by ID"

# Test update goal
$updateGoalBody = @{
    title = "Updated Test Goal"
    description = "An updated test goal"
} | ConvertTo-Json

Test-Endpoint -Method "PUT" -Url "$baseUrl/goals/1" -Description "Update Goal" -Body $updateGoalBody

# ========================================
# CHECKPOINT ENDPOINTS
# ========================================
Write-Host "✅ Testing Checkpoint Endpoints" -ForegroundColor Magenta
Write-Host "-----------------------------" -ForegroundColor Magenta

# Test create checkpoint
$checkpointBody = @{
    title = "Test Checkpoint"
    description = "A test checkpoint for API testing"
    dueDate = "2025-06-30"
    status = "PENDING"
} | ConvertTo-Json

Test-Endpoint -Method "POST" -Url "$baseUrl/checkpoints/1" -Description "Create Checkpoint" -Body $checkpointBody

# Test get checkpoints for goal
Test-Endpoint -Method "GET" -Url "$baseUrl/checkpoints/goal/1" -Description "Get Checkpoints for Goal"

# Test update checkpoint (use ID 3 that was just created)
$updateCheckpointBody = @{
    title = "Updated Test Checkpoint"
    description = "An updated test checkpoint"
} | ConvertTo-Json

Test-Endpoint -Method "PUT" -Url "$baseUrl/checkpoints/3" -Description "Update Checkpoint" -Body $updateCheckpointBody

# Test complete checkpoint (use ID 3 that was just created)
Test-Endpoint -Method "POST" -Url "$baseUrl/checkpoints/3/complete" -Description "Complete Checkpoint"

# ========================================
# BUDDY ENDPOINTS
# ========================================
Write-Host "👥 Testing Buddy Endpoints" -ForegroundColor Magenta
Write-Host "-------------------------" -ForegroundColor Magenta

# Test send buddy request
$buddyRequestBody = @{
    senderId = 1
    receiverId = 2
} | ConvertTo-Json

Test-Endpoint -Method "POST" -Url "$baseUrl/buddies/send" -Description "Send Buddy Request" -Body $buddyRequestBody

# Test get pending requests
Test-Endpoint -Method "GET" -Url "$baseUrl/buddies/pending/2" -Description "Get Pending Buddy Requests"

# Test get sent requests
Test-Endpoint -Method "GET" -Url "$baseUrl/buddies/sent/1" -Description "Get Sent Buddy Requests"

# Test accept buddy request (use the actual request ID from the test results)
Test-Endpoint -Method "POST" -Url "$baseUrl/buddies/2/accept?userId=3" -Description "Accept Buddy Request"

# Test get accepted buddies
Test-Endpoint -Method "GET" -Url "$baseUrl/buddies/accepted/1" -Description "Get Accepted Buddies"

# ========================================
# VALIDATION TESTS (should fail)
# ========================================
Write-Host "🔍 Testing Validation (should fail)" -ForegroundColor Magenta
Write-Host "-----------------------------------" -ForegroundColor Magenta

# Test invalid user registration (missing email)
$invalidRegisterBody = @{
    username = "testuser"
    password = "password123"
} | ConvertTo-Json

Test-EndpointShouldFail -Method "POST" -Url "$baseUrl/users/register" -Description "User Registration without Email" -Body $invalidRegisterBody

# Test invalid goal creation (missing title)
$invalidGoalBody = @{
    description = "A test goal without title"
    startDate = "2025-01-01"
} | ConvertTo-Json

Test-EndpointShouldFail -Method "POST" -Url "$baseUrl/goals/1" -Description "Goal Creation without Title" -Body $invalidGoalBody

# ========================================
# TEST SUMMARY
# ========================================
Write-Host "📊 Test Summary" -ForegroundColor Cyan
Write-Host "==============" -ForegroundColor Cyan
Write-Host "Total Tests: $totalTests" -ForegroundColor White
Write-Host "Passed: $passedTests" -ForegroundColor Green
Write-Host "Failed: $failedTests" -ForegroundColor Red
Write-Host "Success Rate: $([math]::Round(($passedTests / $totalTests) * 100, 2))%" -ForegroundColor Yellow

if ($failedTests -eq 0) {
    Write-Host ""
    Write-Host "🎉 All tests passed! Your API is working perfectly!" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "⚠️  Some tests failed. Check the errors above." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Script completed!" -ForegroundColor Cyan
