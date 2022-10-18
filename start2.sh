#!/bin/bash

rm -rf *.jar
rm -rf *.txt

for i in $(ls)
do
    if [[ "$i" != *".txt" ]] && [[ "$i" != *".jar" ]] && [[ "$i" != *".sh" ]]
    then
        echo "found: $i"
        cd "$i"
        sudo chmod +x scripts/deploy.sh
        ./scripts/deploy.sh
        cd ..
    fi
done

