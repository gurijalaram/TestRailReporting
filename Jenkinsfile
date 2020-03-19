pipeline {
    agent {
        label "CONQBW8VM11"
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
                    bat label: '', script: 'gradle clean :uitests:test --tests CIDTestSuite -DthreadCount=1 -Dbrowser=chrome -Denv=cid-te --scan --info'
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