# Quide - Quality-Centric Inspection (and) Detection Environment

![quide in action](img/quide.png "quide in action")

Umbrella project for static code analysis tools. 
Aims to unify the execution flow of different tools by providing injection points for detection, mapping, repository-mining and data storage.

Work in progress.


### Build

Quide uses gradle as the build tool.

Use `gradle build install fatjar shadow` to build all modules - including the executable platform jar -
 and package the plugins into fatjar's.

### Modules

- `quide-specification` - the api, specifies how the platform and plugins should interact with each other.
- `quide-platform` - implements the platform-specification, knows how to handle plugins und provides an CLI for users
- `quide-xxx-plugin` - official supported plugins for quide
- `quide-shell` - interactive and extensible shell, allows to run __commands__ against projcts
- `codesmell-baseline-format` - xml format to note down false positives or baselines, where only new smells get displayed

### Run

On the first `java -jar quide-platform.jar` run, a folder `.quide` is created in your home folder.
It contains a `plugins` folder where quide-plugins are put, a `configurations` folder where
configurations files for the plugins are hold and a `quide.properties` file with configuration properties for quide.

Official supported quide plugins are:

- _quide-java-plugin_ - code smell detection for Java (uses [SmartSmells](https://github.com/arturbosch/SmartSmells))
- _quide-detekt-plugin_ - code smell detection for Kotlin (uses [Detekt](https://github.com/arturbosch/detekt))
- _quide-vcs-plugin_ - git repository mining for evolutionary analysis (uses [vcs-reader](https://github.com/dkandalov/vcs-reader))

### Shell

The quide-shell module provides an interactive approach to analyze source code while programming.

Commands:
- project [path/to/your/project] - specifies a project path which further commands are aware of
- run - executes the __quide-platform__ against specified _project_ path

##### Additional commands

TODO: I am planning to provide more commands to support developers on their daily work, eg. automatically applying licence
headers, formatting, providing metrics etc.

##### Write your own commands

The __quide-shell__ is highly extensible through __commands__. A command needs to implement
an interface quide-shell is aware of. This interface looks like this:

```kotlin
interface Command {
	val id: String
	fun run(line: String): String
}
```

Example of a simple command written in java:

```java
import io.gitlab.arturbosch.quide.shell.Command;

public class HelloWorld implements Command {

    // quide-shell will use this method for command lookup
    public String getId() {
        return "hello";
    }
        
    // this method is invoked with the user input.minus(getId())
    public String run(String line) {
        return "Hello World!";
    }	
    
}
```

TODO: Support kotlin, groovy scripts and jar files with commands
