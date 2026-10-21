# Docker Container Lifecycle Management Script for SmartDispatch 108
# Candidate: Prachee Wakode (23102A0020)

param (
    [Parameter(Mandatory=$false)]
    [ValidateSet("build", "run", "logs", "health", "stop", "restart", "clean", "lifecycle-demo")]
    [string]$Action = "lifecycle-demo",
    
    [string]$ImageName = "ambulance-dispatch",
    [string]$Tag = "1.0.0",
    [string]$ContainerName = "smartdispatch-108",
    [string]$Port = "8080"
)

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " SmartDispatch 108 - Docker Lifecycle Management Utility " -ForegroundColor Yellow
Write-Host " Student: Prachee Wakode (23102A0020, Roll: 19)          " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

function Build-Image {
    Write-Host "[1/7] Building Docker Image: ${ImageName}:${Tag}..." -ForegroundColor Green
    docker build -t "${ImageName}:${Tag}" -t "${ImageName}:latest" -f docker/Dockerfile .
    Write-Host "Image built successfully!" -ForegroundColor Green
    docker images | Select-String $ImageName
}

function Run-Container {
    Write-Host "[2/7] Starting Container: ${ContainerName} on port ${Port}..." -ForegroundColor Green
    docker run -d -p "${Port}:8080" --name $ContainerName --restart unless-stopped "${ImageName}:${Tag}"
    Write-Host "Container started. Inspecting status..." -ForegroundColor Green
    docker ps -f "name=${ContainerName}"
}

function View-Logs {
    Write-Host "[3/7] Inspecting Container Logs for: ${ContainerName}..." -ForegroundColor Green
    docker logs --tail 30 -f $ContainerName
}

function Check-Health {
    Write-Host "[4/7] Checking Container Health & Actuator Endpoint..." -ForegroundColor Green
    $healthUrl = "http://localhost:${Port}/actuator/health"
    try {
        $res = Invoke-RestMethod -Uri $healthUrl -TimeoutSec 5
        Write-Host "Container Health Result: $($res.status)" -ForegroundColor Green
    } catch {
        Write-Warning "Healthcheck failed or container still warming up: $_"
    }
}

function Stop-Container {
    Write-Host "[5/7] Gracefully Stopping Container: ${ContainerName}..." -ForegroundColor Yellow
    docker stop $ContainerName
}

function Restart-Container {
    Write-Host "[6/7] Restarting Container: ${ContainerName}..." -ForegroundColor Yellow
    docker restart $ContainerName
}

function Clean-Container {
    Write-Host "[7/7] Cleaning up Container & Artifacts..." -ForegroundColor Red
    docker stop $ContainerName -ErrorAction SilentlyContinue
    docker rm $ContainerName -ErrorAction SilentlyContinue
    Write-Host "Cleanup completed." -ForegroundColor Green
}

switch ($Action) {
    "build"          { Build-Image }
    "run"            { Run-Container }
    "logs"           { View-Logs }
    "health"         { Check-Health }
    "stop"           { Stop-Container }
    "restart"        { Restart-Container }
    "clean"          { Clean-Container }
    "lifecycle-demo" {
        Write-Host "Executing Complete Lifecycle Demonstration..." -ForegroundColor Magenta
        Build-Image
        Clean-Container
        Run-Container
        Start-Sleep -Seconds 5
        Check-Health
        Write-Host "Container lifecycle verification complete!" -ForegroundColor Green
    }
}
