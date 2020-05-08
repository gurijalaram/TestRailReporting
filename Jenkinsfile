def buildInfo
def buildInfoFile = 'build-info.yml'
def timeStamp = new Date().format('yyyyMMddHHmmss')
def JAVA_OPTS = ''
def threadCount
def browser
def testSuite

pipeline {
    parameters {
        choice(name: 'TARGET_ENV', choices: ['cid-aut','cid-te','cid-perf','customer-smoke'], description: 'What is the target environment for testing?')
        choice(name: 'TEST_TYPE', choices: ['uitests','apitests'] , description: "What type of test is running?")
        choice(name: 'TEST_SUITE', choices: ['SanityTestSuite', 'AdminSuite', 'SmokeTestSuite','CIDTestSuite','AdhocTestSuite','CustomerSmokeTestSuite','Other'], description: 'What is the test suite?')
        string(name: 'OTHER_TEST', description: 'What is the test/suite to execute')
        string(name: 'THREAD_COUNT', defaultValue: '1', description: 'What is the amount of browser instances?')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', "none"], description: 'What is the browser?')
        booleanParam(name: 'HEADLESS', defaultValue: false, description: 'No browser window?')
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

                    // Set run time parameters
                    JAVA_OPTS = JAVA_OPTS + '-Dmode=QA'
                    JAVA_OPTS = JAVA_OPTS + ' -Denv=${params.TARGET_ENV}'

                    threadCount = ${params.THREAD_COUNT}
                    if (threadCount.toInteger() > 0) {
                        JAVA_OPTS = JAVA_OPTS + ' -DthreadCounts=${threadCount}'
                    }

                    browser = ${params.BROWSER}
                    if (browser != 'none') {
                        JAVA_OPTS = JAVA_OPTS + ' -Dbrowser=${browser}'
                    }

                    if (${params.HEADLESS}) {
                        JAVA_OPTS = JAVA_OPTS + ' -Dheadless=true}'
                    }

                    testSuite = ${params.TEST_SUITE}
                    if (testSuite == 'Other') {
                        testSuite = $ { params.OTHER_TEST }
                    }
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
                        ${JAVA_OPTS} \
                        -jar automation-tests.jar \
                        --tests ${testSuite}
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
