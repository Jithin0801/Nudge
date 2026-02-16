#!/bin/bash

# Define paths
PROJECT_ROOT=$(pwd)
BACKEND_PID_FILE="$PROJECT_ROOT/backend.pid"
FRONTEND_PID_FILE="$PROJECT_ROOT/frontend.pid"

# Function to stop process by PID
stop_process() {
    PID_FILE=$1
    NAME=$2
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if ps -p "$PID" > /dev/null; then
            echo "Stopping $NAME (PID: $PID)..."
            kill "$PID"
            rm "$PID_FILE"
            echo "$NAME stopped."
        else
            echo "$NAME is not running (PID file exists but process not found)."
            rm "$PID_FILE"
        fi
    else
        echo "$NAME is easier to verify, but PID file not found."
    fi
}

echo "Stopping Nudge Application..."

# Stop Backend
if [ -f "$BACKEND_PID_FILE" ]; then
    stop_process "$BACKEND_PID_FILE" "Backend"
else
    echo "Backend PID file not found. Trying to find process by name..."
    pkill -f "nudge-1.0.0.war"
    echo "Backend stopped (if running)."
fi

# Stop Frontend
if [ -f "$FRONTEND_PID_FILE" ]; then
    stop_process "$FRONTEND_PID_FILE" "Frontend"
else
    echo "Frontend PID file not found. Trying to find process by name..."
    # Warning: pkill -f "npm start" might be too broad if user runs other npm start projects,
    # but for this specific context, let's assume it's acceptable or try to target specifically.
    # A better approach is trusting the PID file primarily.
    # Alternatively, kill by port usage if lsof is available, but let's stick to PID for now.
    echo "Frontend PID file missing. Please manually check for 'npm start' processes if needed."
fi

echo "Nudge Application stopped."
