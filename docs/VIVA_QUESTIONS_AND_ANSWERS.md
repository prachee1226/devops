# SmartDispatch 108: Viva Voce Questions & Model Answers

**Candidate Name:** Prachee Wakode  
**Student PRN / Roll:** 23102A0020 / Roll 19  
**Topic:** Jenkins Pipeline for an Ambulance Dispatch Dashboard  

---

### Section 1: Project Domain & Agile Planning

#### Q1: What is the core real-time problem solved by the Ambulance Dispatch Dashboard?
**Answer:** During medical emergencies (e.g., cardiac arrest, trauma), the "Golden Hour" or "Platinum 10 Minutes" dictates survival. Traditional dispatching relies on manual paper logs and voice calls to locate idle ambulances. SmartDispatch 108 automates call intake, matches the closest available unit, tracks journey milestones in real time, and flags critical equipment shortages (oxygen levels < 20%).

#### Q2: What are the 5 core MVP functional requirements implemented?
**Answer:**
1. **Data/Event Entry:** Fast 108 emergency call registration and triage prioritization.
2. **Searchable Dashboard:** Real-time search by driver/vehicle and status filter pills (`AVAILABLE`, `DISPATCHED`, `ON_SCENE`, `AT_HOSPITAL`, `OUT_OF_SERVICE`).
3. **Summary Indicators (KPIs):** Real-time operational metric cards (Active calls, fleet count, avg response time, utilization %).
4. **Status Drill-Down:** Clickable timeline tracking individual ambulance dispatch journey milestones.
5. **Alert / Exception View:** Visual alert banners for critical conditions (oxygen < 20%, fuel < 15%, unassigned Code Red calls).

#### Q3: What is the "Definition of Done" (DoD) in your DevOps workflow?
**Answer:** A feature is only considered Done when:
1. Java code compiles cleanly without warnings.
2. All JUnit 5 unit tests pass with 100% success.
3. Headless Selenium UI test journeys pass without failure.
4. GitFlow branch policies are followed with peer review approval.
5. Jenkins CI pipeline successfully builds and archives the artifact.
6. The Docker image passes container health check validation.
7. Ansible provisioning executes idempotently.

---

### Section 2: Git, GitHub & Branching

#### Q4: What branching strategy was used in the project?
**Answer:** We used GitFlow:
- `main`: Production-ready baseline, tagged with semantic releases (`v1.0.0-mvp`).
- `develop`: Integration branch where tested features converge.
- `feature/*`: Isolated branches for user stories (e.g., `feature/ambulance-dispatch-core`).
- `bugfix/*`: Targeted branches for resolving defects flagged by testing gates.

#### Q5: How was a merge conflict created and resolved in Milestone 6?
**Answer:** We deliberately made concurrent modifications to the SLA response time baseline in `AmbulanceService.java`:
- On `develop`, it was set to `9.2` minutes.
- On `feature/alerts-and-metrics`, it was set to `8.4` minutes.  
When merging into `develop`, Git paused with a conflict. We inspected the conflict markers (`<<<<<<< HEAD`, `=======`, `>>>>>>>`), chose the optimized `8.4` minutes SLA, removed the conflict markers, staged the file, and committed the resolution.

#### Q6: Why do we tag releases in Git?
**Answer:** Git tags create permanent, immutable pointers to specific commits in history. Tagging `v1.0.0-mvp` marks the stable baseline used for production builds and enables clean rollback points.

---

### Section 3: Java, Spring Boot & Maven

#### Q7: Why did you choose Spring Boot 3 with Java 21 LTS?
**Answer:**
- **Java 21 LTS:** Provides long-term enterprise support, modern language features (record patterns, string templates), and Virtual Threads for high-concurrency dispatch operations.
- **Spring Boot 3:** Provides embedded Apache Tomcat (eliminating separate server installations), Spring Boot Actuator for automated health monitoring (`/actuator/health`), and rapid REST API controller mappings.

#### Q8: How does your application achieve thread safety without an external database?
**Answer:** Fleet and emergency call state is managed via `ConcurrentHashMap`, atomic sequence generators (`AtomicLong`), synchronized dispatch lock methods (`synchronized boolean dispatchAmbulance`), and thread-safe synchronized lists for dispatch events.

#### Q9: What is the difference between `mvn compile`, `mvn test`, and `mvn package`?
**Answer:**
- `mvn compile`: Compiles application source code in `src/main/java` to `target/classes`.
- `mvn test`: Compiles test code in `src/test/java` and executes JUnit/Selenium tests via the Surefire plugin.
- `mvn package`: Takes compiled classes, passes tests, and bundles them into an executable fat JAR with embedded Tomcat dependencies.

---

### Section 4: Jenkins CI/CD & Pipelines

#### Q10: What is "Pipeline as Code" and why is it preferred over Freestyle jobs?
**Answer:** Pipeline as Code stores the build, test, and deployment steps in a version-controlled text file (`Jenkinsfile`) alongside the application code. This provides auditability, branch-specific pipeline behaviors, peer review of deployment changes, and instant disaster recovery of Jenkins jobs.

#### Q11: What triggers were configured for the Jenkins CI job?
**Answer:**
1. **GitHub Webhook:** Pushes to `main` or `develop` trigger immediate build execution.
2. **Poll SCM (`H/5 * * * *`):** Scheduled polling every 5 minutes as a fallback if webhooks are blocked by firewalls.

#### Q12: How does parameterization work in your Jenkinsfile?
**Answer:** The `parameters` block in `Jenkinsfile` allows operators to configure build settings at runtime:
- `DEPLOY_ENV`: Choice between `local-embedded-tomcat`, `staging-tomcat`, and `production-nginx`.
- `SERVER_PORT`: Custom HTTP port string (Default: `8080`).
- `RUN_SELENIUM_TESTS`: Boolean toggle to enable or disable headless browser UI testing.

---

### Section 5: Automated Testing & Selenium Quality Gates

#### Q13: What is a Quality Gate in a CI/CD pipeline?
**Answer:** A Quality Gate is an automated enforcement point in the pipeline that halts execution if code fails to meet predefined criteria (e.g., test failures, security flaws). In our pipeline, any failing JUnit or Selenium test halts the pipeline before packaging or deployment occurs.

#### Q14: How does your Selenium test suite execute in headless environments?
**Answer:** Selenium WebDriver is instantiated with `--headless=new`, `--disable-gpu`, `--no-sandbox`, and `--disable-dev-shm-usage`. This allows Chrome or Edge to render the DOM, execute JavaScript, and perform assertions on virtual display buffers without requiring a physical monitor.

#### Q15: How does your failure screenshot mechanism operate?
**Answer:** The test suite casts the `WebDriver` instance to `TakesScreenshot` and invokes `getScreenshotAs(OutputType.BYTES)`. It formats the filename with the test journey name and timestamp, saving the PNG evidence to `target/screenshots/`.

#### Q16: What deliberate defect was injected and fixed in Milestone 10?
**Answer:** Consecutive blocking browser `alert()` modal dialogs were injected into the call submission workflow. This caused Selenium's `UnhandledAlertException` during journey 4, triggering a pipeline failure. We refactored `dashboard.js` to use non-blocking toast notifications and updated the Selenium assertion, restoring a 100% pass rate.

---

### Section 6: Docker Containerization

#### Q17: Why did you use a Multi-Stage Dockerfile?
**Answer:** A single-stage Dockerfile containing Maven and the JDK produces bloated images (> 600MB) with unnecessary build compilers in production. Our multi-stage Dockerfile uses `eclipse-temurin:21-jdk-alpine` to compile the app, and copies only the resulting JAR into `eclipse-temurin:21-jre-alpine`. This reduced the final image size to just 178MB.

#### Q18: Why is running containers as non-root critical in healthcare applications?
**Answer:** If a container running as root is compromised via an application vulnerability, the attacker may escape the container and obtain root control over the host kernel. We created and used a dedicated `spring:spring` user.

#### Q19: What does the Docker `HEALTHCHECK` directive do?
**Answer:** It configures the Docker daemon to periodically run a command inside the container (e.g. `wget -q http://localhost:8080/actuator/health`). If it fails repeatedly, Docker marks the container status as `(unhealthy)`, allowing orchestrators to restart or redirect traffic.

---

### Section 7: Ansible Configuration Management & Reliability

#### Q20: What is Ansible and why is it considered "agentless"?
**Answer:** Ansible is an open-source Configuration Management and Infrastructure as Code tool that automates provisioning and application deployment using declarative YAML playbooks. It is agentless because it connects over standard SSH or local connections and requires no background daemon or agent software on target nodes.

#### Q21: What is Idempotency and how did you prove it in Milestone 14?
**Answer:** Idempotency means executing an automation playbook multiple times results in the exact same system state with zero side effects. On our first playbook run, Ansible reported `changed=10, failed=0`. On the second consecutive run, it reported `changed=0, failed=0`, proving complete idempotency.

#### Q22: How does your emergency rollback mechanism work?
**Answer:** In Ansible (`ansible/rollback.yml`), the playbook stops the degraded service, restores the previous stable `.jar.backup` file, restarts the systemd service, and polls `/actuator/health` to confirm the service is `UP`. In Docker (`scripts/rollback.ps1`), it stops the degraded container and spins up the previous versioned image tag in under 30 seconds.

---

### Section 8: Rapid-Fire Summary Questions

| Question | Short Answer |
| :--- | :--- |
| **Default Application Port?** | `8080` (Configurable via `-Dserver.port`) |
| **Jenkins Service Port?** | `8085` (To prevent conflict with app port 8080) |
| **Health Check Endpoint?** | `http://localhost:8080/actuator/health` |
| **Total Automated Tests?** | 11 Tests (6 Unit + 5 Selenium UI Journeys) |
| **Docker Base Image?** | `eclipse-temurin:21-jre-alpine` (178 MB) |
| **Release Tag?** | `v1.0.0-mvp` |
