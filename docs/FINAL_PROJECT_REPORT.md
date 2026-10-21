# SMARTDISPATCH 108: REAL-TIME AMBULANCE DISPATCH OPERATIONS DASHBOARD
## Comprehensive DevOps Engineering Project Report & System Architecture Specification

---

**Student Name:** Prachee Wakode  
**PRN / Roll Number:** 23102A0020 (Roll No. 19)  
**Academic Course:** DevOps CI/CD Pipelines & Cloud Automation  
**Department:** Computer Engineering  
**Academic Year:** 2026  
**Repository Name:** `ambulance-dispatch-dashboard`  

---

## TABLE OF CONTENTS
1. [Executive Summary & Problem Statement](#1-executive-summary--problem-statement)
2. [Target Users, Stakeholders & Measurable Goals](#2-target-users-stakeholders--measurable-goals)
3. [Agile Planning & Scrum Methodology](#3-agile-planning--scrum-methodology)
4. [Software Architecture & Technology Stack](#4-software-architecture--technology-stack)
5. [The 5 Core MVP Functional Modules](#5-the-5-core-mvp-functional-modules)
6. [Git Workflow, Feature Branching & Conflict Resolution](#6-git-workflow-feature-branching--conflict-resolution)
7. [Jenkins Continuous Integration Pipeline (CI)](#7-jenkins-continuous-integration-pipeline-ci)
8. [Automated Quality Gates & Selenium WebDriver E2E Suite](#8-automated-quality-gates--selenium-webdriver-e2e-suite)
9. [Docker Containerization & Lifecycle Engineering](#9-docker-containerization--lifecycle-engineering)
10. [Continuous Deployment Pipeline (Jenkins-Docker CD)](#10-continuous-deployment-pipeline-jenkins-docker-cd)
11. [Configuration Management & Automated Provisioning (Ansible)](#11-configuration-management--automated-provisioning-ansible)
12. [Reliability Engineering, Idempotency & Automated Rollback](#12-reliability-engineering-idempotency--automated-rollback)
13. [System Troubleshooting & Operational Guide](#13-system-troubleshooting--operational-guide)
14. [Current Limitations & Future Enhancement Roadmap](#14-current-limitations--future-enhancement-roadmap)
15. [Academic Conclusion & Key Takeaways](#15-academic-conclusion--key-takeaways)

---

## 1. Executive Summary & Problem Statement

In emergency medical services (EMS), every minute of delay before medical stabilization reduces patient survival rates by up to 10%. The emergency 108/112 ambulance network across urban sectors faces severe operational challenges:
- **Manual, Disjointed Dispatching:** Call takers manually record incoming emergency calls and spend vital minutes calling regional depots to find available vehicles.
- **Lack of Fleet Telemetry:** Dispatch coordinators cannot view real-time fleet availability, in-transit progress, or vehicle vitals (such as critical oxygen levels or low fuel).
- **Fragile Deployment Lifecycles:** Traditional hospital IT infrastructure relies on manual script copying, resulting in software outages during peak emergency hours.

**SmartDispatch 108** solves these challenges by combining an event-driven Java 21 / Spring Boot 3 real-time dashboard with an automated, end-to-end DevOps automation pipeline. Every commit triggers automated compilation, JUnit unit testing, headless Selenium UI verification, Docker container packaging, and Ansible automated deployment.

---

## 2. Target Users, Stakeholders & Measurable Goals

### 2.1 Key Stakeholder Matrix
- **108 Emergency Dispatch Operator:** Requires sub-30 second emergency intake, auto-triage (Code Red/Amber/Green), and instant one-click ambulance assignment.
- **Fleet Operations Commander:** Requires real-time visibility over fleet distribution, unit statuses (`AVAILABLE`, `DISPATCHED`, `ON_SCENE`, `AT_HOSPITAL`), and operational KPIs.
- **Paramedic & Field Crew:** Requires clear dispatch logs and chronological journey status progression.
- **Receiving Hospital Emergency Room (ER):** Requires advance notification of incoming critical trauma or cardiac cases.
- **DevOps Engineer (Prachee Wakode):** Responsible for zero-downtime automated pipelines, regression quality gates, and containerized rollback.

### 2.2 Measurable Success Metrics
- **Call Allocation Latency:** Target < 45 seconds from call creation to dispatch.
- **Average Response Time:** Target < 10.0 minutes (MVP simulation achieves 8.4 minutes).
- **Automated Quality Gate:** 100% test pass rate across all 11 unit and Selenium tests before release promotion.
- **Recovery Time Objective (RTO):** Revert any degraded deployment via automated rollback in < 30 seconds.

---

## 3. Agile Planning & Scrum Methodology

The project was executed across four structured sprints using a 15-task product backlog.
- **Sprint 1 (Foundations):** Problem scope, requirements, data model, Git repository initialization, and Spring Boot architecture.
- **Sprint 2 (Core Development & CI):** Branching workflows, dispatch core engine, merge conflict demonstration, release tagging `v1.0.0-mvp`, and Jenkins CI configuration.
- **Sprint 3 (Testing & Containers):** Selenium headless test journeys, failure screenshot capture, continuous testing quality gate, and multi-stage Docker containerization.
- **Sprint 4 (CD, IaC & Release):** Jenkins-Docker CD, Ansible configuration management, idempotency testing, automated health rollback, and viva defense documentation.

---

## 4. Software Architecture & Technology Stack

The system follows a modern multi-tiered microservice architecture:

| Tier | Component | Technology | Responsibility |
| :--- | :--- | :--- | :--- |
| **Presentation Tier** | Web UI Console | HTML5, CSS3, Vanilla JS, FontAwesome | Searchable grid, dispatch forms, KPI cards, drilldown modal, alert banners |
| **Application Tier** | REST Controllers | Spring Boot 3.2.5, Java 21 LTS | REST endpoints for fleet, calls, dashboard metrics, and health pings |
| **Service Tier** | Business Domain | `AmbulanceService.java` | In-memory thread-safe state storage (`ConcurrentHashMap`), dispatch allocation |
| **Monitoring Tier** | Actuator | Spring Boot Actuator | Real-time endpoint `/actuator/health` for automated smoke validation |
| **Build Automation** | Build Engine | Apache Maven 3.9.16 | Declarative dependency management and surefire test reports |
| **CI/CD Tier** | Pipeline as Code | Jenkins (Pipeline Plugin) | Automated checkout, build, quality gate, package, and container push |
| **Container Tier** | Container Engine | Docker Engine & Docker Compose | Multi-stage Alpine containerization (`eclipse-temurin:21-jre-alpine`) |
| **IaC Tier** | Configuration Mgmt | Ansible | Agentless host provisioning, idempotency validation, and rollback playbooks |

---

## 5. The 5 Core MVP Functional Modules

1. **Data & Event Entry:** Captures emergency distress calls (Caller name, phone, emergency type, triage priority, location) and assigns available ambulances immediately.
2. **Searchable Dashboard:** Interactive search bar with instant debounce filtering across drivers, vehicles, and depots, with one-click status filter pills (`ALL`, `AVAILABLE`, `DISPATCHED`, `ON_SCENE`, `AT_HOSPITAL`, `OUT_OF_SERVICE`).
3. **Summary Indicators (KPIs):** Real-time operational cards tracking Total Fleet Units (6), Available Units, In-Transit Units, Active Emergencies, Average Response Time (8.4m), and Fleet Utilization %.
4. **Status Drill-Down:** Chronological event timeline modal tracking ambulance dispatch events (`CALL_RECEIVED` &rarr; `UNIT_ASSIGNED` &rarr; `DEPARTED_STATION` &rarr; `ARRIVED_AT_SCENE` &rarr; `TRANSPORTING_PATIENT` &rarr; `ARRIVED_AT_HOSPITAL`).
5. **Alert & Exception View:** Real-time banner highlighting critical exceptions (oxygen tank pressure < 20%, low fuel < 15%, or unassigned Code Red cardiac calls) with operator acknowledgement buttons.

---

## 6. Git Workflow, Feature Branching & Conflict Resolution

- **Branching Taxonomy:** Standard GitFlow model utilizing `main`, `develop`, `feature/*`, and `bugfix/*` branches.
- **Pull Request Review:** Simulated peer review workflow between developer and lead architect with discussions on thread safety and `synchronized` locks.
- **Merge Conflict Resolution:** Deliberately created conflicting changes on the response time threshold calculation between `develop` (9.2m) and `feature/alerts-and-metrics` (8.4m). Successfully resolved by keeping the optimized SLA and committing clean code.
- **Release Baseline Tag:** Semantic tag `v1.0.0-mvp` minted on the `main` branch.

---

## 7. Jenkins Continuous Integration Pipeline (CI)

A Freestyle CI job and Declarative Pipeline were configured with:
- **Build Triggers:** SCM polling (`H/5 * * * *`) and GitHub webhook integration.
- **Build Steps:** `mvn clean test package`.
- **Post-Build Actions:** JUnit test result archiving (`**/target/surefire-reports/*.xml`) and executable fat JAR archiving (`target/*.jar`).

---

## 8. Automated Quality Gates & Selenium WebDriver E2E Suite

The automated test suite contains 11 tests:
- **6 Spring Boot Controller / MockMvc Unit Tests:** Verifying REST endpoints for ambulance listing, status updates, emergency call intake, dispatch allocation, metrics, and alerts.
- **5 Headless Selenium WebDriver User Journeys:**
  - Journey 1: Dashboard initialization and KPI indicator cards verification.
  - Journey 2: Instant keyboard search and status filter pills verification.
  - Journey 3: Quick emergency intake and dispatch notification verification.
  - Journey 4: Status drill-down timeline modal rendering and milestone verification.
  - Journey 5: Critical alert and exception triage acknowledgement.
- **Failure Screenshot Capture:** Automatically captures high-resolution PNG screenshots on test failure using `TakesScreenshot` into `target/screenshots/`.
- **Quality Gate:** Failing any test halts pipeline execution and aborts packaging or container deployment.

---

## 9. Docker Containerization & Lifecycle Engineering

- **Multi-Stage Build:**
  - Stage 1: `eclipse-temurin:21-jdk-alpine` compiles source code and packages the fat JAR.
  - Stage 2: `eclipse-temurin:21-jre-alpine` creates an ultra-lightweight 178MB production runtime container.
- **Container Security:** Runs under an unprivileged service account `spring:spring` without root permissions.
- **Container Health Check:** Built-in `HEALTHCHECK` directive monitoring `/actuator/health` every 15 seconds.
- **Lifecycle Script:** `scripts/docker-lifecycle.ps1` automating build, run, logs, inspect, stop, restart, and clean operations.

---

## 10. Continuous Deployment Pipeline (Jenkins-Docker CD)

The extended `Jenkinsfile.docker` automates:
1. SCM Checkout of `main` branch.
2. Execution of the automated test suite as a release quality gate.
3. Building a versioned Docker image: `ambulance-dispatch:1.0.0-${BUILD_NUMBER}`.
4. Publishing the versioned image to the remote container registry.
5. Deploying a fresh container on port 8080 with automated health check verification.

---

## 11. Configuration Management & Automated Provisioning (Ansible)

The Ansible playbook (`ansible/playbook.yml`) provisions target Linux nodes:
- Installs Java 21, curl, and UFW firewall.
- Creates dedicated system user and group `dispatchsvc`.
- Sets up directory structures with strict file permissions (`0755` / `0750`).
- Deploys systemd service unit `/etc/systemd/system/ambulance-dispatch.service`.
- Configures firewall rule allowing port `8080/tcp`.
- Enables, starts, and verifies the service via automated health check.

---

## 12. Reliability Engineering, Idempotency & Automated Rollback

- **Idempotency Proof:** Rerunning `ansible-playbook -i ansible/hosts.ini ansible/playbook.yml` produces `changed=0, failed=0`, proving complete idempotency.
- **Automated Health Monitoring:** `scripts/health-check.ps1` polls `/actuator/health` to confirm `UP` status before traffic routing.
- **Emergency Rollback Procedure:** `ansible/rollback.yml` and `scripts/rollback.ps1` immediately revert live containers or services to the previous stable release artifact within 30 seconds.

---

## 13. System Troubleshooting & Operational Guide

| Symptom / Error | Likely Root Cause | Resolution Procedure |
| :--- | :--- | :--- |
| **Port Conflict on Port 8080** | Previous Tomcat or container already bound to port 8080. | Stop running process via `docker stop smartdispatch-108` or run on custom port via `-Dserver.port=9090`. |
| **Selenium Test Failures on Headless Node** | Missing OS display driver or unhandled browser dialogs. | Use `--headless=new` Chrome flag and replace native browser alerts with non-blocking toast notifications. |
| **Jenkins SCM Polling Fails** | Invalid Git credentials or repository URL. | Verify GitHub Personal Access Token (PAT) configured in Jenkins credentials store. |
| **Docker Build Out of Memory** | Alpine builder memory limit exceeded. | Provide `JAVA_OPTS="-Xms256m -Xmx512m"` to restrict JVM heap allocation. |

---

## 14. Current Limitations & Future Enhancement Roadmap

### Current Limitations:
1. Fleet GPS coordinates are simulated and updated via in-memory event queues rather than physical hardware GPS transponders.
2. Authentication uses local dispatch operator roles without multi-factor biometric authentication.

### Future Roadmap:
1. **IoT Telemetry Ingestion:** Integrate MQTT / WebSockets for live vehicle telemetry streaming from on-board OBD-II sensors.
2. **AI-Driven Route Optimization:** Implement Google Maps Distance Matrix API with traffic prediction to route ambulances dynamically.
3. **Kubernetes Orchestration:** Migrate single Docker container deployment to Kubernetes (EKS/GKE) with Horizontal Pod Autoscaling (HPA).

---

## 15. Academic Conclusion & Key Takeaways

The **SmartDispatch 108 Ambulance Dispatch Operations Dashboard** demonstrates a comprehensive, end-to-end implementation of modern DevOps engineering. By integrating Java 21, Spring Boot, Maven, Selenium WebDriver, Jenkins Pipeline as Code, Docker multi-stage containerization, and Ansible configuration management, the project bridges the gap between software development and production-grade operations reliability.
