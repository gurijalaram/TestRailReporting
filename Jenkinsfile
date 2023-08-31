def folder = "web"
def module = "cidapp-ui"
def runType = "docker-test"
def timeStamp = new Date().format('yyyyMMdd')
def buildInfo = [name: '563229348140.dkr.ecr.us-east-1.amazonaws.com/apriori-qa-' + { module }, version: { timeStamp }]
def environment = [profile: 'development', region: 'us-east-1']

def nexusDockerRegistry = 'docker-dev-local.apriori.com'

def registry_login(profile = '', region = '', nexusDockerRegistry = '') {
    withCredentials([
            file(credentialsId: 'AWS_CONFIG_FILE', variable: 'AWS_CONFIG_SECRET_TXT'),
            file(credentialsId: 'AWS_CREDENTIALS_FILE', variable: 'AWS_CREDENTIALS_SECRET_TXT')]) {
        return sh(
                returnStdout: true,
                script: """
                docker run \
                    -v "$AWS_CREDENTIALS_SECRET_TXT":/home/jenkins/.aws/credentials \
                    -v "$AWS_CONFIG_SECRET_TXT":/home/jenkins/.aws/config \
                    ${nexusDockerRegistry}/awscli aws ecr get-login \
                    --no-include-email --profile ${profile} --region ${region}
            """
        ).trim()
    }
}

def tag_n_push_version(server = '', image = '', version = '', timeStamp = '') {
    // Tag & push version.
    sh "docker tag ${image}:${runType}-${timeStamp} ${server}/${image}:${version}"
    sh "docker push ${server}/${image}:${version}"

    // Remove images after push.
    sh "docker image rm ${server}/${image}:${version}"
}

pipeline {
    agent { label 'automation && docker' }

    stages {
        stage('Tag and Push') {
            steps {
                echo 'Tag..'
                script {
                    withCredentials([usernamePassword(
                            credentialsId: 'NEXUS_APRIORI_COM',
                            passwordVariable: 'LOCAL_REGISTRY_PASS',
                            usernameVariable: 'LOCAL_REGISTRY_USER')]) {

                        // Tag and push to Nexus.
                        //sh "docker login -u ${LOCAL_REGISTRY_USER} -p ${LOCAL_REGISTRY_PASS} ${nexusDockerRegistry}"
                        //tag_n_push_version(nexusDockerRegistry, buildInfo.name, buildInfo.version, timeStamp)
                    }

                    // Prepare login command.
                    def dockerLoginTarget = registry_login(environment.profile, environment.region, nexusDockerRegistry)
                    def registryServerTarget = dockerLoginTarget.substring(
                            dockerLoginTarget.lastIndexOf('/') + 1,
                            dockerLoginTarget.length()
                    )

                    // Tag and push to ECR.
                    sh dockerLoginTarget
                    tag_n_push_version(registryServerTarget, buildInfo.name, buildInfo.version, timeStamp)

                    // Remove the local image.
                    sh "docker image rm ${buildInfo.name}:latest-${timeStamp}"
                    sh "docker image rm qa-selenium-grid/node-base:latest-${timeStamp}"
                    sh "docker image rm qa-selenium-grid/base:latest-${timeStamp}"
                }
            }
        }
    }
}
