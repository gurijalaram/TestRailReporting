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
def GIT_SSH_KEY
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                script {
                     steps {
                          git branch: 'develop', url: 'https://github-token@github.com/gurijalaram/TestRailReporting.git
                         }
                }
            }
        }
    }
}