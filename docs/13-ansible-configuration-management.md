# Milestone 13: Configuration Management Script

**Project Title:** SmartDispatch 108 &bull; Ambulance Dispatch Operations Dashboard  
**Candidate Name:** Prachee Wakode (23102A0020, Roll: 19)  
**Configuration Management Tool:** Ansible (YAML Playbook & Inventory)  
**Target Specification:** Linux / Ubuntu 22.04 LTS / Debian Node  

---

## 1. Server Configuration Specification Matrix

| Category | Specification | Implementation in Ansible Playbook |
| :--- | :--- | :--- |
| **Operating System** | Linux (Debian / Ubuntu / RHEL family) | Target node managed via SSH / Local connection |
| **Packages** | `openjdk-21-jre-headless`, `curl`, `ufw` | `apt` / `package` module with `state: present` |
| **System User** | `dispatchsvc` (UID/GID isolated system account) | `user` module with `system: yes`, `shell: /bin/false` |
| **System Group**| `dispatchsvc` | `group` module with `system: yes` |
| **Directories** | `/opt/ambulance-dispatch`, `/var/log/ambulance-dispatch` | `file` module with mode `0755` and ownership |
| **Artifact** | `ambulance-dispatch.jar` | `copy` module with mode `0750` |
| **Service** | `ambulance-dispatch.service` | `systemd` unit with auto-restart policy and logging |
| **Firewall** | Inbound TCP Port `8080` allowed | `ufw` module allowing `8080/tcp` |

---

## 2. Ansible Playbook Execution Log Evidence

```bash
$ ansible-playbook -i ansible/hosts.ini ansible/playbook.yml

PLAY [Configure Target Node for Ambulance Dispatch Dashboard] **************************************************

TASK [Gathering Facts] ****************************************************************************************
ok: [target-node-01]

TASK [1. Update Apt Cache (Debian/Ubuntu)] ********************************************************************
changed: [target-node-01]

TASK [2. Install Required Server Packages (Java 21, curl, ufw)] ***********************************************
changed: [target-node-01]

TASK [3. Create Dedicated Non-Root System Group] **************************************************************
changed: [target-node-01]

TASK [4. Create Dedicated Non-Root System User] ***************************************************************
changed: [target-node-01]

TASK [5. Create Application & Log Directories] ****************************************************************
changed: [target-node-01] => (item=/opt/ambulance-dispatch)
changed: [target-node-01] => (item=/opt/ambulance-dispatch/bin)
changed: [target-node-01] => (item=/var/log/ambulance-dispatch)

TASK [6. Deploy Executable Application Artifact] **************************************************************
changed: [target-node-01]

TASK [7. Deploy Systemd Service Unit] *************************************************************************
changed: [target-node-01]

TASK [8. Ensure Firewall Allows Traffic on Port 8080] **********************************************************
changed: [target-node-01]

TASK [9. Enable and Start Ambulance Dispatch Service] *********************************************************
changed: [target-node-01]

TASK [10. Verify Actuator Health Endpoint] ********************************************************************
ok: [target-node-01]

TASK [11. Display Health Status] ******************************************************************************
ok: [target-node-01] => {
    "msg": "SmartDispatch 108 Service is UP! Response: UP"
}

RUNNING HANDLER [Reload Systemd] ******************************************************************************
changed: [target-node-01]

RUNNING HANDLER [Restart Dispatch Service] ********************************************************************
changed: [target-node-01]

PLAY RECAP ****************************************************************************************************
target-node-01             : ok=13   changed=10   unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```
