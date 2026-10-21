# SmartDispatch 108: Viva Voce Presentation Deck

**Project Title:** Jenkins Pipeline for an Ambulance Dispatch Dashboard  
**Candidate Name:** Prachee Wakode  
**Student PRN / Roll:** 23102A0020 / Roll 19  
**Duration:** 15-Minute Viva Presentation  

---

## Slide 1: Title & Candidate Information
- **Title:** SmartDispatch 108 &bull; Real-Time Ambulance Dispatch Operations Dashboard
- **Topic:** End-to-End DevOps Pipeline Architecture (Git, Jenkins, Selenium, Docker, Ansible)
- **Candidate:** Prachee Wakode (PRN: 23102A0020, Roll: 19)
- **Course:** DevOps Engineering & Continuous Automation

---

## Slide 2: Problem Statement & Real-Time Need
- Medical emergency survival drops sharply after the "Golden Hour".
- Urban ambulance operations suffer from manual call logging, lack of vehicle telemetry, and zero visibility into critical supplies (oxygen levels).
- Traditional hospital deployments are manual, error-prone, and slow to recover from regressions.
- **Our Solution:** A real-time web dispatch console combined with a 15-stage automated CI/CD and IaC pipeline.

---

## Slide 3: The 5 Core MVP Functional Modules
1. **Data/Event Entry:** Fast 108 emergency call intake and triage rating.
2. **Searchable Dashboard:** Real-time search across drivers/depots and status filtering pills (`AVAILABLE`, `DISPATCHED`, `ON_SCENE`, `AT_HOSPITAL`, `OUT_OF_SERVICE`).
3. **Summary Indicators (KPIs):** Real-time operational metric cards (Active calls, fleet count, avg response time, utilization %).
4. **Status Drill-Down:** Clickable timeline tracking individual ambulance dispatch milestones.
5. **Alert / Exception View:** Instant visual flags when oxygen < 20% or unassigned Code Red calls occur.

---

## Slide 4: System Architecture & Technology Stack
- **Backend:** Java 21 LTS + Spring Boot 3.2.5 + Embedded Tomcat
- **Frontend:** Responsive HTML5 + CSS3 + Vanilla JavaScript + FontAwesome
- **Build Tool:** Apache Maven 3.9.16
- **Testing:** JUnit 5 (REST APIs) + Selenium WebDriver 4 (Headless UI Journeys)
- **CI/CD:** Jenkins Declarative Pipeline (`Jenkinsfile`)
- **Containers:** Multi-Stage Dockerfile (`eclipse-temurin:21-jre-alpine`)
- **Config Management:** Ansible Playbook (`playbook.yml`, `rollback.yml`)

---

## Slide 5: Git Branching & Collaboration Workflow
- GitFlow branching strategy: `main`, `develop`, `feature/*`, `bugfix/*`.
- Pull Request review simulation with discussions on concurrency and `synchronized` locks.
- Merge conflict demonstration: Deliberate collision on response time SLA resolved and committed.
- Semantic release tagging: `v1.0.0-mvp`.

---

## Slide 6: Continuous Integration & Jenkins Pipeline
- Freestyle job & Declarative `Jenkinsfile` automated builds.
- Triggers: SCM Polling (`H/5 * * * *`) and GitHub webhooks.
- Multi-stage pipeline: Checkout &rarr; Build & Compile &rarr; Continuous Testing &rarr; Package &rarr; Deploy &rarr; Health Check.
- Automated artifact archiving of executable JAR.

---

## Slide 7: Automated Testing & Continuous Quality Gates
- **11 Automated Tests:** 6 JUnit controller unit tests + 5 Selenium WebDriver user journey tests.
- **Failure Screenshot Capture:** Automatically captures PNG screenshots on UI test failures.
- **Strict Quality Gate:** Any test regression aborts pipeline deployment automatically.
- Defect injection and resolution demonstrated (replacing blocking alerts with toast notifications).

---

## Slide 8: Docker Containerization & Lifecycle
- Two-stage multi-stage Docker build reducing final image size to 178MB.
- Non-root user `spring:spring` for enhanced container security.
- Built-in `HEALTHCHECK` directive monitoring `/actuator/health`.
- Comprehensive lifecycle management script (`scripts/docker-lifecycle.ps1`).

---

## Slide 9: Jenkins-Docker Continuous Deployment (CD)
- `Jenkinsfile.docker` builds versioned images: `ambulance-dispatch:1.0.0-${BUILD_NUMBER}`.
- Automated container redeployment with port mapping `8080:8080`.
- Smoke validation verifying container state `UP` before marking build successful.

---

## Slide 10: Infrastructure as Code & Automated Provisioning (Ansible)
- Server prerequisites automation: Java 21, curl, UFW firewall.
- System account creation (`dispatchsvc`) and permissions isolation.
- Deployment of systemd service unit `/etc/systemd/system/ambulance-dispatch.service`.
- Inbound port 8080 opened via UFW firewall.

---

## Slide 11: Idempotency, Health Checks & Automated Rollback
- **Idempotency Proof:** Second run of Ansible playbook yields `changed=0, failed=0`.
- **Health Validation:** Real-time `/actuator/health` verification.
- **Emergency Rollback:** Automated rollback playbook (`ansible/rollback.yml`) reverts degraded releases in < 30 seconds.

---

## Slide 12: Live Demonstration Highlights & Conclusion
- Live dashboard demo on `http://localhost:8080/`.
- Emergency call creation and automatic ambulance assignment.
- Timeline drill-down and oxygen warning acknowledgement.
- Jenkins pipeline build visualization and zero-downtime container upgrade.
- **Thank you! Ready for questions.**
