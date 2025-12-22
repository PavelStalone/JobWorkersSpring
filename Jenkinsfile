pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World from project'
                sh './gradlew clean'
            }
        }
        stage('build') {
            steps {
                sh 'docker-compose build --progress=plain'
            }
        }
        stage('deploy') {
            steps {
                sh 'docker-compose up'
            }
        }
    }
}