FROM jenkins/jenkins:2.479.3-jdk17

USER root

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --verbose --plugin-file /usr/share/jenkins/ref/plugins.txt

USER jenkins

ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
