#!/usr/bin/env bash

mvn clean package

echo 'Start server...'

# shellcheck disable=SC2046
sudo kill -9 $(sudo lsof -t -i:8091)
cp target/oreHuntService-0.1.jar ../oreHuntService-0.1.jar
cd ../
nohup java -Xms256m -Xmx6500m -jar oreHuntService-0.1.jar > logServiceOreHunt.txt &

echo 'Bye'