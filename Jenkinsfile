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
                sh '''
                        echo "=== 1. Структура в Jenkins dind ==="
                        docker run --rm prom/prometheus ls -ld /etc/prometheus/prometheus.yml

                        echo "=== 2. Тип вашего файла ==="
                        ls -l prometheus.yml

                        echo "=== 3. Docker Compose видит ==="
                        docker-compose config | grep -A2 prometheus -B2 volume
                    '''
                sh 'docker-compose up -d'
            }
        }
    }
}