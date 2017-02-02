[![Build Status](https://travis-ci.org/anicos/npm-mvn-proxy.svg?branch=master)](https://travis-ci.org/anicos/npm-mvn-proxy)
[![codecov](https://codecov.io/gh/anicos/npm-mvn-proxy/branch/master/graph/badge.svg?maxAge=0)](https://codecov.io/gh/anicos/npm-mvn-proxy)


# Npm-mvn-proxy
The Npm-mvn-proxy provides easy way to publish and download javaScript projects using [Artifactory][] Maven repository.
This is simple proxy between [Npm][] console and [Artifactory][].

## How It Works


### Publishing from npm console to [Artifactory][]
![LinImage](https://cloud.githubusercontent.com/assets/5099712/21028813/1a2d8dde-bd97-11e6-997c-278758b2e351.png)


### Folder structure after publishing to [Artifactory][]
![LinImage](https://cloud.githubusercontent.com/assets/5099712/21028302/c3c29090-bd94-11e6-8720-2df0d4850a50.png)


### Download [Npm][] dependencies form [Artifactory][]  
![LinImage](https://cloud.githubusercontent.com/assets/5099712/21039531/deae9fc6-bdde-11e6-8ea7-86431a05c19a.png)


## Building project

### Prerequisites

[Git][] and [JDK 8 update 20 or later][JDK8 build]

Be sure that your `JAVA_HOME` environment variable points to the `jdk1.8.0` folder extracted from the JDK download.

### Check out sources

`git clone git@github.com:anicos/npm-mvn-proxy.git` 

### Compile and test; build jar

`./gradlew build`

... and discover more commands with `./gradlew tasks`. See also the [Gradle
build and release FAQ][].

## Running project

### Running from terminal

The Npm-mvn-proxy based on Spring Boot. To start proxy you just add file properties.
`./npm-mvn-proxy.jar --spring.config.location=application.properties`

## Minimal properties example

`maven.repo.url=http://maven.com/artifactory` - variable `maven.repo.url` is artifactory REST API address. For example for Artifactory Version 4.14.0 REST address is {domain}/artifactory

## All application properties example
`maven.repo.url` - the above-described.
####
`folder.for.npm.artifacts` - folder where are saved npm artifacts in Artifactory during publish command. Default value is `libs-release-local/npm`.
####
`npm.url` - Npm registry address. Default value is `http://registry.npmjs.org`.

... and discover more properties in [Spring boot][] documentation in section [Appendix A. Common application properties][].

## Configuration npm console

To use npm-mvn-proxy follow these steps:
### 1 Add [Artifactory][] user to npm console
`npm adduser --registry={npm-nmv-proxy address} --always-auth`

For example when you run application locally use `npm adduser --registry=http://localhost:8080/ --always-auth`.
After this command enter login and password to [Artifactory][]
### 2 Set address registry to npm-mvn-proxy
`npm config set registry {npm-nmv-proxy address}`
### 3 Set always-auth flag to true 
`npm config set always-auth true`

## Artifactory suported version
The npm-mvn-proxy should support all [Artifactory][] version which support rest API. 
This version of npm-mvn-proxy was tested with [Artifactory][] Version 4.14.0.

## Contributing
[Pull requests][] are welcome.

## License
The Npm-mvn-proxy is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
[Pull requests]: https://help.github.com/categories/collaborating-on-projects-using-issues-and-pull-requests/
[Git]: http://help.github.com/set-up-git-redirect
[JDK8 build]: http://www.oracle.com/technetwork/java/javase/downloads
[Gradle build and release FAQ]: https://github.com/spring-projects/spring-framework/wiki/Gradle-build-and-release-FAQ
[Artifactory]: https://www.jfrog.com/open-source/
[Npm]: https://www.npmjs.com/
[Spring boot]:https://projects.spring.io/spring-boot/
[Appendix A. Common application properties]:http://docs.spring.io/spring-boot/docs/1.4.x/reference/html/common-application-properties.html
