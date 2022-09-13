#!/usr/bin/env bash

mvn clean package

echo 'Start server...'

# shellcheck disable=SC2046
sudo kill -9 $(sudo lsof -t -i:8087)
cp target/articleService-0.1.jar ../articleService-0.1.jar
cd ../
nohup java -Xms256m -Xmx6500m -jar articleService-0.1.jar > logArticles.txt &

echo 'Bye'