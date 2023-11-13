def buildInfo
def buildInfoFile = "build-info.yml"
def timeStamp = new Date().format('yyyyMMddHHss')

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
                    stage("Build_n_Test") {
                        steps {
                            echo "Testing..."
                            sh """
                                docker build \
                                --progress=plain \
                                --target test \
                                --tag ${buildInfo.name}-${MODULE}-test-${timeStamp}:latest \
                                --label \"build-date=${timeStamp}\" \
                                --build-arg FOLDER="microservices" \
                                --build-arg MODULE=${MODULE} \
                                --build-arg TESTS="testsuites.ApiSanitySuite" \
                                .
                            """
                        }
                    }
                    stage("Publish_Results") {
                        steps {
                            // Copy out build/test artifacts.
                            echo "Extract Test Results.."
                            sh "docker create --name ${buildInfo.name}-${MODULE}-test-${timeStamp} ${buildInfo.name}-${MODULE}-test-${timeStamp}:latest"
                            sh "docker cp ${buildInfo.name}-${MODULE}-test-${timeStamp}:home/gradle/microservices/${MODULE}/build ."
                            echo "Publishing Results"
                            allure includeProperties: false, jdk: "", results: [[path: "build/allure-results"]]
                            junit skipPublishingChecks: true, testResults: 'build/test-results/test/*.xml'

                            publishHTML(target: [
                                    allowMissing         : false,
                                    alwaysLinkToLastBuild: false,
                                    keepAll              : true,
                                    reportDir            : 'build/reports/tests/test',
                                    reportFiles          : 'index.html',
                                    reportName           : "${buildInfo.name} Test Report"
                            ])
                        }
                    }
                    stage("Clean") {
                        steps {
                            echo "Cleaning..."
                            sh "docker rm -f ${buildInfo.name}-${MODULE}-test-${timeStamp}"
                            sh "docker rmi ${buildInfo.name}-${MODULE}-test-${timeStamp}:latest"
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