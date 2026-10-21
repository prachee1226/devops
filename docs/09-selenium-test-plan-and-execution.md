# Milestone 09: Selenium Test Design and Local Execution

**Project Title:** SmartDispatch 108 &bull; Ambulance Dispatch Operations Dashboard  
**Candidate Name:** Prachee Wakode (23102A0020, Roll: 19)  
**Test Suite:** `AmbulanceDispatchSeleniumTest.java`  
**Execution Engine:** Selenium WebDriver 4.19 + Headless Chrome / Edge + JUnit 5  

---

## 1. Test Plan & The 5 Critical User Journeys

| Journey ID | Journey Title | Operational Scope | Test Assertion | Test Data |
| :---: | :--- | :--- | :--- | :--- |
| **UJ-01** | Dashboard Init & KPIs | Verifies page loading, header branding, and real-time KPI indicators. | Page title contains `Ambulance Dispatch`, clock visible, metric count `> 0`. | N/A |
| **UJ-02** | Search & Fleet Filter | Searches unit by driver name and activates `AVAILABLE` filter pill. | Matched row contains `Rajesh Sharma`; filtered rows match status. | Search: `"Rajesh"`, Filter: `"AVAILABLE"` |
| **UJ-03** | Emergency Call Intake & Auto-Dispatch | Submits 108 emergency distress form and verifies instant unit assignment. | Toast alert displays `registered successfully` and unit dispatched. | Caller: `Selenium Lead`, Phone: `+91 98000 12345`, Type: `Accident` |
| **UJ-04** | Status Drill-Down Timeline | Opens drill-down modal on active ambulance unit to inspect journey events. | Modal becomes visible, timeline milestones rendered with timestamps. | Unit: `AMB-101` / `AMB-103` |
| **UJ-05** | Alert & Exception Triage | Inspects active critical warnings (low O2 / fuel) and acknowledges exception. | Alert card disappears, alert counter decrements. | Alert: `LOW_OXYGEN` on `AMB-105` |

---

## 2. Failure Screenshot Mechanism Implementation

The test suite incorporates an automated failure screenshot mechanism utilizing Selenium's `TakesScreenshot` interface:
```java
private void takeScreenshot(String testName) {
    if (driver instanceof TakesScreenshot ts) {
        try {
            byte[] png = ts.getScreenshotAs(OutputType.BYTES);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path path = Paths.get("target/screenshots", testName + "_" + timestamp + ".png");
            Files.write(path, png);
            System.out.println("[SELENIUM SCREENSHOT] Captured failure evidence: " + path.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
```

---

## 3. Local Test Execution Report (`mvn test`)

```text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.ambulance.dispatch.controller.AmbulanceControllerTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 9.901 s
[INFO] Running com.ambulance.dispatch.selenium.AmbulanceDispatchSeleniumTest
[SELENIUM] Initialized Headless Chrome successfully.
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 14.26 s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] Total time:  36.809 s
[INFO] ------------------------------------------------------------------------
```
Surefire XML reports saved to: `target/surefire-reports/TEST-com.ambulance.dispatch.selenium.AmbulanceDispatchSeleniumTest.xml`.
