# Automated Rollback Script for SmartDispatch 108
# Candidate: Prachee Wakode (23102A0020)

param (
    [string]$TargetDeployment = "docker", # "docker" or "standalone"
    [string]$PreviousTag = "ambulance-dispatch:1.0.0",
    [string]$ContainerName = "smartdispatch-prod",
    [string]$Port = "8080"
)

Write-Host "==========================================================" -ForegroundColor Red
Write-Host " INITIATING EMERGENCY ROLLBACK & RECOVERY PROCEDURE       " -ForegroundColor Yellow
Write-Host " System: SmartDispatch 108 | Rollback Target: $PreviousTag" -ForegroundColor Cyan
Write-Host " Candidate: Prachee Wakode (23102A0020)                   " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Red

if ($TargetDeployment -eq "docker") {
    Write-Host "[1/4] Stopping degraded container: $ContainerName..." -ForegroundColor Yellow
    docker stop $ContainerName -ErrorAction SilentlyContinue
    docker rm -f $ContainerName -ErrorAction SilentlyContinue

    Write-Host "[2/4] Deploying previous stable release image: $PreviousTag..." -ForegroundColor Green
    docker run -d --name $ContainerName -p "${Port}:8080" --restart unless-stopped $PreviousTag

    Write-Host "[3/4] Warming up container..." -ForegroundColor Cyan
    Start-Sleep -Seconds 5

    Write-Host "[4/4] Validating recovery health check..." -ForegroundColor Green
    try {
        $res = Invoke-RestMethod -Uri "http://localhost:${Port}/actuator/health" -TimeoutSec 10
        if ($res.status -eq "UP") {
            Write-Host "ROLLBACK SUCCESSFUL: System recovered and active on $PreviousTag" -ForegroundColor Green
        } else {
            Write-Error "ROLLBACK ALERT: Endpoint responded with status: $($res.status)"
        }
    } catch {
        Write-Error "ROLLBACK VERIFICATION FAILED: $_"
    }
}
