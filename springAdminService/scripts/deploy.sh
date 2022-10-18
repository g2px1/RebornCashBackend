#!/usr/bin/env bash

mvn clean package

echo 'Start server...'

# shellcheck disable=SC2046
sudo kill -9 $(sudo lsof -t -i:8093)
cp target/springAdmin-0.1.jar ../springAdmin-0.1.jar
cd ../
nohup java -Xms256m -Xmx6500m -jar springAdmin-0.1.jar > logServiceSpringAdmin.txt &

echo 'Bye'