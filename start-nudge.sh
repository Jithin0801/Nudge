#!/bin/bash

# Define paths
PROJECT_ROOT=$(pwd)
BACKEND_DIR="$PROJECT_ROOT/nudge"
FRONTEND_DIR="$PROJECT_ROOT/nudge-ui/nudge-app"
BACKEND_PID_FILE="$PROJECT_ROOT/backend.pid"
FRONTEND_PID_FILE="$PROJECT_ROOT/frontend.pid"

# Function to check if a process is running
is_running() {
    if [ -f "$1" ]; then
        PID=$(cat "$1")
        if ps -p "$PID" > /dev/null; then
            return 0
        fi
    fi
    return 1
}

echo "Starting Nudge Application..."

# 1. Start Backend
if is_running "$BACKEND_PID_FILE"; then
    echo "Backend is already running (PID: $(cat "$BACKEND_PID_FILE"))"
else
    echo "Building Backend..."
    cd "$BACKEND_DIR" || exit
    ./mvnw clean install -DskipTests
    
    if [ $? -eq 0 ]; then
        echo "Backend build successful. Starting Backend..."
        nohup java -jar target/nudge-1.0.0.war > "$PROJECT_ROOT/backend.log" 2>&1 &
        echo $! > "$BACKEND_PID_FILE"
        echo "Backend started (PID: $(cat "$BACKEND_PID_FILE"))"
    else
        echo "Backend build failed. Exiting."
        exit 1
    fi
fi

# 2. Start Frontend
if is_running "$FRONTEND_PID_FILE"; then
    echo "Frontend is already running (PID: $(cat "$FRONTEND_PID_FILE"))"
else
    echo "Building Frontend..."
    cd "$FRONTEND_DIR" || exit
    npm install
    npm run build
    
    if [ $? -eq 0 ]; then
        echo "Frontend build successful. Starting Frontend..."
        nohup npm start > "$PROJECT_ROOT/frontend.log" 2>&1 &
        echo $! > "$FRONTEND_PID_FILE"
        echo "Frontend started (PID: $(cat "$FRONTEND_PID_FILE"))"
    else
        echo "Frontend build failed. Exiting."
        exit 1
    fi
fi

echo "Nudge Application started successfully."
echo "Backend Logic: $PROJECT_ROOT/backend.log"
echo "Frontend Logic: $PROJECT_ROOT/frontend.log"
