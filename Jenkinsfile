pipeline {
    agent {
        label "CONQBW8VM11"
    }

    parameters {
        string(name: 'TARGET_ENV', defaultValue: 'cid-aut', description: 'What is the target environment for testing?')
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
                    bat label: '', script: 'gradle clean :uitests:test --tests CIDTestSuite -DthreadCounts=1 -Dbrowser=chrome -Denv=${params.TARGET_ENV} --scan --info'
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