# SmartDispatch 108 - One-Click Build & Packaging Automation
# Student: Prachee Wakode (23102A0020, Roll: 19)

param (
    [ValidateSet("build", "test", "package", "clean", "run")]
    [string]$Action = "package",
    [switch]$SkipTests
)

$ErrorActionPreference = "Stop"

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " SmartDispatch 108 - Automated Build System               " -ForegroundColor Yellow
Write-Host " Student: Prachee Wakode (23102A0020, Roll: 19)          " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

# 1. Verify Java Installation
$javaCmd = Get-Command java -ErrorAction SilentlyContinue
if (-not $javaCmd) {
    if (Test-Path "C:\DevTools\jdk-21\bin\java.exe") {
        $env:Path = "C:\DevTools\jdk-21\bin;" + $env:Path
        $javaCmd = Get-Command java
    } else {
        Write-Error "Java 21 is required but not found in PATH or C:\DevTools\jdk-21."
        exit 1
    }
}
Write-Host "[1/3] Java Runtime Detected: $($javaCmd.Source)" -ForegroundColor Green

# 2. Select Maven executable (mvnw.cmd preferred)
$mvnCmd = ".\mvnw.cmd"
if (-not (Test-Path $mvnCmd)) {
    if (Get-Command mvn -ErrorAction SilentlyContinue) {
        $mvnCmd = "mvn"
    } elseif (Test-Path "C:\DevTools\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin\mvn.cmd") {
        $mvnCmd = "C:\DevTools\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin\mvn.cmd"
    } else {
        Write-Error "Neither Maven Wrapper (mvnw.cmd) nor system Maven was found."
        exit 1
    }
}

Write-Host "[2/3] Using Build Tool: $mvnCmd" -ForegroundColor Green

# 3. Execute Build Target
$testFlag = if ($SkipTests) { "-DskipTests=true" } else { "-DskipTests=false" }

function Stop-RunningDashboard {
    $running = Get-CimInstance Win32_Process -ErrorAction SilentlyContinue | Where-Object { $_.CommandLine -like "*ambulance-dispatch-dashboard*" }
    if ($running) {
        foreach ($proc in $running) {
            Write-Host "Stopping running dashboard instance (PID: $($proc.ProcessId)) to release JAR lock..." -ForegroundColor Yellow
            Stop-Process -Id $proc.ProcessId -Force -ErrorAction SilentlyContinue
        }
        Start-Sleep -Seconds 1
    }
}

switch ($Action) {
    "clean" {
        Stop-RunningDashboard
        Write-Host "[3/3] Cleaning target build directory..." -ForegroundColor Yellow
        & $mvnCmd clean
    }
    "test" {
        Write-Host "[3/3] Executing JUnit 5 and Selenium Test Suite..." -ForegroundColor Yellow
        & $mvnCmd test
    }
    "package" {
        Stop-RunningDashboard
        Write-Host "[3/3] Compiling and Packaging Executable Spring Boot JAR..." -ForegroundColor Yellow
        & $mvnCmd clean package $testFlag
        if (Test-Path "target\ambulance-dispatch-dashboard-1.0.0-SNAPSHOT.jar") {
            Write-Host "----------------------------------------------------------" -ForegroundColor Green
            Write-Host "BUILD SUCCESS! Artifact created:" -ForegroundColor Green
            Write-Host "-> target\ambulance-dispatch-dashboard-1.0.0-SNAPSHOT.jar" -ForegroundColor Cyan
            Write-Host "----------------------------------------------------------" -ForegroundColor Green
        }
    }
    "run" {
        Write-Host "[3/3] Starting Spring Boot application on port 8085..." -ForegroundColor Green
        java -jar target\ambulance-dispatch-dashboard-1.0.0-SNAPSHOT.jar --server.port=8085
    }
}
