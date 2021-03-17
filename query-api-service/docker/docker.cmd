docker build -t svosh/query-api-service .

gradlew :query-api-service:bootBuildImage --imageName=svosh/query-api-service