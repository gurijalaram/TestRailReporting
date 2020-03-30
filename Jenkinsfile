pipeline {

    parameters {
        string(name: 'TARGET_ENV', defaultValue: 'cid-aut', description: 'What is the target environment for testing?')
        string(name: 'TEST_SUITE', defaultValue: 'CIDTestSuite', description: 'What is the test suite?')
        string(name: 'THREAD_COUNT', defaultValue: '1', description: 'What is the amount of browser instances?')
        string(name: 'BROWSER', defaultValue: 'chrome', description: 'What is the browser?')
        string(name: 'TEST_MODE', defaultValue: 'LOCAL', description: 'What is target test mode?')
        string(name: 'JENKINS_NODE', defaultValue: ' ', description: 'What is Jenkins node?')
    }

    agent {
        label "${params.JENKINS_NODE}"
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
                    bat label: "", script: "gradle clean :uitests:test --tests ${params.TEST_SUITE} -DthreadCounts=${params.THREAD_COUNT} -Dbrowser=${params.BROWSER} -Denv=${params.TARGET_ENV} -Dmode=${params.TEST_MODE} --scan --info"
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