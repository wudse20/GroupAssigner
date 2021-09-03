# GroupAssigner
A program that assigns people to different groups, based on their preferences.

# Download
The program can be downloaded by following this [link](https://www.skorup.se/download). This page is in swedish.

# Versions
Java: 16.0.1 <br>
JUnit: 6.14.3 (maven: org.testng:testng:6.14.3)

# Compile and run
## Windows: <br>
```powershell
javac -encoding utf-8 (Get-ChildItem -Name *.java - Path . -Recurse)
java se.skorup.main.Main
```

## Unix
```bash
javac -encoding utf-8 $(find . -type f -name "*.java") && java se.skorup.main.Main
```
