# Quide - Quality-Centric Integrated Detection Environment

![detekt in action](img/quide.png "quide in action")

Umbrella project for static code analysis tools. 
Aims to unify the execution flow of different tools by providing injection points for detection, mapping, repository-mining and data storage.

Work in progress.


### Build

Quide uses gradle as the build tool.

Use `gradle build install fatjar shadow` to build all modules - including the executable platform jar -
 and package the plugins into fatjar's.
 

### Run

On the first `java -jar quide-platform.jar` run, a folder `.quide` is created in your home folder.
It contains a `plugins` folder where quide-plugins are put, a `configurations` folder where
configurations files for the plugins are hold and a `quide.properties` file with configuration properties for quide.

Official supported quide plugins are:

- _quide-java-plugin_ - code smell detection for Java (uses [SmartSmells](https://github.com/arturbosch/SmartSmells))
- _quide-detekt-plugin_ - code smell detection for Kotlin (uses [Detekt](https://github.com/arturbosch/detekt))
- _quide-vcs-plugin_ - git repository mining for evolutionary analysis (uses [vcs-reader](https://github.com/dkandalov/vcs-reader))