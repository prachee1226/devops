# Milestone 01: Problem Definition and Scope

**Project Title:** SmartDispatch 108 &bull; Real-Time Ambulance Dispatch Operations Dashboard  
**Candidate Name:** Prachee Wakode  
**Student PRN / Roll No:** 23102A0020 / Roll 19  
**Deployment Target:** Jenkins CI/CD Pipeline &bull; Docker Containerization &bull; Ansible Automated Provisioning  

---

## 1. Executive Summary & Real-Time Need
During critical medical emergencies (e.g., cardiac arrest, acute polytrauma, severe strokes, maternal emergencies), the time elapsed between the distress call and medical intervention—known as the "Golden Hour" or "Platinum 10 Minutes"—is the single most significant factor in patient survival.

Conventional emergency ambulance dispatch systems suffer from fragmented communications:
- Emergency call operators log calls manually on paper or separate telephone logs.
- Fleet location and unit status (Available vs. On-Scene vs. At Hospital) are tracked via voice radio or phone calls, resulting in frequent communication bottlenecks.
- Critical equipment states (such as oxygen cylinder pressure or low vehicle fuel) are not surfaced in real time, leading to ambulances arriving at emergencies ill-equipped.
- Lack of centralized, automated CI/CD deployment leads to delayed software fixes, manual deployment errors, and severe operational downtime.

The **SmartDispatch 108 Dashboard** provides a real-time, event-driven web dashboard engineered with an automated DevOps pipeline (Git, Jenkins, Selenium, Docker, Ansible) to guarantee high availability, continuous testing, and rapid zero-downtime deployment.

---

## 2. Target Users & Stakeholders

| Stakeholder Role | Description | Key Operational Need |
| :--- | :--- | :--- |
| **Emergency 108 Dispatch Operator** | Call center personnel receiving incoming 108/112 distress calls. | Rapid intake form (<30 seconds), automatic location tagging, instant unit availability match. |
| **Fleet Commander / Supervisor** | Central operations lead overseeing city-wide ambulance distribution. | Real-time fleet overview, utilization metrics, bottleneck tracking, priority overrides. |
| **Ambulance Driver & Paramedic Crew** | On-road first responders operating vehicles AMB-101 to AMB-106. | Clear dispatch assignment, GPS destination routing, instant status progression updates. |
| **Hospital Emergency Department (ER)** | Receiving hospital trauma triage team. | Advance notice of in-transit critical patients and estimated time of arrival (ETA). |
| **DevOps & System Reliability Engineer** | Prachee Wakode (Developer & Pipeline Engineer). | Automated Git-triggered CI builds, regression test gates, containerized deployment, and idempotency. |

---

## 3. Existing Pain Points
1. **High Call-to-Dispatch Latency:** Manual dispatch takes 4 to 8 minutes just to identify an available ambulance and relay coordinates.
2. **Lack of Visual Fleet Telemetry:** Dispatchers cannot see which unit is closest, idle, or detained at a hospital emergency bay.
3. **Equipment Blind Spots:** Ambulances with depleted oxygen tanks (<20%) or critical fuel deficits are dispatched to critical trauma cases.
4. **Fragile Deployment Processes:** Traditional hospital IT relies on manual FTP/SSH file copies, creating frequent deployment regressions and lack of rollback capabilities.

---

## 4. Measurable Success Criteria
- **Emergency Call Intake & Allocation:** Under 45 seconds from call receipt to unit assignment.
- **Average Response Time (Dispatch to On-Scene):** Target under 10.0 minutes (currently achieving 8.4 mins in MVP simulation).
- **CI/CD Quality Gate:** 100% automated build and test pass rate on Jenkins before container promotion.
- **Pipeline Execution Speed:** Under 3.5 minutes for full checkout, compilation, unit testing, and Docker container deployment.
- **Zero-Downtime Rollback:** Revert failed production deployments to the previous stable release container within 60 seconds using Ansible.

---

## 5. Constraints
- **Technical Stack:** Java 21 LTS, Spring Boot 3, Maven build automation, Selenium WebDriver 4, Docker, Ansible, and Jenkins.
- **Security & Privacy:** Patient identifying data must be sanitized; emergency dispatch coordinates restricted to authenticated operators.
- **Port Allocations:** Local port `8080` (Spring Boot / Tomcat), container port `8080:8080`, Jenkins server `8085`.

---

## 6. Approved 15-Task MVP Scope

```
[M01: Scope & Definition]  -->  [M02: Agile DevOps Plan]     -->  [M03: Arch & REST API]
         |                                |                                |
[M04: Git Repository]      -->  [M05: Feature Branch 1]      -->  [M06: Conflict & Tagging]
         |                                |                                |
[M07: Jenkins CI Job]      -->  [M08: Pipeline as Code]      -->  [M09: Selenium Testing]
         |                                |                                |
[M10: Quality Gate Fail]   -->  [M11: Docker Lifecycle]      -->  [M12: Docker Jenkins CD]
         |                                |                                |
[M13: Ansible Config]      -->  [M14: Idempotent Rollback]   -->  [M15: Final Release & Viva]
```

### The 15 Milestone Breakdown:
1. **Task 1: Problem Definition and Scope** – Stakeholder mapping, pain point analysis, success criteria, and scope freeze.
2. **Task 2: Agile Planning & DevOps Workflow** – Product backlog, 15-task Kanban board, DoD, and end-to-end DevOps lifecycle flow.
3. **Task 3: Requirements, Architecture & Tech Stack** – SRS specifications, component architecture, data model, and REST API definitions.
4. **Task 4: Git & GitHub Repository Initialization** – Repo setup, `.gitignore`, branch policy, issue templates, and initial commit baseline.
5. **Task 5: Feature Development with Branching** – Branch `feature/ambulance-dispatch-core`, PR review, and merge to `develop`.
6. **Task 6: MVP Completion & Collaboration** – Core 5 functions, branch `feature/alerts-and-metrics`, conflict resolution demo, and tag `v1.0.0-mvp`.
7. **Task 7: Jenkins CI Job Configuration** – Polling triggers, Maven build job, artifact archiving (`*.jar`).
8. **Task 8: Pipeline as Code (Jenkinsfile)** – Declarative pipeline with checkout, build, package, parameterized deployment stages.
9. **Task 9: Selenium Test Design & Execution** – 5 critical user journeys, assertions, failure screenshot capture, Maven execution.
10. **Task 10: Continuous Testing Quality Gate** – Test failure halts deployment stage, deliberate defect injection and resolution commit.
11. **Task 11: Docker Container Lifecycle** – Multi-stage `Dockerfile`, build, tag, port mapping, log inspection, container management.
12. **Task 12: Jenkins-Docker CD Pipeline** – Extension of `Jenkinsfile` to build versioned images and run fresh containers.
13. **Task 13: Configuration Management (Ansible)** – Inventory, prerequisites playbook (Java, Docker, user, firewall, app directory).
14. **Task 14: Automated Provisioning & Reliability** – Idempotency verification, automated health checks (`/actuator/health`), rollback mechanism.
15. **Task 15: Final End-to-End Release & Viva** – Technical report, architecture documentation, presentation slides, and viva examination defense.
