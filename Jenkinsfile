def buildInfo
def buildInfoFile = 'build-info.yml'
def branchName = env.BRANCH_NAME
def timeStamp = new Date().format('yyyyMMddHHmmss')

pipeline {
    agent any

    stages {
        stage('Initialize') {
            steps {
                echo 'Initializing..'
                script {
                    // Read file.
                    buildInfo = readYaml file: buildInfoFile
                    sh "rm ${buildInfoFile}"

                    // Write file.
                    buildInfo.buildNumber = env.BUILD_TAG
                    buildInfo.buildTimestamp = timeStamp
                    buildInfo.commitHash = env.GIT_COMMIT
                    writeYaml file: buildInfoFile, data: buildInfo

                    // Log file.
                    sh "cat ${buildInfoFile}"
                }
            }
        }
        stage('Build') {
            when { expression { shouldBuild } }
            steps {
                echo 'Building..'
                withCredentials([usernamePassword(
                    credentialsId: 'NEXUS_APRIORI_COM',
                    passwordVariable: 'NEXUS_PASS',
                    usernameVariable: 'NEXUS_USER')]) {
                    sh """
                        docker build \
                            --no-cache \
                            --target build \
                            --tag ${buildInfo.name}-build-${timeStamp}:latest \
                            --label \"build-date=${timeStamp}\" \
                            --build-arg ORG_GRADLE_PROJECT_mavenUser=${NEXUS_USER} \
                            --build-arg ORG_GRADLE_PROJECT_mavenPassword=${NEXUS_PASS} \
                            .
                    """
                }
            }
        }
        stage('Test') {
            when { expression { shouldBuild } }
            steps {
                echo 'Testing..'
                withCredentials([usernamePassword(
                    credentialsId: 'NEXUS_APRIORI_COM',
                    passwordVariable: 'NEXUS_PASS',
                    usernameVariable: 'NEXUS_USER')]) {
                    sh """
                        docker build \
                            --target test \
                            --tag ${buildInfo.name}-test-${timeStamp}:latest \
                            --label \"build-date=${timeStamp}\" \
                            --build-arg ORG_GRADLE_PROJECT_mavenUser=${NEXUS_USER} \
                            --build-arg ORG_GRADLE_PROJECT_mavenPassword=${NEXUS_PASS} \
                            .
                   """
                }

                // Copy out build/test artifacts.
                sh "docker create --name ${buildInfo.name}-test-${timeStamp} ${buildInfo.name}-test-${timeStamp}:latest"
                sh "docker cp ${buildInfo.name}-test-${timeStamp}:build-workspace/build build"
                sh "docker rm ${buildInfo.name}-test-${timeStamp}"
                sh "docker rmi ${buildInfo.name}-test-${timeStamp}:latest"

                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'build/reports/tests/test',
                    reportFiles: 'index.html',
                    reportName: "${buildInfo.name} Test Report"
                ])
            }
        }
    }
    post {
        always {
            echo 'Cleaning up..'
            sh "docker image prune --force --filter=\"label=build-date=${timeStamp}\""
        }
    }
}