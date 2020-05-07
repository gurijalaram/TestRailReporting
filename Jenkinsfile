def buildInfo
def buildInfoFile = 'build-info.yml'
def timeStamp = new Date().format('yyyyMMddHHmmss')

pipeline {
    parameters {
        choice(name: 'TEST_TYPE', choices: ['uitests','apitests'] , description: "What type of test is running?")
        string(name: 'TARGET_ENV', defaultValue: 'cid-aut', description: 'What is the target environment for testing?')
        string(name: 'TEST_SUITE', defaultValue: 'com.apriori.apitests.fms.suite.FmsAPISuite', description: 'What is the test suite?')
        string(name: 'THREAD_COUNT', defaultValue: '1', description: 'What is the amount of browser instances?')
        string(name: 'BROWSER', defaultValue: 'chrome', description: 'What is the browser?')
        string(name: 'HEADLESS', defaultValue: 'true', description: 'No browser window?')
    }

    agent {
        label 'automation'
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
                        --build-arg MODULE=${TEST_TYPE} \
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
                        java \
                        -jar automation-tests.jar \
                        --tests TEST_SUITE
                """

                // Copy out Allure results
                sh """
                    docker cp \
                    ${buildInfo.name}-build-${timeStamp}:app/target/allure-results \
                    .
                """

                echo 'Publishing Results'
                allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]
            }
        }
    }
    post {
        always {
            echo 'Cleaning up..'
            cleanWs()
            sh "docker rm -f ${buildInfo.name}-build-${timeStamp}"
            sh "docker rmi ${buildInfo.name}-build-${timeStamp}:latest"
            sh "docker image prune --force --filter=\"label=build-date=${timeStamp}\""
        }
    }
}
