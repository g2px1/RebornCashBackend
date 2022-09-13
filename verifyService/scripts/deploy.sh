#!/usr/bin/env bash

mvn clean package

echo 'Start server...'

# shellcheck disable=SC2046
sudo kill -9 $(sudo lsof -t -i:8087)
cp target/verifyService-0.1.jar ../verifyService-0.1.jar
cd ../
nohup java -Xms256m -Xmx6500m -jar verifyService-0.1.jar > logVerify.txt &

echo 'Bye'