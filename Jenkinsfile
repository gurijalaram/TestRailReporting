pipeline {

    parameters {
        string(name: 'TARGET_URL', defaultValue: 'https://automation.awsdev.apriori.com/', description: 'What is the target URL for testing?')
        choice(name: 'TARGET_ENV', choices: ['cid-aut', 'cid-te', 'customer-smoke'], description: 'What is the target environment for testing?')
        choice(name: 'TEST_SUITE', choices: ['AdminSuite', 'SmokeTestSuite','CIDTestSuite','AdhocTestSuite','CustomerSmokeTestSuite'], description: 'What is the test suite?')
        string(name: 'THREAD_COUNT', defaultValue: '1', description: 'What is the amount of browser instances?')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'What is the browser?')
        string(name: 'TEST_MODE', defaultValue: 'LOCAL', description: 'What is target test mode?')
        choice(name: 'VM', choices: ['frodo','gimli','legolas'], description: 'What is the VM?')
        string(name: 'HEADLESS', defaultValue: 'false', description: 'Open the browser window?')
    }

    agent {
        label "${params.VM}"
    }

    environment {
        JAVA_HOME = "${tool 'OpenJDK 1.8.0_192 WIN64'}"
        PATH = "${JAVA_HOME}/bin:${PATH}"
    }

    tools {
        gradle "Gradle"
    }

    stages {

        stage('UI Testing') {
            steps {
                echo 'Running test...'
                dir("${env.WORKSPACE}/build") {
                    bat label: "", script: "gradle clean :uitests:test --tests ${params.TEST_SUITE} -DthreadCounts=${params.THREAD_COUNT} -Dbrowser=${params.BROWSER} -Durl=${params.TARGET_URL} -Denv=${params.TARGET_ENV} -Dmode=${params.TEST_MODE} -Dheadless=${params.HEADLESS} --scan --info"
                }
            }

            post {
                always {
                    script {
                        allure([
                                includeProperties: false,
                                jdk              : '',
                                properties       : [],
                                reportBuildPolicy: 'ALWAYS',
                                results          : [[path: 'uitests/target/allure-results']]
                        ])
                    }
                }
            }
        }
    }
}