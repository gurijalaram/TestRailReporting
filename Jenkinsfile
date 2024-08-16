def buildInfo
def buildInfoFile = "build-info.yml"
def timeStamp = new Date().format('yyyyMMddHHmmssSSS')
def javaOpts = ""
def url
def threadCount
def browser
def customer
def testSuite
def users_csv_file
def default_aws_region
def folder
def addJavaOpts
def number_of_parts
def parts_csv_file
def agent_type
def nexus_repository
def nexus_version
def custom_install
def connector
def environment = [profile: 'development', region: 'us-east-1']
def ecrDockerRegistry = '563229348140.dkr.ecr.us-east-1.amazonaws.com/apriori-qa'

def registry_password(profile = '', region = '') {
    withCredentials([
            file(credentialsId: 'AWS_CONFIG_FILE', variable: 'AWS_CONFIG_SECRET_TXT'),
            file(credentialsId: 'AWS_CREDENTIALS_FILE', variable: 'AWS_CREDENTIALS_SECRET_TXT')]) {
        return sh(
                returnStdout: true,
                script: """
                docker run \
                    -l amazon-cli \
                    -v "$AWS_CREDENTIALS_SECRET_TXT":/root/.aws/credentials \
                    -v "$AWS_CONFIG_SECRET_TXT":/root/.aws/config \
                    amazon/aws-cli ecr get-login-password \
                    --profile ${profile} --region ${region}
            """
        ).trim()
    }
}

pipeline {
    agent any

    stages {
        stage("Initialize") {
            steps {
                echo "Initializing.."
            }
        }

        stage("Build") {
            steps {
                echo "Building..."
            }
        }

        stage("Test") {
            steps {
                echo "Testing..."
            }
        }

        stage('Checkout') {
                        steps {
                            checkout([$class: 'GitSCM', branches: [[name: '*/develop']], userRemoteConfigs: [[url: 'https://github.com/gurijalaram/TestRailReporting.git']]])
                        }
            }

        stage("Extract Test Result") {
            steps {
                // Copy out build/test artifacts.
                echo "Extract Test Results.."

                publishHTML(target: [
                        allowMissing         : false,
                        alwaysLinkToLastBuild: false,
                        keepAll              : true,
                        reportDir            : 'trr-api/Reports',
                        reportFiles          : 'TestCoverageReport.html',
                        reportName           : "TestCoverageReport"
                ])
            }
        }
    }

    post {
        always {
            echo "Cleaning up.."
            }
        }
    }
}