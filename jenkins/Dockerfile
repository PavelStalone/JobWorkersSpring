FROM jenkins/jenkins:2.479.3-jdk17

USER root

RUN apt-get update && apt-get install -y docker.io \
    && rm -rf /var/lib/apt/lists/*

RUN curl -L "https://github.com/docker/compose/releases/download/$(curl -s https://api.github.com/repos/docker/compose/releases/latest | grep -Po '"tag_name": "\K.*?(?=")')/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
RUN chmod +x /usr/local/bin/docker-compose
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --verbose --plugin-file /usr/share/jenkins/ref/plugins.txt

ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
