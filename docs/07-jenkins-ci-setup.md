# Milestone 07: Jenkins Installation and Continuous Integration Job

**Project Title:** SmartDispatch 108 &bull; Ambulance Dispatch Operations Dashboard  
**Candidate Name:** Prachee Wakode (23102A0020, Roll: 19)  
**Jenkins Job Name:** `Ambulance-Dispatch-CI`  
**Trigger Mechanism:** Poll SCM (`H/5 * * * *`) and GitHub Webhook  

---

## 1. Jenkins Architecture & Environment Configuration

### Server Details:
- **Jenkins Host:** `http://localhost:8085` (Configured to avoid port conflict with app on `8080`)
- **Global Tools Configuration:**
  - **JDK:** `JDK-21` &rarr; Path: `C:\DevTools\jdk-21`
  - **Maven:** `Maven-3.9` &rarr; Path: `C:\DevTools\apache-maven-3.9.16-bin\apache-maven-3.9.16`
  - **Git:** Git for Windows 2.40+
- **Plugins Installed:**
  - Git Plugin
  - Maven Integration Plugin
  - Pipeline & Pipeline Stage View Plugin
  - JUnit Plugin
  - HTML Publisher Plugin
  - Timestamper Plugin

---

## 2. CI Job Configuration Steps

1. **Job Creation:** Create a new Freestyle / Maven project named `Ambulance-Dispatch-CI`.
2. **Source Code Management:**
   - Select **Git**.
   - Repository URL: `https://github.com/prachee-wakode/ambulance-dispatch-dashboard.git`
   - Branch Specifier: `*/main`
3. **Build Triggers:**
   - Enable **Poll SCM** with schedule string `H/5 * * * *` (Poll every 5 minutes).
   - Alternatively, enable **GitHub hook trigger for GITScm polling**.
4. **Build Environment:**
   - Enable **Add timestamps to the Console Output**.
5. **Build Steps:**
   - Invoke top-level Maven targets: `clean test package`
   - POM: `pom.xml`
6. **Post-build Actions:**
   - **Publish JUnit test result report:** `**/target/surefire-reports/*.xml`
   - **Archive the artifacts:** `target/*.jar`

---

## 3. Successful Build Log Evidence (Console Output)

```text
Started by an SCM change (commit a7201fd)
Running as SYSTEM
[EnvInject] - Loading node environment variables.
Building in workspace C:\ProgramData\Jenkins\.jenkins\workspace\Ambulance-Dispatch-CI
The recommended git tool is: NONE
using credential github-pat-prachee
 > git.exe rev-parse --resolve-git-dir C:\ProgramData\Jenkins\.jenkins\workspace\Ambulance-Dispatch-CI\.git # timeout=10
Fetching changes from the remote Git repository
 > git.exe config remote.origin.url https://github.com/prachee-wakode/ambulance-dispatch-dashboard.git # timeout=10
Fetching upstream changes from origin
 > git.exe --version # timeout=10
 > git.exe fetch --tags --force --progress -- origin +refs/heads/*:refs/remotes/origin/* # timeout=10
Checking out Revision a7201fd (refs/remotes/origin/main)
 > git.exe checkout -f a7201fd # timeout=10
Commit message: "merge(resolve-conflict): consolidate response time SLA to 8.4 minutes"
[Ambulance-Dispatch-CI] $ C:\DevTools\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin\mvn.cmd clean test package
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------< com.ambulance:ambulance-dispatch-dashboard >-------------
[INFO] Building ambulance-dispatch-dashboard 1.0.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- clean:3.3.2:clean (default-clean) @ ambulance-dispatch-dashboard ---
[INFO] --- resources:3.3.1:resources (default-resources) @ ambulance-dispatch-dashboard ---
[INFO] --- compiler:3.11.0:compile (default-compile) @ ambulance-dispatch-dashboard ---
[INFO] Compiling 10 source files with javac [debug release 21] to target\classes
[INFO] --- surefire:3.2.5:test (default-test) @ ambulance-dispatch-dashboard ---
[INFO] Running com.ambulance.dispatch.controller.AmbulanceControllerTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.326 s
[INFO] Running com.ambulance.dispatch.selenium.AmbulanceDispatchSeleniumTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 14.26 s
[INFO] 
[INFO] Results:
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] --- jar:3.3.0:jar (default-jar) @ ambulance-dispatch-dashboard ---
[INFO] Building jar: C:\...\target\ambulance-dispatch-dashboard-1.0.0-SNAPSHOT.jar
[INFO] --- spring-boot-maven-plugin:3.2.5:repackage (repackage) @ ambulance-dispatch-dashboard ---
[INFO] Replacing main artifact with repackaged archive.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] Total time:  38.412 s
[INFO] Finished at: 2026-10-21T14:38:10+05:30
[INFO] ------------------------------------------------------------------------
Recording test results
Archiving artifacts
‘target/ambulance-dispatch-dashboard-1.0.0-SNAPSHOT.jar’ saved to artifact repository.
Finished: SUCCESS
```
