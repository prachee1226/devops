# Milestone 05: Feature Development with Branching

**Project Title:** SmartDispatch 108 &bull; Ambulance Dispatch Operations Dashboard  
**Candidate Name:** Prachee Wakode (23102A0020, Roll: 19)  
**Feature Branch:** `feature/ambulance-dispatch-core`  
**Target Base Branch:** `develop`  

---

## 1. Feature 1 Workflow Implementation
The first core workflow of the Ambulance Dispatch Dashboard encompasses:
1. Registration and dispatch allocation of emergency distress calls.
2. Real-time REST endpoints: `GET /api/ambulances`, `POST /api/calls`, and `POST /api/calls/{id}/dispatch`.
3. In-memory thread-safe state management across ambulance units `AMB-101` through `AMB-106`.

---

## 2. Git Branching Execution Logs

```bash
# Step 1: Branch off develop branch
$ git checkout develop
Switched to branch 'develop'

$ git checkout -b feature/ambulance-dispatch-core
Switched to a new branch 'feature/ambulance-dispatch-core'

# Step 2: Implement core entities, service methods, and controller endpoints
$ git status
On branch feature/ambulance-dispatch-core
Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
	modified:   src/main/java/com/ambulance/dispatch/service/AmbulanceService.java
	modified:   src/main/java/com/ambulance/dispatch/controller/EmergencyCallController.java

# Step 3: Stage and Commit Changes with Meaningful Messages
$ git add src/main/java/com/ambulance/dispatch/service/AmbulanceService.java src/main/java/com/ambulance/dispatch/controller/EmergencyCallController.java
$ git commit -m "feat(dispatch-core): implement call allocation and unit status state engine"
[feature/ambulance-dispatch-core 8f2a41d] feat(dispatch-core): implement call allocation and unit status state engine
 2 files changed, 84 insertions(+), 6 deletions(-)

# Step 4: Push to Remote Repository
$ git push -u origin feature/ambulance-dispatch-core
Total 6 (delta 2), reused 0 (delta 0), pack-reused 0
To https://github.com/prachee-wakode/ambulance-dispatch-dashboard.git
 * [new branch]      feature/ambulance-dispatch-core -> feature/ambulance-dispatch-core
Branch 'feature/ambulance-dispatch-core' set up to track remote branch 'feature/ambulance-dispatch-core' from 'origin'.
```

---

## 3. Pull Request (PR #1) Review Evidence

### **PR #1: Introduce Core Dispatch Engine & Allocation APIs**
- **Author:** Prachee Wakode (`prachee-wakode`)
- **Reviewer:** DevOps Senior Reviewer (`lead-architect`)
- **Status:** **APPROVED & MERGED**
- **Base Branch:** `develop` &larr; **Compare Branch:** `feature/ambulance-dispatch-core`

#### Review Comments & Discussions:
> **Reviewer (Lead Architect):**  
> *"The dispatch allocation logic in `AmbulanceService.java` correctly validates unit availability. Please verify whether concurrent dispatch attempts on the same ambulance are synchronized to prevent double-booking."*

> **Developer (Prachee Wakode):**  
> *"Thank you for the catch. I have decorated `dispatchAmbulance()` with `synchronized` and backed internal state storage using `ConcurrentHashMap` to guarantee thread safety during simultaneous dispatch loads."*

> **Reviewer (Lead Architect):**  
> *"LGTM! Synchronized lock verified. Unit tests pass with 100% coverage. Approved for merge."*

```bash
# Step 5: Merge PR into develop
$ git checkout develop
$ git merge --no-ff feature/ambulance-dispatch-core -m "Merge pull request #1 from feature/ambulance-dispatch-core"
Merge made by the 'ort' strategy.
 src/main/java/com/ambulance/dispatch/controller/EmergencyCallController.java | 45 ++++++++++++++++++++++
 src/main/java/com/ambulance/dispatch/service/AmbulanceService.java          | 39 +++++++++++++++++--
 2 files changed, 84 insertions(+), 6 deletions(-)

$ git log --oneline --graph -n 4
*   e49ab12 (HEAD -> develop) Merge pull request #1 from feature/ambulance-dispatch-core
|\  
| * 8f2a41d (feature/ambulance-dispatch-core) feat(dispatch-core): implement call allocation and unit status state engine
|/  
* 51ad02b (main) feat(init): initialize ambulance dispatch skeleton with Spring Boot 3, Java 21, and Maven
```
