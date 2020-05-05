def buildInfo
def buildInfoFile = 'build-info.yml'
def timeStamp = new Date().format('yyyyMMddHHmmss')

pipeline {
    agent {
        label 'loki'
    }

    tools {
        gradle "Gradle"
    }

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
            steps {
                echo 'Building..'
                sh """
                    docker build \
                        --no-cache \
                        --tag ${buildInfo.name}-build-${timeStamp}:latest \
                        --label \"build-date=${timeStamp}\" \
                        .
                """
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                sh """
                    docker run \
                        -itd \
                        --name ${buildInfo.name}-build-${timeStamp} \
                        ${buildInfo.name}-build-${timeStamp}:latest
                """
                sh """
                    docker exec \
                        ${buildInfo.name}-build-${timeStamp} \
                        java -jar automation-tests.jar"
                """

                // Copy out Allure results
                sh """
                    docker cp \
                    ${buildInfo.name}-build-${timeStamp}:app/allure-results \
                    allure-results
                """

                // Stop and remove container and image
                sh "docker rm -f ${buildInfo.name}-build-${timeStamp}"
                sh "docker rmi ${buildInfo.name}-build-${timeStamp}:latest"
            }
        }
    }
    post {
        always {
            // just in case error or something was missed in previous step
            echo 'Publishing Results & Cleaning up..'
            allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]
            sh "docker image prune --force --filter=\"label=build-date=${timeStamp}\""
        }
    }
}
