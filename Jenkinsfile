pipeline {
    agent {
        docker {
            image 'docker:27.1-dind'
            args '-v /var/run/docker.sock:/var/run/docker.sock --privileged'
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
                sh 'ls -la'
                sh 'pwd'
                sh '''
                    echo "=== Исследуем файл ==="
                    ls -la prometheus.yml
                    file prometheus.yml
                    stat prometheus.yml
                    hexdump -C prometheus.yml | head -5
                '''
                sh 'docker-compose up -d'
            }
        }
    }
}