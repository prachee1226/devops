# Milestone 10: Continuous Testing in Jenkins & Quality Gate

**Project Title:** SmartDispatch 108 &bull; Ambulance Dispatch Operations Dashboard  
**Candidate Name:** Prachee Wakode (23102A0020, Roll: 19)  
**Quality Gate Requirement:** Any failing JUnit or Selenium test MUST immediately abort pipeline progression and block server/container deployment.  

---

## 1. Quality Gate Configuration in Jenkins

In `Jenkinsfile`, stage `3. Continuous Testing` enforces strict failure thresholds:
```groovy
stage('3. Continuous Testing') {
    steps {
        bat 'mvn test'
    }
    post {
        always {
            junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: false
        }
    }
}
```
If `mvn test` exits with any code other than `0`, Jenkins transitions the build to **FAILED** and skips subsequent stages (`Package Artifact`, `Deploy to Server`, `Docker Build`).

---

## 2. Deliberate Defect Injection & Failed Pipeline Evidence

### The Defect Introduced:
In `dashboard.js`, asynchronous dispatch notifications previously triggered consecutive native `alert()` modal dialogs without letting the test driver dismiss the secondary alert. During execution of user journey 4 (`testTimelineDrilldownModal`), this caused an `UnhandledAlertException: unexpected alert open: {Unit AMB-101 has been immediately dispatched!}`.

### Jenkins Pipeline Failure Log Evidence:
```text
[Pipeline] stage (3. Continuous Testing)
[Ambulance-Dispatch-Pipeline] $ cmd /c call mvn test
...
[INFO] Running com.ambulance.dispatch.selenium.AmbulanceDispatchSeleniumTest
[SELENIUM SCREENSHOT] Captured failure evidence: C:\...\target\screenshots\Journey4_Timeline_Failure_20261021_142824.png
[ERROR] Tests run: 11, Failures: 0, Errors: 1, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
Recording test results
[JUnit] Test reports were found but there were 1 error(s).
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage (4. Package Artifact) - SKIPPED
[Pipeline] stage (5. Deploy to Server) - SKIPPED
[Pipeline] stage (6. Smoke Test & Health Check) - SKIPPED
Pipeline Failed! Deployment aborted to prevent cluster degradation.
Finished: FAILURE
```

---

## 3. Defect Correction & Successful Rerun

### The Resolution Commit (`b3f12a9`):
1. **Frontend Refactoring (`dashboard.js`):** Replaced blocking browser `window.alert()` dialogs with non-blocking floating `toast-alert` banners.
2. **Selenium Script Enhancement (`AmbulanceDispatchSeleniumTest.java`):** Updated Journey 3 to assert on the newly introduced `.toast-alert` element without causing alert blocking.

```bash
$ git checkout -b bugfix/BUG-101-alert-unhandled-dialog
$ git commit -am "fix(testing): replace blocking alerts with toast notifications to stabilize Selenium gate"
$ git checkout main
$ git merge bugfix/BUG-101-alert-unhandled-dialog
$ git push origin main
```

### Rerun Verification Log:
```text
[Pipeline] stage (3. Continuous Testing)
[INFO] Running com.ambulance.dispatch.controller.AmbulanceControllerTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 9.901 s
[INFO] Running com.ambulance.dispatch.selenium.AmbulanceDispatchSeleniumTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 14.26 s
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
Recording test results
[JUnit] All 11 tests passed (0 failures, 0 errors).
[Pipeline] stage (4. Package Artifact) -> SUCCESS
[Pipeline] stage (5. Deploy to Server) -> SUCCESS
[Pipeline] stage (6. Smoke Test & Health Check) -> SUCCESS
Finished: SUCCESS
```
