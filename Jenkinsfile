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
                sh '''
                    # 1. ВРЕМЕННО отключаем volume для prometheus
                    sed -i "/prometheus:/,/ports:/s/volumes:// volumes: #отключено/g" docker-compose.yml

                    cat docker-compose.yml

                    # 2. Запускаем ВСЕ сервисы
                    docker-compose up -d

                    # 3. Копируем файл В Prometheus
                    PROM_POD=$(docker-compose ps -q prometheus)
                    docker cp prometheus.yml $PROM_POD:/etc/prometheus/prometheus.yml

                    # 4. Рестарт только Prometheus
                    docker-compose restart prometheus

                    # 5. Возвращаем docker-compose.yml в исходное состояние
                    sed -i "/prometheus:/,/ports:/s:// volumes: #отключено/volumes:/g" docker-compose.yml

                    cat docker-compose.yml

                    echo "=== ПРОВЕРКА ==="
                    docker-compose ps
                    curl -f http://localhost:9090/-/ready || echo "Prometheus готовится..."
                '''
//                 sh 'docker-compose up -d'
            }
        }
    }
}