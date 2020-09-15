def buildInfo
def buildInfoFile = "build-info.yml"
def timeStamp = new Date().format("yyyyMMddHHmmss")
def javaOpts = ""
def threadCount
def browser
def testSuite

pipeline {
    parameters {
        string(name: 'TARGET_URL', defaultValue: 'https://automation.awsdev.apriori.com/', description: 'What is the target URL for testing?')
        choice(name: 'TARGET_ENV', choices: ['cid-aut', 'cid-te', 'cid-perf', 'customer-smoke', 'cic-qa', 'cas-int', 'cas-qa', 'cid-int', 'cid-qa'], description: 'What is the target environment for testing?')
        choice(name: 'TEST_TYPE', choices: ['uitests', 'apitests', 'ciconnect', 'cid', 'cas', 'cir', 'cia'], description: 'What type of test is running?')
        choice(name: 'TEST_SUITE', choices: ['SanityTestSuite', 'AdminSuite', 'ReportingSuite', 'SmokeTestSuite', 'CIDTestSuite', 'AdhocTestSuite', 'CustomerSmokeTestSuite', 'CiaCirTestDevSuite', 'CIARStagingSmokeTestSuite', 'Other'], description: 'What is the test tests.suite?')
        string(name: 'OTHER_TEST', defaultValue:'test name', description: 'What is the test/tests.suite to execute')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'none'], description: 'What is the browser?')
        booleanParam(name: 'HEADLESS', defaultValue: true)
        string(name: 'THREAD_COUNT', defaultValue: '1', description: 'What is the amount of browser instances?')
        choice(name: 'TEST_MODE', choices: ['GRID', 'LOCAL', 'QA'], description: 'What is target test mode?')
    }

    agent {
        label "automation"
    }

    stages {
        stage("Initialize") {
            steps {
                echo "Initializing.."
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
                    javaOpts = javaOpts + "-Dmode=${params.TEST_MODE}"
                    javaOpts = javaOpts + " -Durl=${params.TARGET_URL}"
                    javaOpts = javaOpts + " -Denv=${params.TARGET_ENV}"

                    threadCount = params.THREAD_COUNT
                    if (threadCount.isInteger() && threadCount.toInteger() > 0) {
                        javaOpts = javaOpts + " -DthreadCounts=${threadCount}"
                    }

                    browser = params.BROWSER
                    if (browser != "none") {
                        javaOpts = javaOpts + " -Dbrowser=${browser}"
                    }

                    if (params.HEADLESS) {
                        javaOpts = javaOpts + " -Dheadless=true"
                    }

                    testSuite = "testsuites." + params.TEST_SUITE
                    if (params.TEST_SUITE == "Other")
                    {
                        testSuite = params.OTHER_TEST
                    }
                    echo "${javaOpts}"
                }
            }
        }
        stage("Build") {
            steps {
                echo "Building.."
                sh """
                    docker build \
                        --build-arg MODULE=${TEST_TYPE} \
                        --build-arg TEST_MODE=${TEST_MODE} \
                        --no-cache \
                        --tag ${buildInfo.name}-build-${timeStamp}:latest \
                        --label \"build-date=${timeStamp}\" \
                        .
                """
            }
        }
        stage("Test") {
            steps {
                echo "Running.."
                sh """
                    docker run \
                        -itd \
                        --name ${buildInfo.name}-build-${timeStamp} \
                        ${buildInfo.name}-build-${timeStamp}:latest
                """

                echo "Testing.."

                script {
                    if ("${params.TEST_MODE}" == "LOCAL") {
                        sh """
                            docker-compose up -d --force-recreate
                        """
                    }
                }

                sh """
                    sleep 15s
                    docker exec \
                        ${buildInfo.name}-build-${timeStamp} \
                        java \
                        ${javaOpts} \
                        -jar automation-tests.jar \
                        --tests ${testSuite}
                """

                // Copy out Allure results
                echo "Publishing Results"
                sh """
                    docker cp \
                    ${buildInfo.name}-build-${timeStamp}:app/target/allure-results \
                    .
                """
                allure includeProperties: false, jdk: "", results: [[path: "allure-results"]]
            }
        }
    }
    post {
        always {
            echo "Cleaning up.."
            sh "docker rm -f ${buildInfo.name}-build-${timeStamp}"
            sh "docker rmi ${buildInfo.name}-build-${timeStamp}:latest"
            script {
                if ("${params.TEST_MODE}" == "LOCAL") {
                    sh "docker rm -f \$(docker ps --filter name=chrome -q)"
                    sh "docker rm -f \$(docker ps --filter name=firefox -q)"
                    sh "docker rmi -f selenium/node-firefox"
                    sh "docker rmi -f selenium/node-chrome"
                }
            }
            sh "docker image prune --force --filter=\"label=build-date=${timeStamp}\""
            cleanWs()
        }
    }
}
