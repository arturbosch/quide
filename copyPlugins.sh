#!/bin/bash
./gradlew build fatjar
find $PWD/quide-*-plugin/build/libs/ -type f | while read file ; do cp ${file} ~/.quide/plugins/ ; done
echo "Copied plugins into tinbo directory."