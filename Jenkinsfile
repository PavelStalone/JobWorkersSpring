pipeline {
    agent {
        docker {
            image 'docker:20.10-dind'
            args '-v /var/run/docker.sock:/var/run/docker.sock -v /usr/local/bin/docker-compose:/usr/local/bin/docker-compose'
        }
    }

    stages {
//         stage('Setup') {
//             steps {
//                 sh 'chmod +x gradlew gradlew.bat docker-compose*'
// //                 sh './gradlew build'
//             }
//         }
        stage('build') {
            steps {
                sh 'docker-compose build --progress=plain'
            }
        }
        stage('deploy') {
            steps {
                sh 'docker-compose up -d'
            }
        }
    }
}