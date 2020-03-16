pipeline {
    agent {
        label "CONQBW8VM11"
    }

    environment {
       JAVA_HOME="${tool 'OpenJDK 1.8.0_192 WIN64'}"
       PATH="${JAVA_HOME}/bin:${PATH}"
    }

    tools {
        gradle "Gradle"
    }

    stages {
        stage('Checkout GIT Branch') {
    	    steps {
        	    echo 'Checking out branch....'
        		git branch: 'ba-881', credentialsId: '4a8bc0e7-4651-461a-843c-ed16c4fbef0e', poll: false, url: 'git@github.com:aPrioriTechnologies/apriori-qa'

    	    }
    	}

    	stage('Setup Workspace') {
    	    steps {
        	    echo 'Setting up workspace....'
        	    bat label: '', script: '''rmdir /s /q "%WORKSPACE%\\temp"
                if exist "%WORKSPACE%\\temp" (
                if not exist "%WORKSPACE%\\empty" mkdir %WORKSPACE%\\empty"
                robocopy /e /mir /log:"%WORKSPACE%\\robocopy.log" "%WORKSPACE%\\empty" "%WORKSPACE%\\temp"
                rmdir /s /q "%WORKSPACE%\\empty"
                rmdir /s /q "%WORKSPACE%\\temp"
                )
                mkdir "%WORKSPACE%\\temp"'''
    	    }
    	}

    	stage('End-2-End Testing') {
    	    steps {
    	        echo 'Running test....'
    	        dir("${env.WORKSPACE}/build"){
            	   bat label: '', script: 'gradle clean :uitests:test --tests "login.LoginTests" -Dmode=LOCAL -DthreadCount=3 --scan --info'
    	        }
    	    }

    	    post {
                  always {
                    script {
                      allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'uitests/target/allure-results']]
                      ])
                    }
                }
            }
    	}

    	stage('Temp Dir') {
    	    steps {
        	    echo 'Creating temp dir....'
        	    bat label: '', script: 'dir %TEMP% | findstr bytes'
    	    }
    	}
    }
}