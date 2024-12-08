#!/bin/bash

PROCESS_NAME="idea"
LOG_FILE="$HOME/.monitor.log"

while true; do
    TIMESTAMP=$(date +"%Y-%m-%d %H:%M:%S")
    if pgrep -x "$PROCESS_NAME" > /dev/null; then
        echo "$TIMESTAMP START" >> "$LOG_FILE"
        while pgrep -x "$PROCESS_NAME" > /dev/null; do sleep 1; done
        TIMESTAMP=$(date +"%Y-%m-%d %H:%M:%S")
        echo "$TIMESTAMP END" >> "$LOG_FILE"
    fi
    sleep 5
done

