def buildInfo
def buildInfoFile = "build-info.yml"

pipeline {
    agent {
        label "automation"
    }

    stages {
        stage("Initialize") {
            steps {
                echo "Initializing..."
                script {
                    // Read file.
                    buildInfo = readYaml file: buildInfoFile
                    sh "rm ${buildInfoFile}"
                }
            }
        }

        stage("Multi-Stage") {
            matrix {
                agent {
                    label "automation"
                }
                axes {
                    axis {
                        name 'MODULE'
                        values 'ats-api', 'fms-api'
                    }
                }

                stages {
                    stage("Deploy_Web") {
                        steps {
                            echo "Testing..."
                            sh """
                                docker build \
                                --progress=plain \
                                --target test \
                                --tag ${buildInfo.name}-test-${timeStamp}:latest \
                                --label \"build-date=${timeStamp}\" \
                                --build-arg FOLDER="microservices" \
                                --build-arg MODULE=${MODULE} \
                                --build-arg TESTS="testsuites.APISanitySuite" \
                                .
                            """
                        }
                        steps {
                            // Copy out build/test artifacts.
                            echo "Extract Test Results.."
                            sh "docker create --name ${buildInfo.name}-test-${timeStamp} ${buildInfo.name}-test-${timeStamp}:latest"
                            sh "docker cp ${buildInfo.name}-test-${timeStamp}:home/gradle/microservices/${MODULE}/build ."
                            echo "Publishing Results"
                            allure includeProperties: false, jdk: "", results: [[path: "build/allure-results"]]
                            junit skipPublishingChecks: true, testResults: 'build/test-results/test/*.xml'

                            publishHTML(target: [
                                    allowMissing: false,
                                    alwaysLinkToLastBuild: false,
                                    keepAll: true,
                                    reportDir: 'build/reports/tests/test',
                                    reportFiles: 'index.html',
                                    reportName: "${buildInfo.name} Test Report"
                            ])
                        }
                        steps {
                            echo "Cleaning..."
                            sh "docker rmi ${buildInfo.name}-test-${timeStamp}:latest"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}