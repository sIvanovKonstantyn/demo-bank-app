Gradle and docker should be installed on build server...

```
## Pull src from repo https://github.com/sIvanovKonstantyn/demo-bank-app.git
> git pull origin/master 

## Build all project modules (end to end test excluded)
> gradle clean build -x :end-to-end-tests:build

## Generate gradle wrapper
> gradle wrapper

## Build docker images
> gradlew :config-server:bootBuildImage --imageName=svosh/demo-bankapp-config-server
> gradlew :deposit-service:bootBuildImage --imageName=svosh/deposit-service
> gradlew :gateway:bootBuildImage --imageName=svosh/gateway
> gradlew :query-api-service:bootBuildImage --imageName=svosh/query-api-service 

## Run end to end tests
> gradle :end-to-end-tests:test

## Stop all test docker conainers
> docker stop application_deposit-service_1
> docker stop application_query-api-service_1
> docker stop application_gateway_1
```