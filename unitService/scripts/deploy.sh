#!/usr/bin/env bash

mvn clean package

echo 'Start server...'

# shellcheck disable=SC2046
sudo kill -9 $(sudo lsof -t -i:8087)
cp target/unitService-0.1.jar ../unitService-0.1.jar
cd ../
nohup java -Xms256m -Xmx6500m -jar unitService-0.1.jar > logUnitService.txt &

echo 'Bye'