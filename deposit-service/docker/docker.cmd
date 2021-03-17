docker build -t svosh/deposit-service .

gradlew :deposit-service:bootBuildImage --imageName=svosh/deposit-service