#!/usr/bin/env bash

mvn clean package

echo 'Start server...'

# shellcheck disable=SC2046
sudo kill -9 $(sudo lsof -t -i:8092)
cp target/securedApiGateway-0.1.jar ../securedApiGateway-0.1.jar
cd ../
nohup java -Xms256m -Xmx6500m -jar securedApiGateway-0.1.jar > logSecuredApiGateway.txt &

echo 'Bye'