# Automated Health Check Script for SmartDispatch 108
# Candidate: Prachee Wakode (23102A0020)

param (
    [string]$HostUrl = "http://localhost:8080",
    [int]$MaxRetries = 10,
    [int]$DelaySeconds = 3
)

Write-Host "Pinging health endpoint: ${HostUrl}/actuator/health..." -ForegroundColor Cyan

$attempt = 1
$healthy = $false

while ($attempt -le $MaxRetries) {
    try {
        $res = Invoke-RestMethod -Uri "${HostUrl}/actuator/health" -TimeoutSec 5
        if ($res.status -eq "UP") {
            Write-Host "✅ Health Check PASSED on Attempt $attempt: System is UP" -ForegroundColor Green
            $healthy = $true
            break
        }
    } catch {
        Write-Host "⏳ Attempt $attempt of $MaxRetries: Server starting up..." -ForegroundColor Yellow
        Start-Sleep -Seconds $DelaySeconds
        $attempt++
    }
}

if (-not $healthy) {
    Write-Host "❌ Health Check FAILED after $MaxRetries attempts!" -ForegroundColor Red
    exit 1
} else {
    exit 0
}
