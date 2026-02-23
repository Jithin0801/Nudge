#!/bin/bash

# Define paths
PROJECT_ROOT=$(pwd)
BACKEND_DIR="$PROJECT_ROOT/nudge"
FRONTEND_DIR="$PROJECT_ROOT/nudge-ui/nudge-app"

echo "Building Nudge Application..."

# 1. Build Backend
echo "--> Building Backend (Spring Boot)..."
cd "$BACKEND_DIR" || exit
./mvnw clean install -DskipTests

if [ $? -eq 0 ]; then
    echo "Backend build successful."
else
    echo "Backend build failed. Exiting."
    exit 1
fi

# 2. Build Frontend
echo "--> Building Frontend (Next.js)..."
cd "$FRONTEND_DIR" || exit
npm install
npm run build

if [ $? -eq 0 ]; then
    echo "Frontend build successful."
else
    echo "Frontend build failed. Exiting."
    exit 1
fi

echo "Nudge Application built successfully!"
echo "Backend artifact: $BACKEND_DIR/target/nudge-1.0.0.war"
echo "Frontend build: $FRONTEND_DIR/.next"
