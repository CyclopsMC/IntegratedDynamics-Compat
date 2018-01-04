## IntegratedDynamics-Compat

[![Build Status](https://travis-ci.org/CyclopsMC/IntegratedDynamics-Compat.svg?branch=master-1.11)](https://travis-ci.org/CyclopsMC/IntegratedDynamics-Compat)
[![Download](https://img.shields.io/maven-metadata/v/https/oss.jfrog.org/artifactory/simple/libs-release/org/cyclops/integrateddynamicscompat/IntegratedDynamicsCompat/maven-metadata.xml.svg) ](https://oss.jfrog.org/artifactory/simple/libs-release/org/cyclops/integrateddynamicscompat/IntegratedDynamicsCompat/)

[Integrated Dynamics](https://github.com/CyclopsMC/IntegratedDynamics) compatibility with other mods.
This mod is automatically packaged with [Integrated Dynamics](https://github.com/CyclopsMC/IntegratedDynamics).

[Development builds](https://oss.jfrog.org/artifactory/simple/libs-release/org/cyclops/integrateddynamicscompat/IntegratedDynamicsCompat/) are hosted by [JFrog Artifactory](https://www.jfrog.com/artifactory/).

### Contributing
* Before submitting a pull request containing a new feature, please discuss this first with one of the lead developers.
* When fixing an accepted bug, make sure to declare this in the issue so that no duplicate fixes exist.
* All code must comply to our coding conventions, be clean and must be well documented.

### Issues
* All bug reports and other issues are appreciated. If the issue is a crash, please include the FULL Forge log.
* Before submission, first check for duplicates, including already closed issues since those can then be re-opened.

### Branching Strategy

For every major Minecraft version, two branches exist:

* `master-{mc_version}`: Latest (potentially unstable) development.
* `release-{mc_version}`: Latest stable release for that Minecraft version. This is also tagged with all mod releases.

### Building and setting up a development environment

This mod uses [Project Lombok](http://projectlombok.org/) -- an annotation processor that allows us you to generate constructors, getters and setters using annotations -- to speed up recurring tasks and keep part of our codebase clean at the same time. Because of this it is advised that you install a plugin for your IDE that supports Project Lombok. Should you encounter any weird errors concerning missing getter or setter methods, it's probably because your code has not been processed by Project Lombok's processor. A list of Project Lombok plugins can be found [here](http://projectlombok.org/download.htm).

### License
All code and images are licenced under the [MIT License](https://github.com/CyclopsMC/IntegratedDynamics-Compat/blob/master-1.8/LICENSE.txt)
