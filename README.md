# SmartDispatch 108 &bull; Ambulance Dispatch Operations Dashboard

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Build-Maven%203.9-blue.svg)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](https://www.docker.com/)
[![Ansible](https://img.shields.io/badge/Automation-Ansible-EE0000.svg)](https://www.ansible.com/)
[![Jenkins](https://img.shields.io/badge/CI%2FCD-Jenkins%20Pipeline-D24939.svg)](https://www.jenkins.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Student Name:** Prachee Wakode  
**Roll No:** 23102A0020   
**Assignment:** Jenkins Pipeline for an Ambulance Dispatch Dashboard (End-to-End 15-Milestone DevOps Project)

---

## 📌 Project Overview
SmartDispatch 108 is a real-time emergency healthcare fleet operations dashboard designed to eliminate dispatch delays, reduce emergency response latency (< 10 minutes), and monitor ambulance vitals (oxygen cylinder pressure, vehicle fuel, location, and destination emergency rooms).

Built with a complete, enterprise-grade Continuous Integration, Continuous Delivery (CI/CD), and Infrastructure as Code (IaC) DevOps lifecycle using Git, Jenkins, Selenium WebDriver, Docker, and Ansible.

---

## 🚀 Key Application Capabilities (The 5 Core MVP Functions)
1. **Data & Event Entry:** Rapid intake form for emergency 108 calls with automated triage scoring (Code Red Cardiac, Trauma, Road Accidents) and instantaneous available unit allocation.
2. **Searchable Fleet Dashboard:** Interactive filtering by status (`AVAILABLE`, `DISPATCHED`, `ON_SCENE`, `AT_HOSPITAL`, `OUT_OF_SERVICE`) and instant text search across driver names, vehicle registration numbers, and depots.
3. **Summary Indicators (KPIs):** Real-time metrics tracking Total Fleet Units, Available Units, In-Transit Units, Active Emergencies, Average Response Time, and Fleet Utilization %.
4. **Status Drill-Down Timeline:** Clickable chronological timeline tracking individual ambulance journeys from dispatch assignment to patient ER handover with operator timestamps and audit remarks.
5. **Real-Time Alert & Exception View:** Visual warning banner flagging vehicles with critical oxygen levels (<20%), low fuel (<15%), or unattended Code Red emergencies, with one-click dispatcher acknowledgement.

---

## 🛠️ DevOps Technology Stack
- **Source Code Management:** Git & GitHub (GitFlow branching, PR reviews, release tagging `v1.0.0-mvp`)
- **Build & Dependency Management:** Apache Maven 3.9+ & Java 21 LTS
- **Automated Testing & Quality Gates:** JUnit 5 (REST / Controller Unit Tests) & Selenium WebDriver 4 (Headless E2E Journeys with failure screenshot capture)
- **Continuous Integration & Delivery:** Jenkins Declarative Pipeline (`Jenkinsfile` & `Jenkinsfile.docker`)
- **Containerization:** Multi-stage `Dockerfile` (`eclipse-temurin:21-jre-alpine`)
- **Configuration Management & Provisioning:** Ansible Playbook (`site.yml` & `rollback.yml`) with idempotency and health check validation.

---

## 📁 Repository Structure
```
ambulance-dispatch-dashboard/
├── .github/
│   └── ISSUE_TEMPLATE/          # GitHub Issue & Bug Report Templates
├── ansible/
│   ├── hosts.ini                # Target inventory
│   ├── playbook.yml             # Server provisioning & prerequisite installation
│   └── rollback.yml             # Idempotent rollback script to previous stable release
├── docker/
│   ├── Dockerfile               # Multi-stage production Dockerfile
│   └── docker-compose.yml       # Production deployment specification
├── docs/                        # Complete 15-Milestone DevOps Documentation
│   ├── 01-problem-definition-and-scope.md
│   ├── 02-agile-planning-and-devops-workflow.md
│   ├── 03-requirements-and-architecture.md
│   ├── 04-git-and-branching-policy.md
│   ├── 05-feature-branch-workflow.md
│   ├── 06-mvp-completion-and-conflict-resolution.md
│   ├── 07-jenkins-ci-setup.md
│   ├── 08-pipeline-as-code-tomcat.md
│   ├── 09-selenium-test-plan-and-execution.md
│   ├── 10-continuous-testing-quality-gate.md
│   ├── 11-docker-container-lifecycle.md
│   ├── 12-jenkins-docker-cd-pipeline.md
│   ├── 13-ansible-configuration-management.md
│   ├── 14-automated-provisioning-and-reliability.md
│   ├── FINAL_PROJECT_REPORT.md  # Comprehensive academic/viva submission report
│   ├── VIVA_PRESENTATION.md     # Presentation slide deck outline
│   └── VIVA_QUESTIONS_AND_ANSWERS.md # 30+ Viva Voce questions & model answers
├── jenkins/
│   ├── Jenkinsfile              # Declarative CI/CD pipeline (Jar/Tomcat target)
│   ├── Jenkinsfile.docker       # Declarative Containerized CI/CD pipeline
│   └── jenkins-ci-config.xml    # Freestyle job XML configuration template
├── scripts/
│   ├── docker-lifecycle.ps1     # Docker container build/run/inspect lifecycle utility
│   ├── health-check.ps1         # Automated endpoint health verification
│   └── rollback.ps1             # Local rollback script
├── src/
│   ├── main/
│   │   ├── java/com/ambulance/dispatch/
│   │   │   ├── controller/      # REST API Controllers (Ambulance, Call, Dashboard)
│   │   │   ├── model/           # Data models (Ambulance, EmergencyCall, Event, Alert, Metrics)
│   │   │   ├── service/         # Business domain service & in-memory database
│   │   │   └── AmbulanceDispatchApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/          # HTML5, CSS3, JS Responsive Dashboard UI
│   └── test/
│       └── java/com/ambulance/dispatch/
│           ├── controller/      # JUnit 5 Controller & MockMvc Tests
│           └── selenium/        # Headless Selenium WebDriver E2E Journey Tests
├── pom.xml                      # Maven build file with plugins and dependencies
└── README.md                    # Project README
```

---

## ⚡ Quick Start (Local Run)

### 1. Compile and Execute Tests
```powershell
mvn clean test
```

### 2. Run the Application
```powershell
mvn spring-boot:run
```
Open **`http://localhost:8080`** in any web browser.

### 3. Build & Run with Docker
```powershell
docker build -t ambulance-dispatch:1.0.0 -f docker/Dockerfile .
docker run -d -p 8080:8080 --name smart-dispatch ambulance-dispatch:1.0.0
```

---

## 👩‍💻 Author & Academic Attribution
- **Candidate:** Prachee Wakode
- **PRN:** 23102A0020 (Roll No. 19)
- **Course:** DevOps CI/CD & Cloud Automation
- **Institution:** Department of Computer Engineering
