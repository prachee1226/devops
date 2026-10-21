# Milestone 04: Git and GitHub Repository Initialization

**Project Title:** SmartDispatch 108 &bull; Ambulance Dispatch Operations Dashboard  
**Candidate Name:** Prachee Wakode (23102A0020, Roll: 19)  
**Repository Name:** `ambulance-dispatch-dashboard`  
**GitHub URL Pattern:** `https://github.com/prachee-wakode/ambulance-dispatch-dashboard.git`  

---

## 1. Branch Naming Policy & Git Flow

To guarantee code stability and facilitate collaborative DevOps teams, the following branch taxonomy is strictly enforced:

| Branch Type | Naming Pattern | Purpose | Example |
| :--- | :--- | :--- | :--- |
| **Main / Production** | `main` | Production-ready baseline, tagged with semantic versions (`v1.0.0-mvp`). Deploys to live Docker container. | `main` |
| **Development** | `develop` | Integration branch where tested feature branches merge. Triggers nightly regression tests. | `develop` |
| **Feature Branch** | `feature/<jira-or-task-id>-<short-description>` | Isolated branch for building individual MVP user stories. | `feature/TSK-05-ambulance-dispatch-core` |
| **Bugfix / Patch** | `bugfix/<issue-id>-<defect-summary>` | Branch created to resolve defects flagged by Jenkins Selenium gates. | `bugfix/BUG-101-alert-unhandled-dialog` |
| **Hotfix Branch** | `hotfix/v<x.y.z>-<critical-fix>` | Emergency patch branched directly from `main`. | `hotfix/v1.0.1-security-patch` |

---

## 2. Protected Branch Rules & Commit Standards

### Branch Protection Rules for `main` and `develop`:
1. **Pull Request Required:** No direct pushes allowed to `main` or `develop`.
2. **Mandatory Peer Reviews:** At least 1 code review approval required before merge.
3. **Status Checks Must Pass:** Jenkins CI webhook must report successful compilation and 100% unit/Selenium test execution.
4. **Linear Git History:** Merge via Pull Request with standard squash or merge commits.

### Conventional Commit Message Convention:
All commit messages must adhere to the standard format:
`<type>(<scope>): <concise present-tense subject>`

- `feat(dispatch)`: Add emergency call intake and auto-dispatch logic
- `test(selenium)`: Implement E2E headless journeys for fleet filtering
- `fix(ui)`: Replace blocking browser alerts with non-blocking toast notifications
- `ci(jenkins)`: Introduce declarative Jenkinsfile with multi-stage quality gates
- `docker(build)`: Add multi-stage Alpine JRE Dockerfile

---

## 3. Git Initialization Script & Log Evidence

```powershell
# Initializing Git Repository
cd C:\Users\Prachee\.gemini\antigravity\scratch\ambulance-dispatch-dashboard
git init -b main

# Configuring User Identity
git config user.name "Prachee Wakode"
git config user.email "23102A0020@student.institution.edu"

# Stage and Initial Commit
git add .
git commit -m "feat(init): initialize ambulance dispatch skeleton with Spring Boot 3, Java 21, and Maven"

# Create develop branch
git branch develop
git checkout develop
```
