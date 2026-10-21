#!/usr/bin/env bash
# Docker Container Lifecycle Management Script (Bash / Linux)
# Candidate: Prachee Wakode (23102A0020)

set -e

ACTION=${1:-"lifecycle-demo"}
IMAGE_NAME="ambulance-dispatch"
TAG="1.0.0"
CONTAINER_NAME="smartdispatch-108"
PORT="8080"

echo "=========================================================="
echo " SmartDispatch 108 - Docker Lifecycle Management Utility "
echo " Student: Prachee Wakode (23102A0020, Roll: 19)          "
echo "=========================================================="

build_image() {
    echo "--> [1/7] Building Docker Image: ${IMAGE_NAME}:${TAG}..."
    docker build -t "${IMAGE_NAME}:${TAG}" -t "${IMAGE_NAME}:latest" -f docker/Dockerfile .
    docker images | grep "$IMAGE_NAME"
}

run_container() {
    echo "--> [2/7] Running Container: ${CONTAINER_NAME} on port ${PORT}..."
    docker run -d -p "${PORT}:8080" --name "$CONTAINER_NAME" --restart unless-stopped "${IMAGE_NAME}:${TAG}"
    docker ps -f "name=$CONTAINER_NAME"
}

view_logs() {
    echo "--> [3/7] Inspecting Logs for ${CONTAINER_NAME}..."
    docker logs --tail 30 -f "$CONTAINER_NAME"
}

check_health() {
    echo "--> [4/7] Checking Container Health Endpoint..."
    curl -f -s "http://localhost:${PORT}/actuator/health" || echo "Healthcheck failed or starting up."
}

stop_container() {
    echo "--> [5/7] Stopping Container: ${CONTAINER_NAME}..."
    docker stop "$CONTAINER_NAME"
}

restart_container() {
    echo "--> [6/7] Restarting Container: ${CONTAINER_NAME}..."
    docker restart "$CONTAINER_NAME"
}

clean_container() {
    echo "--> [7/7] Cleaning up container..."
    docker stop "$CONTAINER_NAME" 2>/dev/null || true
    docker rm "$CONTAINER_NAME" 2>/dev/null || true
}

case "$ACTION" in
    build) build_image ;;
    run) run_container ;;
    logs) view_logs ;;
    health) check_health ;;
    stop) stop_container ;;
    restart) restart_container ;;
    clean) clean_container ;;
    lifecycle-demo)
        build_image
        clean_container
        run_container
        sleep 5
        check_health
        ;;
    *)
        echo "Usage: $0 {build|run|logs|health|stop|restart|clean|lifecycle-demo}"
        exit 1
        ;;
esac
