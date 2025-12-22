pipeline {
    agent any

    stages {
//         stage('Setup') {
//             steps {
//                 sh 'chmod +x gradlew gradlew.bat'
//                 sh './gradlew build'
//             }
//         }
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