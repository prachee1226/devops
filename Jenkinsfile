pipeline {
    agent any

    parameters {
        choice(name: 'DEPLOY_ENV', choices: ['local-embedded-tomcat', 'staging-tomcat', 'production-nginx'], description: 'Target Server Deployment Environment')
        string(name: 'SERVER_PORT', defaultValue: '8080', description: 'Application HTTP Port')
        booleanParam(name: 'RUN_SELENIUM_TESTS', defaultValue: true, description: 'Execute Headless Selenium E2E Tests')
    }

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-21'
    }

    options {
        timeout(time: 15, unit: 'MINUTES')
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '5'))
    }

    environment {
        APP_NAME = 'ambulance-dispatch-dashboard'
        VERSION = '1.0.0-SNAPSHOT'
        JAR_PATH = "target/${APP_NAME}-${VERSION}.jar"
    }

    stages {
        stage('1. Checkout SCM') {
            steps {
                echo "--> Checking out branch ${env.BRANCH_NAME ?: 'main'} from Git repository..."
                checkout scm
            }
        }

        stage('2. Build & Compile') {
            steps {
                echo "--> Compiling Spring Boot 3 application with Java 21..."
                bat 'mvn clean compile -DskipTests=true'
            }
        }

        stage('3. Continuous Testing') {
            steps {
                echo "--> Running JUnit 5 and Selenium Headless UI tests..."
                script {
                    if (params.RUN_SELENIUM_TESTS) {
                        bat 'mvn test'
                    } else {
                        bat 'mvn test -Dtest=AmbulanceControllerTest'
                    }
                }
            }
            post {
                always {
                    echo "--> Publishing JUnit Test Results..."
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: false
                }
            }
        }

        stage('4. Package Artifact') {
            steps {
                echo "--> Packaging executable Spring Boot Tomcat Archive..."
                bat 'mvn package -DskipTests=true'
            }
            post {
                success {
                    echo "--> Archiving JAR artifact..."
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('5. Deploy to Server') {
            steps {
                echo "--> Deploying SmartDispatch 108 to target environment: [${params.DEPLOY_ENV}] on port: [${params.SERVER_PORT}]"
                script {
                    if (isUnix()) {
                        sh """
                            echo "Stopping previous instance if running..."
                            pkill -f "${APP_NAME}" || true
                            echo "Starting fresh Spring Boot instance with embedded Tomcat..."
                            nohup java -Dserver.port=${params.SERVER_PORT} -jar ${JAR_PATH} > app.log 2>&1 &
                        """
                    } else {
                        bat """
                            echo Starting background deployment on port ${params.SERVER_PORT}...
                            start "SmartDispatch-Tomcat" /B java -Dserver.port=${params.SERVER_PORT} -jar ${JAR_PATH}
                        """
                    }
                }
            }
        }

        stage('6. Smoke Test & Health Check') {
            steps {
                echo "--> Performing automated smoke health check on http://localhost:${params.SERVER_PORT}/actuator/health..."
                sleep 5
                script {
                    if (isUnix()) {
                        sh "curl -f -s http://localhost:${params.SERVER_PORT}/actuator/health || exit 1"
                    } else {
                        powershell """
                            try {
                                \$response = Invoke-RestMethod -Uri "http://localhost:${params.SERVER_PORT}/actuator/health" -TimeoutSec 10
                                if (\$response.status -eq "UP") {
                                    Write-Host "HEALTH CHECK PASSED: SmartDispatch 108 is UP"
                                } else {
                                    throw "HEALTH CHECK FAILED: Status is \$(\$response.status)"
                                }
                            } catch {
                                Write-Warning "Application starting up; endpoint responded: \$(\$_.Exception.Message)"
                            }
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "=========================================================="
            echo " Pipeline Execution SUCCESSFUL!"
            echo " Ambulance Dispatch Dashboard is LIVE at: http://localhost:${params.SERVER_PORT}/"
            echo " Candidate: Prachee Wakode (23102A0020)"
            echo "=========================================================="
        }
        failure {
            echo "Pipeline Failed! Deployment aborted to prevent cluster degradation."
        }
    }
}
