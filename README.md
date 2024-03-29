# GroupAssigner
[![Java CI with Gradle](https://github.com/wudse20/GroupAssigner/actions/workflows/gradle.yml/badge.svg)](https://github.com/wudse20/GroupAssigner/actions/workflows/gradle.yml)<br>

A program that assigns people to different groups, based on their preferences.

## Download
The program can be downloaded by following this [link](https://www.skorup.se/download). This page is in swedish.

## Docs
Some technical docs over groupgeneration - in swedish. [Take me there!](https://www.skorup.se/how)

## Versions
Java: 20 <br>
Juniper JUnit (org.junit.jupiter:junit-jupiter:5.7.2)

## Build project

To build the project and get a runnable jar file you run the following command:
```powershell
./gradlew buildProject
```
This generates a jar file that is located in `app/build/libs/` and the file name is `app-xxx.jar`. To run the jar file you just run the following command:
```powershell
java -jar app-xxx.jar
```

## Compile and run

### Windows: <br>
gradle:
```powershell
./gradlew run
```
old way:
```powershell
javac -encoding utf-8 (Get-ChildItem -Name *.java -Path . -Recurse)
java se.skorup.main.Main
```

### Unix
gradle:
```bash
chmod +x ./gradlew # Only if you don't have the permissions on the file to run it.
./gradlew run
```
old way:
```bash
javac -encoding utf-8 $(find . -type f -name "*.java") && java se.skorup.main.Main
```
