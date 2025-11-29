#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

# API base URL
API_BASE="http://localhost:8080"

echo -e "${BLUE}🧪 API Testing Script for Spring Boot CRUD API${NC}"
echo "======================================================"

# Check if the API is running
print_info "Checking if API is running..."
if ! curl -s "$API_BASE/health" > /dev/null; then
    print_error "API is not running on $API_BASE"
    print_info "Please start the application first:"
    print_info "  ./scripts/start.sh"
    print_info "  or"
    print_info "  docker-compose up -d"
    exit 1
fi

print_success "API is running!"

# Test health endpoint
echo ""
print_info "Testing Health Endpoint..."
HEALTH_RESPONSE=$(curl -s "$API_BASE/health")
if [[ $? -eq 0 ]]; then
    print_success "Health endpoint: $HEALTH_RESPONSE"
else
    print_error "Health endpoint failed"
fi

# Test root endpoint
echo ""
print_info "Testing Root Endpoint..."
ROOT_RESPONSE=$(curl -s "$API_BASE/")
if [[ $? -eq 0 ]]; then
    print_success "Root endpoint: $ROOT_RESPONSE"
else
    print_error "Root endpoint failed"
fi

# Test Users API
echo ""
print_info "Testing Users API..."
echo "======================"

# Create a user
print_info "1. Creating a user..."
USER_RESPONSE=$(curl -s -X POST "$API_BASE/api/users" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123",
    "age": 30,
    "isActive": true
  }')

if [[ $? -eq 0 ]]; then
    print_success "User created successfully"
    USER_ID=$(echo $USER_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
    print_info "User ID: $USER_ID"
else
    print_error "Failed to create user"
    echo "Response: $USER_RESPONSE"
fi

# Get all users
print_info "2. Getting all users..."
USERS_RESPONSE=$(curl -s "$API_BASE/api/users")
if [[ $? -eq 0 ]]; then
    print_success "Retrieved all users"
    USER_COUNT=$(echo $USERS_RESPONSE | grep -o '"id"' | wc -l)
    print_info "Total users: $USER_COUNT"
else
    print_error "Failed to get users"
    echo "Response: $USERS_RESPONSE"
fi

# Get user by ID (if we have an ID)
if [[ -n "$USER_ID" ]]; then
    print_info "3. Getting user by ID: $USER_ID"
    USER_BY_ID_RESPONSE=$(curl -s "$API_BASE/api/users/$USER_ID")
    if [[ $? -eq 0 ]]; then
        print_success "Retrieved user by ID"
    else
        print_error "Failed to get user by ID"
        echo "Response: $USER_BY_ID_RESPONSE"
    fi
fi

# Update user (if we have an ID)
if [[ -n "$USER_ID" ]]; then
    print_info "4. Updating user: $USER_ID"
    UPDATE_RESPONSE=$(curl -s -X PUT "$API_BASE/api/users/$USER_ID" \
      -H "Content-Type: application/json" \
      -d '{
        "name": "Jane Doe",
        "age": 31
      }')
    if [[ $? -eq 0 ]]; then
        print_success "User updated successfully"
    else
        print_error "Failed to update user"
        echo "Response: $UPDATE_RESPONSE"
    fi
fi

# Test Authentication API
echo ""
print_info "Testing Authentication API..."
echo "================================="

# Test signup
print_info "1. Testing user signup..."
SIGNUP_RESPONSE=$(curl -s -X POST "$API_BASE/api/auth/signup" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test.user@example.com",
    "password": "password123",
    "age": 25
  }')

if [[ $? -eq 0 ]]; then
    print_success "User signup successful"
else
    print_error "Failed to signup user"
    echo "Response: $SIGNUP_RESPONSE"
fi

# Test login
print_info "2. Testing user login..."
LOGIN_RESPONSE=$(curl -s -X POST "$API_BASE/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test.user@example.com",
    "password": "password123"
  }')

if [[ $? -eq 0 ]]; then
    print_success "User login successful"
else
    print_error "Failed to login user"
    echo "Response: $LOGIN_RESPONSE"
fi

# Test login with wrong password
print_info "3. Testing login with wrong password..."
WRONG_LOGIN_RESPONSE=$(curl -s -X POST "$API_BASE/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test.user@example.com",
    "password": "wrongpassword"
  }')

if [[ $? -eq 0 ]]; then
    if echo "$WRONG_LOGIN_RESPONSE" | grep -q "Invalid credentials"; then
        print_success "Correctly rejected wrong password"
    else
        print_warning "Unexpected response for wrong password"
        echo "Response: $WRONG_LOGIN_RESPONSE"
    fi
else
    print_error "Failed to test wrong password login"
fi

# Delete user (if we have an ID)
if [[ -n "$USER_ID" ]]; then
    echo ""
    print_info "5. Deleting user: $USER_ID"
    DELETE_RESPONSE=$(curl -s -X DELETE "$API_BASE/api/users/$USER_ID")
    if [[ $? -eq 0 ]]; then
        print_success "User deleted successfully"
    else
        print_error "Failed to delete user"
        echo "Response: $DELETE_RESPONSE"
    fi
fi

echo ""
print_success "API testing completed!"
print_info "Swagger documentation available at: $API_BASE/api" 