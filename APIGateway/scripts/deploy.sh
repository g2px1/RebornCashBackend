#!/usr/bin/env bash

mvn clean package

echo 'Start server...'

# shellcheck disable=SC2046
sudo kill -9 $(sudo lsof -t -i:8084)
cp target/apiGateway-0.1.jar ../apiGateway-0.1.jar
cd ../
nohup java -Xms256m -Xmx6500m -jar apiGateway-0.1.jar > logApiGateway.txt &

echo 'Bye'