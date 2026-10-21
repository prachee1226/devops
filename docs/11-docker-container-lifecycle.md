# Milestone 11: Docker Image and Container Lifecycle

**Project Title:** SmartDispatch 108 &bull; Ambulance Dispatch Operations Dashboard  
**Candidate Name:** Prachee Wakode (23102A0020, Roll: 19)  
**Image Name:** `ambulance-dispatch:1.0.0` &bull; `ambulance-dispatch:latest`  
**Base Image:** `eclipse-temurin:21-jre-alpine` (Minimal Attack Surface, Size < 180MB)  

---

## 1. Multi-Stage Dockerfile Architecture

To optimize performance and eliminate build tools from production images, a 2-stage multi-stage build pattern is utilized:
1. **Builder Stage (`eclipse-temurin:21-jdk-alpine`):** Compiles Spring Boot source code and packages the fat JAR.
2. **Runtime Stage (`eclipse-temurin:21-jre-alpine`):** Lightweight Alpine JRE environment executing under non-root service account `spring:spring` for enterprise container security compliance.

---

## 2. Docker Command Execution Log (Complete Lifecycle)

```bash
# Step 1: Build Docker Image
$ docker build -t ambulance-dispatch:1.0.0 -t ambulance-dispatch:latest -f docker/Dockerfile .
[+] Building 14.8s (15/15) FINISHED
 => [internal] load build definition from Dockerfile
 => [internal] load .dockerignore
 => [builder 1/5] FROM docker.io/library/eclipse-temurin:21-jdk-alpine
 => [builder 4/5] COPY src ./src
 => [builder 5/5] RUN apk add --no-cache maven && mvn clean package -DskipTests=true
 => [stage-1 1/4] FROM docker.io/library/eclipse-temurin:21-jre-alpine
 => [stage-1 2/4] RUN addgroup -S spring && adduser -S spring -G spring
 => [stage-1 3/4] COPY --from=builder /app/target/*.jar app.jar
 => naming to docker.io/library/ambulance-dispatch:1.0.0
 => naming to docker.io/library/ambulance-dispatch:latest

# Step 2: Inspect Image Repository Details
$ docker images | grep ambulance-dispatch
REPOSITORY            TAG       IMAGE ID       CREATED          SIZE
ambulance-dispatch    1.0.0     d9f12ab458c1   30 seconds ago   178MB
ambulance-dispatch    latest    d9f12ab458c1   30 seconds ago   178MB

# Step 3: Run Container with Port Forwarding
$ docker run -d -p 8080:8080 --name smartdispatch-108 --restart unless-stopped ambulance-dispatch:1.0.0
e94b2f812cb901e82845c0882e3794aa1d39b7d8bb380e227df37c7689d0b674

# Step 4: Verify Running Container Status & Health
$ docker ps -f "name=smartdispatch-108"
CONTAINER ID   IMAGE                    COMMAND                  CREATED         STATUS                    PORTS                    NAMES
e94b2f812cb9   ambulance-dispatch:1.0.0 "sh -c 'java $JAVA_O…"   5 seconds ago   Up 4 seconds (healthy)   0.0.0.0:8080->8080/tcp   smartdispatch-108

# Step 5: Inspect Live Container Logs
$ docker logs --tail 20 smartdispatch-108
2026-10-21T14:45:01.120Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
2026-10-21T14:45:01.125Z  INFO 1 --- [           main] c.a.d.AmbulanceDispatchApplication      : Started AmbulanceDispatchApplication in 2.914 seconds

# Step 6: Test Stop, Restart, and Removal
$ docker stop smartdispatch-108
smartdispatch-108

$ docker restart smartdispatch-108
smartdispatch-108

$ docker rm -f smartdispatch-108
smartdispatch-108
```
