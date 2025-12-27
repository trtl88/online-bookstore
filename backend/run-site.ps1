<#
Simple script to stop any running backend, build and run the jar.
Place in `backend` folder and run from repository root or directly.

Usage (from repo root):
powershell -ExecutionPolicy Bypass -File backend\run-site.ps1

#>

# powershell -ExecutionPolicy Bypass -File backend\run-site.ps1

try {
    $backendDir = Split-Path -Parent $MyInvocation.MyCommand.Path
} catch {
    # When invoked from some contexts $MyInvocation may be null; fallback to cwd
    $backendDir = Join-Path (Get-Location) ''
}

Write-Host "Backend dir: $backendDir"

Write-Host "Stopping any running backend processes..."
$procs = Get-CimInstance Win32_Process | Where-Object { $_.CommandLine -and (
    $_.CommandLine -like '*backend-0.0.1-SNAPSHOT.jar*' -or
    $_.CommandLine -like '*mvnw.cmd spring-boot:run*' -or
    $_.CommandLine -like '*spring-boot:run*'
) }
if ($procs) {
    $procs | Select-Object ProcessId, CommandLine | Format-Table -AutoSize
    $procs | ForEach-Object { 
        try { Stop-Process -Id $_.ProcessId -Force -ErrorAction Stop; Write-Host "Stopped PID:$($_.ProcessId)" } 
        catch { Write-Warning "Failed to stop PID $($_.ProcessId): $_" }
    }
} else {
    Write-Host "No matching backend process found."
}

Push-Location $backendDir

Write-Host "Building backend (mvn package)..."
if (Test-Path .\mvnw.cmd) {
    & .\mvnw.cmd -DskipTests package
} else {
    Write-Host "mvnw.cmd not found, trying system mvn..."
    & mvn -DskipTests package
}

if ($LASTEXITCODE -ne 0) {
    Write-Error "Build failed (exit code $LASTEXITCODE). Aborting run."
    Pop-Location
    exit $LASTEXITCODE
}

# Ensure logs folder exists
if (-not (Test-Path logs)) { New-Item -ItemType Directory -Path logs | Out-Null }

Write-Host "Starting backend jar (detached). Logs -> logs\\backend.log"
Start-Process -FilePath cmd.exe -ArgumentList '/c', "java -jar target\\backend-0.0.1-SNAPSHOT.jar > logs\\backend.log 2>&1" -WorkingDirectory $backendDir -WindowStyle Hidden

Write-Host "Started. Follow logs with: Get-Content backend\\logs\\backend.log -Wait"

Pop-Location

exit 0
