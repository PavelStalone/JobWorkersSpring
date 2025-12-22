pipeline {
    agent {
        docker {
            image 'docker:27.1-dind'
            args '-v /var/run/docker.sock:/var/run/docker.sock --privileged --security-opt apparmor:unconfined --security-opt seccomp:unconfined'
        }
    }

    stages {
        stage('Setup') {
            steps {
                sh 'chmod +x gradlew gradlew.bat docker-compose*'
                sh 'docker images'
                sh 'docker container ls -a'
//                 sh './gradlew build'
            }
        }
        stage('build') {
            steps {
                sh 'docker-compose build --progress=plain'
            }
        }
        stage('deploy') {
            steps {
                sh '''
                    chmod 777 prometheus.yml
                    echo "=== 2. Тип вашего файла ==="
                    ls -l prometheus.yml
                '''
                sh 'docker-compose up -d'
            }
        }
    }
}