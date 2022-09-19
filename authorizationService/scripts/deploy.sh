#!/usr/bin/env bash

mvn clean package

echo 'Start server...'

# shellcheck disable=SC2046
sudo kill -9 $(sudo lsof -t -i:8081)
cp target/authorizationService-0.1.jar ../authorizationService-0.1.jar
cd ../
nohup java -Xms256m -Xmx6500m -jar authorizationService-0.1.jar > logAuthorization.txt &

echo 'Bye'