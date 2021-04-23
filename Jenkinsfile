def buildInfo
def buildInfoFile = "build-info.yml"
def uuid = UUID.randomUUID()
def javaOpts = ""
def url
def threadCount
def browser
def testSuite
def csvFile
def folder = "web"

pipeline {
    parameters {
        string(name: 'TARGET_URL', defaultValue: 'none', description: 'What is the target URL for testing?')
        choice(name: 'TARGET_MODULE', choices: ['cid', 'apitests', 'ciconnect', 'cas', 'cir', 'cia', 'cidapp'], description: 'What target module to run?')
        choice(name: 'MODULE_PROP', choices: ['customer-smoke', 'cic-qa', 'cas-int', 'cas-qa', 'cid-int', 'cid-qa', 'cidapp-int', 'staging', 'cia-qa-21-1', 'cir-qa-21-1'], description: 'What is the module properties file?')
        choice(name: 'TEST_SUITE', choices: ['SanityTestSuite', 'AdminSuite', 'ReportingSuite', 'CIDSmokeTestSuite', 'CIDNonSmokeTestSuite', 'AdhocTestSuite', 'CustomerSmokeTestSuite', 'CiaCirTestDevSuite', 'Other'], description: 'What is the test tests.suite?')
        string(name: 'OTHER_TEST', defaultValue:'test name', description: 'What is the test/tests.suite to execute')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'none'], description: 'What is the browser?')
        booleanParam(name: 'HEADLESS', defaultValue: true)
        string(name: 'THREAD_COUNT', defaultValue: '1', description: 'What is the amount of browser instances?')
        choice(name: 'TEST_MODE', choices: ['GRID', 'LOCAL', 'QA'], description: 'What is target test mode?')
        string(name: 'CSV_FILE', defaultValue: 'none', description: 'What is the csv file to use?')
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
                    buildInfo.buildTimestamp = uuid
                    buildInfo.commitHash = env.GIT_COMMIT
                    writeYaml file: buildInfoFile, data: buildInfo

                    // Log file.
                    sh "cat ${buildInfoFile}"

                    // Set run time parameters
                    javaOpts = javaOpts + "-Dmode=${params.TEST_MODE}"
                    javaOpts = javaOpts + " -Denv=${params.MODULE_PROP}"

                    url = params.TARGET_URL
                    if (url != "none") {
                        javaOpts = javaOpts + " -Durl=${params.TARGET_URL}"
                    }

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
                    if (params.TEST_SUITE == "Other") {
                        testSuite = params.OTHER_TEST
                    }

                    csvFile = params.CSV_FILE
                    if (csvFile != "none") {
                        javaOpts = javaOpts + " -DcsvFile=${params.CSV_FILE}"
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
                        --build-arg MODULE=${TARGET_MODULE} \
                        --build-arg TEST_MODE=${TEST_MODE} \
                        --build-arg FOLDER=${folder} \
                        --no-cache \
                        --tag ${buildInfo.name}-build-${uuid}:latest \
                        --label \"build-date=${uuid}\" \
                        .
                """
            }
        }
        stage("Test") {
            steps {
                echo "Running.."

                withCredentials([
                        string(credentialsId: 'aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'),
                        string(credentialsId: 'aws_secret_access_key', variable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh """
                    docker run \
                        -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} \
                        -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} \
                        -e AWS_PROFILE='development' \
                        -e AWS_DEFAULT_REGION='us-east-1' \
                        -itd \
                        --name ${buildInfo.name}-build-${uuid} \
                        ${buildInfo.name}-build-${uuid}:latest
                     """
                     }

                echo "Testing.."

                script {
                    if ("${params.TEST_MODE}" == "LOCAL") {
                        sh """
                            docker-compose up -d --force-recreate
                        """
                    }
                }

                sh """
                    sleep 10s
                    docker exec \
                        ${buildInfo.name}-build-${uuid} \
                        java \
                        ${javaOpts} \
                        -javaagent:aspectjweaver-1.8.10.jar \
                        -jar automation-tests.jar \
                        --tests ${testSuite}
                """

                // Copy out Allure results
                echo "Publishing Results"
                sh """
                    docker cp \
                    ${buildInfo.name}-build-${uuid}:app/target/allure-results \
                    .
                """
                allure includeProperties: false, jdk: "", results: [[path: "allure-results"]]
            }
        }

    }
    post {
        always {
            echo "Cleaning up.."
            sh "docker rm -f ${buildInfo.name}-build-${uuid}"
            sh "docker rmi ${buildInfo.name}-build-${uuid}:latest"
            script {
                if ("${params.TEST_MODE}" == "LOCAL") {
                    sh "docker rm -f \$(docker ps --filter name=chrome -q)"
                    sh "docker rm -f \$(docker ps --filter name=firefox -q)"
                    sh "docker rmi -f selenium/node-firefox"
                    sh "docker rmi -f selenium/node-chrome"
                }
            }
            sh "docker system prune --force"
            cleanWs()
        }
    }
}
