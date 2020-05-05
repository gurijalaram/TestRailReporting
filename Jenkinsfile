def buildInfo
def buildInfoFile = 'build-info.yml'
def timeStamp = new Date().format('yyyyMMddHHmmss')

pipeline {
    parameters {
        string(name: 'TARGET_URL', defaultValue: 'https://automation.awsdev.apriori.com/', description: 'What is the target URL for testing?')
        choice(name: 'TARGET_ENV', choices: ['cid-aut', 'cid-te', 'customer-smoke'], description: 'What is the target environment for testing?')
        choice(name: 'TEST_SUITE', choices: ['AdminSuite', 'SmokeTestSuite','CIDTestSuite','AdhocTestSuite','CustomerSmokeTestSuite'], description: 'What is the test suite?')
        string(name: 'THREAD_COUNT', defaultValue: '1', description: 'What is the amount of browser instances?')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'What is the browser?')
        string(name: 'TEST_MODE', defaultValue: 'LOCAL', description: 'What is target test mode?')
        choice(name: 'VM', choices: ['loki','hela','ragnarok'], description: 'What is the VM?')
        string(name: 'HEADLESS', defaultValue: 'true', description: 'No browser window?')
    }

    agent {
        label "${params.VM}"
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
                        java \
                        -DthreadCounts=${params.THREAD_COUNT} -Dbrowser=${params.BROWSER} -Durl=${params.TARGET_URL} -Denv=${params.TARGET_ENV} -Dmode=${params.TEST_MODE} -Dheadless=${params.HEADLESS} \
                        -jar \
                        automation-tests.jar \
                        -tests testsuites.${params.TEST_SUITE}
                """

                // Copy out Allure results
                sh """
                    docker cp \
                    ${buildInfo.name}-build-${timeStamp}:app/allure-results \
                    .
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
