# Bank deposit service demo:
https://www.baeldung.com/sonar-qube
docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest
6c8686176ed9a728703017b027931a17d2b1f9fd

-- start sonarqube
docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest

-- jenkins
docker run -p 8080:8080 -p 50000:50000 jenkins/jenkins:lts

jenkins console:http://localhost:8080/