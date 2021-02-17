# Bank deposit service demo:
https://www.baeldung.com/sonar-qube
docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest
afc36e5bc60d670dd3d2cff48539e380db745f

-- start sonarqube
docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest

-- jenkins
docker run -p 8080:8080 -p 50000:50000 jenkins/jenkins:lts

jenkins console:http://localhost:8080/