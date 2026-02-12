pipeline {
    agent any
    environment {
        CONTAINER_NAME = 'educationback'  
        CONTAINER_NAME_N = 'educationnew'  
        IMAGE_NAME = 'educationbackend'  
        DOCKER_REGISTRY = 'docker.io'  
        DOCKER_REPO = 'jagadishspyd/e-education'  
        TAG = "1.0BE"  
    }
    stages {
        stage('Setup Environment') {
            steps {
                echo 'Installing required dependencies...'
                sh '''
                sudo yum install git -y
                sudo yum install docker -y
                sudo systemctl enable docker
                sudo systemctl start docker
                sudo usermod -aG docker jenkins
                '''
            }
        }
        stage('Clone Repository') {
            steps {
                echo 'Cloning repository from GitHub...'
                git branch: 'main', url: 'https://github.com/spydtech/EonlineCource.git'
            }
        }
        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t $DOCKER_REPO:$TAG .'
            }
        }
        stage('Stop and Remove Old Container') {
            steps {
                echo 'Stopping and removing old container if exists...'
                sh '''
                docker stop $CONTAINER_NAME_N || true
                docker rm $CONTAINER_NAME_N || true
                '''
            }
        }
        //stage('Run Docker Container') {
           // steps {
                //echo 'Running Docker container...'
               // sh 'docker run -d --name $CONTAINER_NAME -p 80:80 $DOCKER_REPO:$TAG'
           // }
        //}
        stage('Push to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'eeducation', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        echo 'Logging in to Docker Hub...'
                        sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                    }
                    echo 'Pushing Docker image to Docker Hub...'
                    sh 'docker push $DOCKER_REPO:$TAG'
                }
            }
        }
        stage('Cleanup and Pull Latest Image') {
            steps {
                echo 'Cleaning up old images and pulling the latest image...'
                sh '''
                docker rmi $DOCKER_REPO:$TAG || true
               // docker system prune -af || true
                docker pull $DOCKER_REPO:$TAG
                '''
            }
        }
        stage('Deploy New Container') {
            steps {
                echo 'Creating a new Docker container...'
                sh 'docker run -d --name $CONTAINER_NAME_N -p 8082:81 $DOCKER_REPO:$TAG'
            }
        }
    }
    post {
        success {
            echo "Pipeline executed successfully!"
        }
        failure {
            echo "Pipeline failed. Please check the logs."
        }
    }
}
