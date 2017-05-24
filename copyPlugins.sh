#!/bin/bash
./gradlew build fatjar
cp $PWD/quide-java-plugin/build/libs/quide-java-plugin-1.0.0.M2.jar ~/.quide/plugins/quide-java-plugin-1.0.0.M2.jar
cp $PWD/quide-detekt-plugin/build/libs/quide-detekt-plugin-1.0.0.M2.jar ~/.quide/plugins/quide-detekt-plugin-1.0.0.M2.jar
cp $PWD/quide-groovy-plugin/build/libs/quide-groovy-plugin-1.0.0.M2.jar ~/.quide/plugins/quide-groovy-plugin-1.0.0.M2.jar
echo "Copied plugins into .quide directory."
