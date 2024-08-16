pipeline {
    agent any

    stages {
    stage('Checkout') {
                steps {
                    checkout([$class: 'GitSCM', branches: [[name: '*/develop']], userRemoteConfigs: [[url: 'https://github.com/gurijalaram/TestRailReporting.git']]])
                    //git 'https://github.com/rajrules21/Jenkins-Projects'
                }
    }
    stage("Extract Test Results") {
            steps {
                // Copy out build/test artifacts.
                echo "Extract Test Results.."
                publishHTML(target: [
                        allowMissing         : false,
                        alwaysLinkToLastBuild: false,
                        keepAll              : true,
                        reportDir            : '',
                        reportFiles          : 'test-report.html',
                        reportName           : "TestRailReport"
                ])
            }
            }
}
}