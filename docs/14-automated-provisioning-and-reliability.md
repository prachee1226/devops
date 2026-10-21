# Milestone 14: Automated Provisioning and Reliability Validation

**Project Title:** SmartDispatch 108 &bull; Ambulance Dispatch Operations Dashboard  
**Candidate Name:** Prachee Wakode (23102A0020, Roll: 19)  
**Reliability Goals:** Idempotency &bull; Automated Health Checks &bull; Fast Recovery Rollback  

---

## 1. Idempotency Demonstration Log

A foundational principle of Infrastructure as Code is **idempotency**: executing an automation playbook repeatedly on an already-configured target node must make **zero unnecessary modifications** and produce zero side-effects.

```bash
# Second execution on already-provisioned target node
$ ansible-playbook -i ansible/hosts.ini ansible/playbook.yml

PLAY [Configure Target Node for Ambulance Dispatch Dashboard] **************************************************

TASK [Gathering Facts] ****************************************************************************************
ok: [target-node-01]

TASK [1. Update Apt Cache (Debian/Ubuntu)] ********************************************************************
ok: [target-node-01]

TASK [2. Install Required Server Packages (Java 21, curl, ufw)] ***********************************************
ok: [target-node-01]

TASK [3. Create Dedicated Non-Root System Group] **************************************************************
ok: [target-node-01]

TASK [4. Create Dedicated Non-Root System User] ***************************************************************
ok: [target-node-01]

TASK [5. Create Application & Log Directories] ****************************************************************
ok: [target-node-01] => (item=/opt/ambulance-dispatch)
ok: [target-node-01] => (item=/opt/ambulance-dispatch/bin)
ok: [target-node-01] => (item=/var/log/ambulance-dispatch)

TASK [6. Deploy Executable Application Artifact] **************************************************************
ok: [target-node-01]

TASK [7. Deploy Systemd Service Unit] *************************************************************************
ok: [target-node-01]

TASK [8. Ensure Firewall Allows Traffic on Port 8080] **********************************************************
ok: [target-node-01]

TASK [9. Enable and Start Ambulance Dispatch Service] *********************************************************
ok: [target-node-01]

TASK [10. Verify Actuator Health Endpoint] ********************************************************************
ok: [target-node-01]

TASK [11. Display Health Status] ******************************************************************************
ok: [target-node-01] => {
    "msg": "SmartDispatch 108 Service is UP! Response: UP"
}

PLAY RECAP ****************************************************************************************************
target-node-01             : ok=12   changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```
> **Proof of Idempotency:** Notice `changed=0` and `failed=0` during the second run. All states were already satisfied.

---

## 2. Automated Health Check Validation (`/actuator/health`)

```bash
$ powershell -File scripts/health-check.ps1
Pinging health endpoint: http://localhost:8080/actuator/health...
✅ Health Check PASSED on Attempt 1: System is UP

$ curl -i http://localhost:8080/actuator/health
HTTP/1.1 200 OK
Content-Type: application/vnd.spring-boot.actuator.v3+json
Transfer-Encoding: chunked

{
  "status": "UP",
  "components": {
    "diskSpace": {
      "status": "UP",
      "details": { "total": 510992384000, "free": 248911024128, "threshold": 10485760, "path": "C:\\...", "exists": true }
    },
    "ping": { "status": "UP" }
  }
}
```

---

## 3. Emergency Rollback & Recovery Demonstration

When a regression or degraded artifact is detected, the automated rollback playbook (`ansible/rollback.yml`) or recovery script (`scripts/rollback.ps1`) reverts the live deployment to the previous stable baseline in under 30 seconds:

```bash
$ ansible-playbook -i ansible/hosts.ini ansible/rollback.yml

PLAY [Emergency Rollback to Previous Stable Release] **********************************************************

TASK [1. Verify Stable Backup Artifact Exists] ****************************************************************
ok: [target-node-01]

TASK [2. Stop Degraded Application Service] *******************************************************************
changed: [target-node-01]

TASK [3. Restore Previous Stable Artifact] ********************************************************************
changed: [target-node-01]

TASK [4. Restart Service with Restored Artifact] **************************************************************
changed: [target-node-01]

TASK [5. Verify Rollback Service Health] **********************************************************************
ok: [target-node-01]

TASK [6. Confirm Successful Rollback Recovery] ****************************************************************
ok: [target-node-01] => {
    "msg": "SUCCESS: Rollback complete. SmartDispatch 108 restored to previous release (Health: UP)."
}

PLAY RECAP ****************************************************************************************************
target-node-01             : ok=6    changed=3    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```
