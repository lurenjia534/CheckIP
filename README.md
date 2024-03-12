# Check IP

A project that uses the IPinfo.io API with Jetpack compose desktop with the aim of creating a beautiful GUI and checking IP information

## Features

- Cross-platform support: Jetpack Compose Desktop is used to create cross-platform desktop applications, including but not limited to TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm, TargetFormat.AppImage, and TargetFormat.Exe.
- Support for separate jar package publishing: allow users to execute applications through jars.
- Simple modern UI interface
## Getting Started

These instructions will take a copy of the project and run it on the local machine for development and testing purposes.

The project uses an MIT license

### Prerequisites

Before you begin, you'll need to install the following software:

- OpenJDK 17 or later
- IntelliJ IDEA or any other IDE (optional, if you plan to build from source)

### Installation

How to install and set up your project:

```bash
git clone https://github.com/lurenjia534/CheckIP
```

```bash
cd CheckIP
```

##  Build the project, if applicable, will build a bundle for your current platform

```shell
./gradlew package 
```

### Build a jar distribution

```shell
./gradlew shadowjar
```

## Run jar

```shell
java -jar build/libs/yourproject.jar
```