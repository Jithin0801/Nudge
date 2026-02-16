#!/bin/bash

# Define paths
PROJECT_ROOT=$(pwd)

echo "Restarting Nudge Application..."

./stop-nudge.sh

echo "Waiting for processes to terminate..."
sleep 5

./start-nudge.sh

echo "Nudge Application restarted."
