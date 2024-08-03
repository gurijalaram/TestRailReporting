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

pipeline {
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

        stage("Extract Test Results") {
            steps {
                // Copy out build/test artifacts.
                echo "Extract Test Results.."
                publishHTML(target: [
                        allowMissing         : false,
                        alwaysLinkToLastBuild: false,
                        keepAll              : true,
                        reportDir            : '',
                        reportFiles          : 'index.html',
                        reportName           : "test-report.html"
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