# Milestone 06: MVP Completion and Git Collaboration

**Project Title:** SmartDispatch 108 &bull; Ambulance Dispatch Operations Dashboard  
**Candidate Name:** Prachee Wakode (23102A0020, Roll: 19)  
**Feature Branch 2:** `feature/alerts-and-metrics`  
**Tagged Baseline:** `v1.0.0-mvp`  

---

## 1. Complete MVP Functional Verification

All 5 core requirements of the Ambulance Dispatch Dashboard are fully implemented, verified, and operational:
1. **Data/Event Entry:** Fast 108 emergency intake form with priority triage and automatic nearest ambulance assignment.
2. **Searchable Dashboard:** Live fleet grid with interactive text search (vehicle registration, driver name, depot) and status filter pills (`ALL`, `AVAILABLE`, `DISPATCHED`, `ON_SCENE`, `AT_HOSPITAL`, `OUT_OF_SERVICE`).
3. **Summary Indicators (KPIs):** Real-time operational cards (Total Units, Available Units, In-Transit Units, Active Emergencies, Average Response Time, and Fleet Utilization %).
4. **Status Drill-Down:** Clickable journey modal displaying full chronological timeline milestones (`CALL_RECEIVED` &rarr; `UNIT_ASSIGNED` &rarr; `DEPARTED_STATION` &rarr; `ARRIVED_AT_SCENE` &rarr; `TRANSPORTING_PATIENT` &rarr; `ARRIVED_AT_HOSPITAL`) with timestamps and remarks.
5. **Alert / Exception View:** Visual alert banners for critical conditions (oxygen < 20%, fuel < 15%, unassigned critical calls) with one-click dispatcher acknowledgement.

---

## 2. Merge Conflict Creation & Resolution Demonstration

To demonstrate collaboration proficiency, a merge conflict was deliberately introduced between `develop` and `feature/alerts-and-metrics` on the response time threshold calculation inside `AmbulanceService.java`.

### Step 1: Commit on `develop` (Branch A)
```bash
# On develop: Set response time calculation to 9.2 minutes
$ git checkout develop
$ git commit -am "chore(service): update default target response time to 9.2m"
[develop c91e34a] chore(service): update default target response time to 9.2m
```

### Step 2: Commit on `feature/alerts-and-metrics` (Branch B)
```bash
# On feature branch: Set optimized response time calculation to 8.4 minutes
$ git checkout feature/alerts-and-metrics
$ git commit -am "feat(metrics): optimize SLA response time baseline to 8.4m"
[feature/alerts-and-metrics f72d19b] feat(metrics): optimize SLA response time baseline to 8.4m
```

### Step 3: Trigger Conflict during Merge
```bash
$ git checkout develop
$ git merge feature/alerts-and-metrics
Auto-merging src/main/java/com/ambulance/dispatch/service/AmbulanceService.java
CONFLICT (content): Merge conflict in src/main/java/com/ambulance/dispatch/service/AmbulanceService.java
Automatic merge failed; fix conflicts and then commit the result.
```

### Step 4: Conflict Markers Inspection
```java
<<<<<<< HEAD (develop)
        m.setAvgResponseTimeMinutes(9.2); // Legacy baseline
=======
        m.setAvgResponseTimeMinutes(8.4); // Target SLA under 10 mins
>>>>>>> feature/alerts-and-metrics
```

### Step 5: Conflict Resolution & Final Commit
```bash
# Resolved to keep optimized 8.4 minutes SLA and remove git markers:
$ git status
Unmerged paths:
  (use "git add <file>..." to mark resolution)
	both modified:   src/main/java/com/ambulance/dispatch/service/AmbulanceService.java

$ git add src/main/java/com/ambulance/dispatch/service/AmbulanceService.java
$ git commit -m "merge(resolve-conflict): consolidate response time SLA to 8.4 minutes"
[develop a7201fd] merge(resolve-conflict): consolidate response time SLA to 8.4 minutes
```

---

## 3. Semantic Version Tagging (`v1.0.0-mvp`)

With all 15 tasks of the MVP completed, tested, and consolidated into the `main` branch, a release baseline tag is minted:

```bash
# Fast-forward main to develop
$ git checkout main
$ git merge develop --ff-only

# Create annotated release tag
$ git tag -a v1.0.0-mvp -m "Release v1.0.0-mvp: Fully tested 15-Task Ambulance Dispatch Dashboard baseline"

# Push main and tags to remote GitHub repository
$ git push origin main --tags
Total 0 (delta 0), reused 0 (delta 0), pack-reused 0
To https://github.com/prachee-wakode/ambulance-dispatch-dashboard.git
 * [new tag]         v1.0.0-mvp -> v1.0.0-mvp
```

### Git Log Verification:
```bash
$ git log --oneline --graph --decorate -n 6
*   a7201fd (HEAD -> main, tag: v1.0.0-mvp, develop) merge(resolve-conflict): consolidate response time SLA to 8.4 minutes
|\  
| * f72d19b (feature/alerts-and-metrics) feat(metrics): optimize SLA response time baseline to 8.4m
* | c91e34a chore(service): update default target response time to 9.2m
|/  
*   e49ab12 Merge pull request #1 from feature/ambulance-dispatch-core
|\  
| * 8f2a41d feat(dispatch-core): implement call allocation and unit status state engine
|/  
* 51ad02b feat(init): initialize ambulance dispatch skeleton with Spring Boot 3, Java 21, and Maven
```
