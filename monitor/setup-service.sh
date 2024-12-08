#!/bin/bash

SERVICE_NAME="process-monitor"
SERVICE_FILE="/etc/systemd/system/$SERVICE_NAME.service"
SCRIPT_PATH="/home/tayssir/projects/test-hgraph/monitor.sh"

if [ ! -f "$SCRIPT_PATH" ]; then
    echo "Error: Script not found at $SCRIPT_PATH. Please update the script path."
    exit 1
fi

echo "Creating the service file at $SERVICE_FILE..."
sudo bash -c "cat > $SERVICE_FILE" <<EOL
[Unit]
Description=Process Monitor Service
After=network.target

[Service]
ExecStart=$SCRIPT_PATH
Restart=always

[Install]
WantedBy=multi-user.target
EOL

echo "Reloading systemd daemon..."
sudo systemctl daemon-reload

echo "Enabling the $SERVICE_NAME service..."
sudo systemctl enable $SERVICE_NAME

echo "Starting the $SERVICE_NAME service..."
sudo systemctl start $SERVICE_NAME


