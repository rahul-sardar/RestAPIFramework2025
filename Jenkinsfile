pipeline {
    agent any

    tools {
        maven 'maven'
    }

    environment {
        DOCKER_IMAGE = 'naveenkhunteta/jan2025apiframework:${BUILD_NUMBER}'
        DOCKER_CREDENTIALS_ID = 'dockerhub_credentials'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git 'https://github.com/naveenanimation20/Jan2025APIFramework.git'
            }
        }

        stage('Build') 
        {
            steps
            {
                 git 'https://github.com/jglick/simple-maven-project-with-tests.git'
                 sh "mvn -Dmaven.test.failure.ignore=true clean package"
            }
            post 
            {
                success
                {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${DOCKER_IMAGE}")
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('', "${DOCKER_CREDENTIALS_ID}") {
                        dockerImage.push()
                    }
                }
            }
        }

        stage('Deploy to Dev') {
            steps {
                echo 'Deploying to Dev environment...'
            }
        }

        stage('Run Sanity Tests on Dev') {
            steps {
                sh "docker run --rm ${DOCKER_IMAGE} mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testng_sanity.xml -Denv=dev"
            }
        }

        stage('Deploy to QA') {
            steps {
                echo 'Deploying to QA environment...'
            }
        }

        stage('Run Regression Tests on QA') {
            steps {
                sh "docker run --rm ${DOCKER_IMAGE} mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testng_regression.xml -Denv=qa"
            }
        }

        stage('Publish Allure Reports') {
            steps {
                allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }

        stage('Publish ChainTest Report') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'target/chaintest',
                    reportFiles: 'Index.html',
                    reportName: 'HTML API Regression ChainTest Report',
                    reportTitles: ''
                ])
            }
        }

        stage('Deploy to Stage') {
            steps {
                echo 'Deploying to Stage environment...'
            }
        }

        stage('Run Sanity Tests on Stage') {
            steps {
                sh "docker run --rm ${DOCKER_IMAGE} mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testng_sanity.xml -Denv=stage"
            }
        }

        stage('Publish Sanity ChainTest Report') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'target/chaintest',
                    reportFiles: 'Index.html',
                    reportName: 'HTML API Sanity ChainTest Report',
                    reportTitles: ''
                ])
            }
        }

        stage('Deploy to Prod') {
            steps {
                echo 'Deploying to Prod environment...'
            }
        }

        stage('Run Sanity Tests on Prod') {
            steps {
                sh "docker run --rm ${DOCKER_IMAGE} mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testng_sanity.xml -Denv=prod"
            }
        }
    }
}
