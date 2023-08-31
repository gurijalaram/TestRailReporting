def buildInfo
def buildInfoFile = "build-info.yml"
def timeStamp = new Date().format('yyyyMMddHHss')
def javaOpts = ""
def folder = "web"
def module = "cidapp-ui"
def runType = "docker-test"

 pipeline {
    agent {
        label "automation"
    }

    stages {
    stage("Initialize") {
        steps {
            echo "Initializing.."
                script {
                    // Read file.
                    buildInfo = readYaml file: buildInfoFile
                    sh "rm ${buildInfoFile}"
                }
            }
        }

        stage("Build") {
            steps {
                echo "Building..."
                withCredentials([usernamePassword(
                    credentialsId: 'NEXUS_APRIORI_COM',
                    passwordVariable: 'NEXUS_PASS',
                    usernameVariable: 'NEXUS_USER')]) {
                    sh """
                        docker login -u ${NEXUS_USER} -p ${NEXUS_PASS} docker.apriori.com
                        docker build -f qa-stacks.Dockerfile \
                        --build-arg FOLDER=${folder} \
                        --build-arg MODULE=${module} \
                        --tag ${buildInfo.name}-${module}-${runType}:${timeStamp} \
                        .
                """
                }
            }
        }

        stage("Tag") {
            steps {
                withCredentials([
                        string(credentialsId: 'aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'),
                        string(credentialsId: 'aws_secret_access_key', variable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh """
                        docker tag \
                            ${buildInfo.name}-${module}-${runType}:${timeStamp} 563229348140.dkr.ecr.us-east-1.amazonaws.com/apriori-qa-${module}:${timeStamp}
                    """
                }
            }
        }

        stage("Push") {
            steps {
                withCredentials([
                        string(credentialsId: 'aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'),
                        string(credentialsId: 'aws_secret_access_key', variable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh """
                        docker push \
                            563229348140.dkr.ecr.us-east-1.amazonaws.com/apriori-qa-${module}:${timeStamp}
                    """
                }
            }
        }
    }

    post {
        always {
            echo "Cleaning up.."
            sh "docker rm -f ${buildInfo.name}-${module}-${runType}:${timeStamp}"
            sh "docker rmi ${buildInfo.name}-${module}-${runType}:${timeStamp}"
            sh "docker volume rm \$(docker volume ls -qf dangling=true)"
            sh "docker system prune --all --force"
            cleanWs()
        }
    }
}
