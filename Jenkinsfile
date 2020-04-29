def buildInfo
def buildInfoFile = 'build-info.yml'
def branchName = env.BRANCH_NAME
def changeBranch = env.CHANGE_BRANCH ?: ''
def environment = [profile: 'development', region: 'us-east-1']
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
                withCredentials([usernamePassword(
                    credentialsId: 'NEXUS_APRIORI_COM',
                    passwordVariable: 'NEXUS_PASS',
                    usernameVariable: 'NEXUS_USER')]) {
                    sh """
                        docker build \
                            --no-cache \
                            --target build \
                            --tag automation-qa-build:latest \
                            --label \"build-date=${timeStamp}\" \
                            --build-arg ORG_GRADLE_PROJECT_mavenUser=${NEXUS_USER} \
                            --build-arg ORG_GRADLE_PROJECT_mavenPassword=${NEXUS_PASS} \
                            .
                    """
                }
            }
        }
    }
}