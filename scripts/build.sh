#!/bin/bash
# SmartDispatch 108 - Automated Build Script for Linux/macOS
# Student: Prachee Wakode (23102A0020, Roll: 19)

set -e

ACTION="${1:-package}"
SKIP_TESTS="${2:-false}"

echo "=========================================================="
echo " SmartDispatch 108 - Linux Build Automation               "
echo " Student: Prachee Wakode (23102A0020, Roll: 19)          "
echo "=========================================================="

if [ -f "./mvnw" ]; then
    MVN_CMD="./mvnw"
    chmod +x ./mvnw
else
    MVN_CMD="mvn"
fi

TEST_ARG="-DskipTests=$SKIP_TESTS"

case "$ACTION" in
    clean)
        echo "Cleaning build targets..."
        $MVN_CMD clean
        ;;
    test)
        echo "Running test suite..."
        $MVN_CMD test
        ;;
    package)
        echo "Packaging executable JAR..."
        $MVN_CMD clean package $TEST_ARG
        echo "Build completed successfully!"
        ;;
    run)
        echo "Launching application on port 8085..."
        java -jar target/ambulance-dispatch-dashboard-1.0.0-SNAPSHOT.jar --server.port=8085
        ;;
    *)
        echo "Usage: ./scripts/build.sh [clean|test|package|run] [skipTests: true|false]"
        exit 1
        ;;
esac
