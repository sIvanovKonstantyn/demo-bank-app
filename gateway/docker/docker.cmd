docker build -t svosh/gateway .

gradlew :gateway:bootBuildImage --imageName=svosh/gateway