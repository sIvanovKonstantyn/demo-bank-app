docker build -t svosh/demo-bankapp-config-server .

gradlew :config-server:bootBuildImage --imageName=svosh/demo-bankapp-config-server