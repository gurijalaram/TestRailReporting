import groovy.sql.GroovyResultSet

def buildInfo
def buildInfoFile = "build-info.yml"
def timeStamp = new Date().format('yyyyMMddHHss')
def buildVersion = "latest"
def folder = "web"
def module = ["cidapp-ui", "cidapp-api"]
def runType = "docker-test"

pipeline {
    agent {
        label "automation"
    }

    stages {
        stage('MultiConfig') {
            matrix {
                axes {
                    axis {
                        name 'MODULE'
                        values 'cidapp-ui', 'cidapp-api'
                    }
                }
                when {
                    "${MODULE}" values.contains("-ui"), folder = "web"
                }
                when {
                    "${MODULE}" values.contains("-api"), folder = "microservices"
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
                            echo "Building.."
                            withCredentials([usernamePassword(
                                    credentialsId: 'NEXUS_APRIORI_COM',
                                    passwordVariable: 'NEXUS_PASS',
                                    usernameVariable: 'NEXUS_USER')]) {
                                sh """
                        docker login -u ${NEXUS_USER} -p ${NEXUS_PASS} docker.apriori.com
                        docker build -f qa-stacks.Dockerfile \
                        --build-arg FOLDER=${folder} \
                        --build-arg MODULE=${MODULE} \
                        --tag ${buildInfo.name}-${MODULE}-${runType}:${buildVersion} \
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
                            ${buildInfo.name}-${MODULE}-${runType}:latest 563229348140.dkr.ecr.us-east-1.amazonaws.com/apriori-qa-${MODULE}:${buildVersion}
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
                            563229348140.dkr.ecr.us-east-1.amazonaws.com/apriori-qa-${MODULE}:${buildVersion}
                        """
                            }
                        }
                    }

                    stage("Cleaning") {
                        steps {
                            echo "Cleaning up.."
                            sh "docker rmi ${buildInfo.name}-${MODULE}-${runType}:${buildVersion}"
                            sh "docker system prune --all --force"
                            cleanWs()
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
