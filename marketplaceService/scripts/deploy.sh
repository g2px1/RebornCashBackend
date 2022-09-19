#!/usr/bin/env bash

mvn clean package

echo 'Start server...'

# shellcheck disable=SC2046
sudo kill -9 $(sudo lsof -t -i:8088)
cp target/marketPlaceService-0.1.jar ../marketPlaceService-0.1.jar
cd ../
nohup java -Xms256m -Xmx6500m -jar marketPlaceService-0.1.jar > logMarket.txt &

echo 'Bye'