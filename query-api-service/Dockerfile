FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=build/libs/query-api-service-*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]