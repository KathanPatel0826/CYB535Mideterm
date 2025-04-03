pipeline {
    agent any

    environment {
        MAVEN_CACHE = "${WORKSPACE}/.m2"
        SONAR_HOST_URL = 'http://host.docker.internal:9191/'
        SONAR_PROJECT_KEY = 'helloworld-app'
        SONAR_LOGIN = 'b9b2068219b06852ce17c26d573edf6834953f2e'  // Use Jenkins Credentials for security
        DOCKER_PASSWORD = credentials('DOCKER_PASSWORD')
        DOCKER_USERNAME = 'kevinpatel26'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/KathanPatel0826/CYB535Mideterm.git'
            }
        }

        stage('Build with Java 17') {
            agent {
                docker {
                    image 'maven:3.8.5-openjdk-17'
                    reuseNode true
                    args '-v $HOME/.m2:/root/.m2 -v $WORKSPACE:/app'
                }
            }
            steps {
                script {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Run Tests with Java 11') {
            agent {
                docker {
                    image 'maven:3.8.5-openjdk-11'
                    reuseNode true
                    args '-v $HOME/.m2:/root/.m2 -v $WORKSPACE:/app'
                }
            }
            steps {
                script {
                    sh 'mvn test'
                }
            }
        }

        stage('Analyze Code Quality with SonarQube (Java 8)') {
            agent {
                docker {
                    image 'maven:3.8.5-openjdk-8'
                    reuseNode true
                    args '-v $HOME/.m2:/root/.m2 -v $WORKSPACE:/app'
                }
            }
            steps {
                script {
                    sh """
                        mvn sonar:sonar \
                            -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                            -Dsonar.host.url=${SONAR_HOST_URL} \
                            -Dsonar.login=${SONAR_LOGIN}
                    """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image..."

                    // Create Dockerfile
                    writeFile file: 'Dockerfile', text: """
                        FROM openjdk:17-jdk-slim
                        WORKDIR /app
                        COPY target/*.jar app.jar
                        ENTRYPOINT ["java", "-jar", "app.jar"]
                    """

                    // Build and push Docker image
                        docker.build("kevinpatel26/helloworld:${env.BUILD_ID}")
                        docker.build("kevinpatel26/helloworld:latest")
                    }
                }
            }
        stage('Push to Docker Hub') {
            steps {
                withCredentials([string(credentialsId: 'DOCKER_PASSWORD', variable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u kevinpatel26 --password-stdin'
                    sh 'docker push kevinpatel26/helloworld:latest'
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                    sh 'kubectl apply -f Deployment.yaml'
            }
        }
    }

    post {
        success {
            echo "Build completed successfully!"
        }
        failure {
            echo "Build failed! Check the logs."
        }
    }
}
