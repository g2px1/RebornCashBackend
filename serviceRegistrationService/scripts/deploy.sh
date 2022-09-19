#!/usr/bin/env bash

mvn clean package

echo 'Start server...'

# shellcheck disable=SC2046
sudo kill -9 $(sudo lsof -t -i:8761)
cp target/serviceRegistrationService-0.1.jar ../serviceRegistrationService-0.1.jar
cd ../
nohup java -Xms256m -Xmx6500m -jar serviceRegistrationService-0.1.jar > logServiceRegistrator.txt &

echo 'Bye'