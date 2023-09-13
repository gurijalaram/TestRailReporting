def buildInfo
def buildInfoFile = "build-info.yml"
def timeStamp = new Date().format('yyyyMMddHHss')
def javaOpts = ""
def url
def threadCount
def browser
def customer
def testSuite
def users_csv_file
def default_aws_region
def folder
def addlJavaOpts
def number_of_parts
def parts_csv_file
def agent_type
def nexus_repository
def nexus_version
def custom_install
def connector

pipeline {
/*
Configure the following parameters on your Jenkins jobs directly.  You only need to include those relevant to your job/app.
This should reduce the need for multiple jenkinsfiles
Those marked with a * are required or the job will not run
    parameters {
        * choice(name: 'MODULE_TYPE', choices: ['web', 'microservices', 'integrate'], description: 'What module type to run?')
        * choice(name: 'MODULE', choices: ['edc-ui', 'cid-ui', 'apitests', 'ciconnect-ui', 'cas-ui', 'cir-ui', 'cia-ui', 'cidapp-ui', 'integration'], description: 'What target module to run?')
        * choice(name: 'TARGET_ENV', choices: ['qa-21-1', 'qa-20-1', 'int-core'], description: 'What is the target environment?')
        * choice(name: 'TEST_MODE', choices: ['GRID', 'LOCAL', 'QA'], description: 'What is target test mode?')

        choice(name: 'TEST_SUITE', choices: ['SanityTestSuite', 'RegressionTestSuite', 'AdminSuite', 'ReportingSuite', 'CIDSmokeTestSuite', 'CIDNonSmokeTestSuite', 'AdhocTestSuite', 'CustomerSmokeTestSuite', 'CiaCirTestDevSuite', 'Other'], description: 'What is the test tests.suite?')
        string(name: 'OTHER_TEST', defaultValue: 'test name', description: 'What is the test/tests.suite to execute')

        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'none'], description: 'What is the browser?')
        booleanParam(name: 'HEADLESS', defaultValue: true)
        string(name: 'THREAD_COUNT', defaultValue: '1', description: 'What is the amount of browser instances?')

        string(name: 'TARGET_URL', defaultValue: 'none', description: 'What is the target URL for testing?')
        string(name: 'CSV_FILE', defaultValue: 'none', description: 'What is the csv file to use?')
    }
*/
    agent {
        label "automation"
    }

    stages {
        stage("Java") {
            tools {
                jdk "OpenJDK 11.0.18_10"
            }
            steps {
                sh 'java -version'
            }
        }

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
                    javaOpts = javaOpts + " -Denv=${params.TARGET_ENV}"

                    folder = params.MODULE_TYPE
                    if (!folder && "${MODULE}".contains("-ui")) {
                        folder = "web"
                    }
                    else if (!folder && "${MODULE}".contains("-api")) {
                        folder = "microservices"
                    }
                    else if (!folder && "${MODULE}".contains("-agent")) {
                        folder = "agent"
                    }
                    else {
                          folder = "integrate"
                    }

                    url = params.TARGET_URL
                    if (url && url != "none") {
                        javaOpts = javaOpts + " -Durl=${params.TARGET_URL}"
                    }

                    threadCount = params.THREAD_COUNT
                    if (threadCount && threadCount.isInteger() && threadCount.toInteger() > 0) {
                        javaOpts = javaOpts + " -DthreadCounts=${threadCount}"
                    }

                    browser = params.BROWSER
                    if (browser && browser != "none") {
                        javaOpts = javaOpts + " -Dbrowser=${browser}"
                    }

                    if (params.HEADLESS) {
                        javaOpts = javaOpts + " -Dheadless=true"
                    }

                    testSuite = params.TEST_SUITE
                    if (params.TEST_SUITE == "Other") {
                        testSuite = params.OTHER_TEST
                    }

                    users_csv_file = params.CSV_FILE
                    if (users_csv_file && users_csv_file != "none") {
                        javaOpts = javaOpts + " -Ddefault_users_csv_file=${params.CSV_FILE}"
                    }

                    customer = params.CUSTOMER
                    if (customer && customer != "none") {
                       javaOpts = javaOpts + " -Dcustomer=${params.CUSTOMER}"
                    }

                    default_aws_region = params.REGION
                    if (default_aws_region && default_aws_region != "none") {
                       javaOpts = javaOpts + " -Ddefault_aws_region=${params.REGION}"
                    }

                    number_of_parts = params.NUMBER_OF_PARTS
                    if (number_of_parts && number_of_parts != "none") {
                       javaOpts = javaOpts + " -Dnumber_of_parts=${params.NUMBER_OF_PARTS}"
                    }

                    parts_csv_file = params.PARTS_CSV_FILE
                    if (parts_csv_file && parts_csv_file != "none") {
                       javaOpts = javaOpts + " -Dparts_csv_file=${params.PARTS_CSV_FILE}"
                    }

                    agent_type = params.AGENT_TYPE
                    if (agent_type && agent_type != "none") {
                       javaOpts = javaOpts + " -Dci-connect_agent_type=${params.AGENT_TYPE}"
                    }

                    nexus_repository = params.NEXUS_REPOSITORY
                    if (nexus_repository && nexus_repository != "none") {
                       javaOpts = javaOpts + " -Dci-connect_nexus_repository=${params.NEXUS_REPOSITORY}"
                    }

                    nexus_version = params.NEXUS_VERSION
                    if (nexus_version && nexus_version != "none") {
                       javaOpts = javaOpts + " -Dci-connect_nexus_version=${params.NEXUS_VERSION}"
                    }

                    custom_install = params.CUSTOM_INSTALL
                    if (custom_install && custom_install != "none") {
                       javaOpts = javaOpts + " -Dci-connect_custom_install=${params.CUSTOM_INSTALL}"
                    }

                    addlJavaOpts = params.JAVAOPTS
                    if (addlJavaOpts && addlJavaOpts != "none") {
                        javaOpts = javaOpts + " " + addlJavaOpts
                    }

                    echo "${javaOpts}"
                }
            }
        }

        stage("Build") {
            steps {
                echo "Building..."
                sh """
                    docker build \
                        --no-cache \
                        --target build \
                        --tag ${buildInfo.name}-test-${timeStamp}:latest \
                        --label \"build-date=${timeStamp}\" \
                        --build-arg FOLDER=${folder} \
                        --build-arg MODULE=${MODULE} \
                        .
                """
            }
        }

        stage("Test") {
            steps {
                echo "Testing..."
                withCredentials([
                        string(credentialsId: 'aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'),
                        string(credentialsId: 'aws_secret_access_key', variable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh """
                        docker build \
                            --progress=plain \
                            --target test \
                            --tag ${buildInfo.name}-test-${timeStamp}:latest \
                            --label \"build-date=${timeStamp}\" \
                            --build-arg AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} \
                            --build-arg AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} \
                            --build-arg FOLDER=${folder} \
                            --build-arg MODULE=${MODULE} \
                            --build-arg JAVAOPTS='${javaOpts}' \
                            --build-arg TESTS=${testSuite} \
                            .
                    """
                }
            }
        }

        stage("Extract Test Results") {
            steps {
                // Copy out build/test artifacts.
                echo "Extract Test Results.."
                sh "docker create --name ${buildInfo.name}-test-${timeStamp} ${buildInfo.name}-test-${timeStamp}:latest"
                sh "docker cp ${buildInfo.name}-test-${timeStamp}:home/gradle/${folder}/${MODULE}/build ."
                echo "Publishing Results"
                allure includeProperties: false, jdk: "", results: [[path: "build/allure-results"]]
                junit skipPublishingChecks: true, testResults: 'build/test-results/test/*.xml'

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

    // TODO z: finish this
     stage('CheckLog') {
          steps {
            if (currentBuild.rawBuild.log().contains('Response contains UnrecognizedPropertyException.')) {
              error("Build failed because of Response contains UnrecognizedPropertyException. Please check Test logs.")
            }
    }

    post {
        always {
            echo "Cleaning up.."
            sh "docker rm -f ${buildInfo.name}-test-${timeStamp}"
            sh "docker rmi ${buildInfo.name}-test-${timeStamp}:latest"
            sh "docker volume rm \$(docker volume ls -qf dangling=true)"
            sh "docker system prune --all --force"
            cleanWs()
        }
    }
}
