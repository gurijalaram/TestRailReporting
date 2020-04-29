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
                        --target build \
                        --tag automation-qa-build:latest \
                        --label \"build-date=${timeStamp}\" \
                        .
                """
            }
        }
    }
}