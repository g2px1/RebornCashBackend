#!/usr/bin/env bash

mvn clean package

echo 'Start server...'

# shellcheck disable=SC2046
sudo kill -9 $(sudo lsof -t -i:8090)
cp target/balanceService-0.1.jar ../balanceService-0.1.jar
cd ../
nohup java -Xms256m -Xmx6500m -jar balanceService-0.1.jar > logServiceBalance.txt &

echo 'Bye'