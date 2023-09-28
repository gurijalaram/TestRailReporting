def buildInfo
def buildInfoFile = "build-info.yml"
def buildVersion = "latest"
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

def tag_n_push_version(currentVersion = '', targetVersion = '') {
    // Tag & push version.
    sh "docker tag ${currentVersion} ${targetVersion}"
    sh "docker push ${targetVersion}"

}

pipeline {
    agent {
        label "WALQSDOCKER08"
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

        stage("Multi-Stage") {
            matrix {
                axes {
                    axis {
                        name 'MODULE'
                        values 'cidapp-ui', 'cidapp-api'
                    }
                }

                stages {
                    stage("Deploy") {
                        steps {
                            script {
                                if (MODULE.name.endsWith("-ui")) {
                                    folder = "web"
                                } else {
                                    folder = "microservices"
                                }

                                stage("Build") {
                                    echo "Building..."
                                    sh """
                                        docker build -f qa-stacks.Dockerfile \
                                        --build-arg FOLDER=${folder} \
                                        --build-arg MODULE=${MODULE} \
                                        --tag ${buildInfo.name}-${MODULE}-${runType}:${buildVersion} \
                                        .
                                    """
                                }
                            }

                            stage("Tag_n_Push") {
                                echo "Tagging and Pushing ..."

                                // Prepare aws login command.
                                def registryPwd = registry_password(environment.profile, environment.region)

                                sh "docker login -u AWS -p ${registryPwd} ${ecrDockerRegistry}"

                                def awsArtifactTarget = "${ecrDockerRegistry}-${MODULE}:${buildVersion}"

                                // Tag and push to ECR.
                                tag_n_push_version("${buildInfo.name}-${MODULE}-${runType}:latest", "${awsArtifactTarget}")
                            }

                            stage("Clean") {
                                echo "Cleaning up..."
                                sh "docker rmi ${buildInfo.name}-${MODULE}-${runType}:${buildVersion}"
                                sh "docker system prune --all --force"
                            }
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