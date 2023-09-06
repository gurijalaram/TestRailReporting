def buildInfo
def buildInfoFile = "build-info.yml"
def timeStamp = new Date().format('yyyyMMddHHss')
def buildVersion = "latest"
def modules = ["cidapp-ui"]
def folder
def runType = "docker-test"
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
                    -v "$AWS_CREDENTIALS_SECRET_TXT":/root/.aws/credentials \
                    -v "$AWS_CONFIG_SECRET_TXT":/root/.aws/config \
                    amazon/aws-cli ecr get-login-password \
                    --profile ${profile} --region ${region}
            """
        ).trim()
    }
}

def registry_id(profile = '', region = '') {
    withCredentials([
            file(credentialsId: 'AWS_CONFIG_FILE', variable: 'AWS_CONFIG_SECRET_TXT'),
            file(credentialsId: 'AWS_CREDENTIALS_FILE', variable: 'AWS_CREDENTIALS_SECRET_TXT')]) {
        return sh(
                returnStdout: true,
                script: """
                docker run \
                    -v "$AWS_CREDENTIALS_SECRET_TXT":/root/.aws/credentials \
                    -v "$AWS_CONFIG_SECRET_TXT":/root/.aws/config \
                    amazon/aws-cli sts get-caller-identity --output text --query Account
            """
        ).trim()
    }
}

def tag_n_push_version(currentVersion = '', targetVersion = '') {
    // Tag & push version.
    sh "docker tag ${currentVersion} ${targetVersion}"
    sh "docker push ${targetVersion}"

}

pipeline {
    agent {
        label "automation"
    }

    stages {
        stage("Initialize") {
            steps {
                echo "Initializing..."
                script {
                    // Read file.
                    buildInfo = readYaml file: buildInfoFile
                    sh "rm ${buildInfoFile}"
                }
            }
        }

        stage("Deploy") {
            steps {
                script {
                    modules.each { module ->
                        if (module.endsWith("-ui")) {
                            folder = "web"
                        } else {
                            folder = "microservices"
                        }

                        stage("Build") {
                            echo "Building..."
//                            withCredentials([usernamePassword(
//                                    credentialsId: 'NEXUS_APRIORI_COM',
//                                    passwordVariable: 'NEXUS_PASS',
//                                    usernameVariable: 'NEXUS_USER')]) {
                            sh """
                        docker build -f qa-stacks.Dockerfile \
                        --build-arg FOLDER=${folder} \
                        --build-arg MODULE=${module} \
                        --tag ${buildInfo.name}-${module}-${runType}:${buildVersion} \
                        .
                """
                        }

                        stage("Tag_n_Push") {
                            echo "Tagging and Pushing ..."

                            // Prepare aws login command.
                            def registryPwd = registry_password(environment.profile, environment.region)
//                            def registryId = registry_id(environment.profile, environment.region)

                            sh "docker login -u AWS -p ${registryPwd} ${ecrDockerRegistry}"

                            def awsArtifactCurrent="${ecrDockerRegistry}/${module}:${buildVersion}"
                            def awsArtifactTarget="${ecrDockerRegistry}/${module}:${buildVersion}"

                            // Tag and push to ECR.
                            tag_n_push_version("${buildInfo.name}-${module}-${runType}:latest ${awsArtifactCurrent}", "${awsArtifactTarget}")
                        }


//                        stage("Tag") {
//                            withCredentials([
//                                    string(credentialsId: 'aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'),
//                                    string(credentialsId: 'aws_secret_access_key', variable: 'AWS_SECRET_ACCESS_KEY')]) {
//                                sh """
//                        docker tag \
//                            ${buildInfo.name}-${module}-${runType}:latest 563229348140.dkr.ecr.us-east-1.amazonaws.com/apriori-qa-${module}:${buildVersion}
//                    """
//                            }
//                        }
//
//                        stage("Push") {
//                            withCredentials([
//                                    string(credentialsId: 'aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'),
//                                    string(credentialsId: 'aws_secret_access_key', variable: 'AWS_SECRET_ACCESS_KEY')]) {
//                                sh """
//                        docker push \
//                            563229348140.dkr.ecr.us-east-1.amazonaws.com/apriori-qa-${module}:${buildVersion}
//                    """
//                            }
//                        }

                        stage("Clean") {
                            echo "Cleaning up..."
                            sh "docker rmi ${buildInfo.name}-${module}-${runType}:${buildVersion}"
                            sh "docker system prune --all --force"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
