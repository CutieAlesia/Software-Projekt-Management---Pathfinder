# Pathfinder Project

![TeamCity build status](http://185.194.217.213:8111/app/rest/builds/buildType:id:SpmPathfinder_Build/statusIcon.svg)
![](https://img.shields.io/github/last-commit/Kushurando/Software-Projekt-Management---Pathfinder/dev)
___
### Module Info
![](https://img.shields.io/badge/Java%20JDK-11-orange?style=for-the-badge&logo=java)

![](https://img.shields.io/badge/Build-Gradle-purple?style=for-the-badge&logo=Gradle)
![](https://img.shields.io/badge/Deployment-Docker-blue?style=for-the-badge&logo=Docker)

![](https://img.shields.io/badge/Package-com.spmfhb.pathfinder-green?style=for-the-badge)
___
### Statistics
![](https://img.shields.io/github/issues-raw/Kushurando/Software-Projekt-Management---Pathfinder?style=for-the-badge)
![](https://img.shields.io/github/issues-pr-raw/Kushurando/Software-Projekt-Management---Pathfinder?style=for-the-badge)
___
### Workflow

![](Documentation/Images/Workflow.png)
___
### Build instructions
**1. Clone the repository manually or by console**
```bash
git clone https://github.com/Kushurando/Software-Projekt-Management---Pathfinder
```
**2. Enter the project directory**
```bash
cd ~DIR~/Software-Projekt-Management---Pathfinder
```

**3. Run the gradle task to build a executable jar**

_Gradle 2.0 or higher required_
```bash
gradlew desktop:dist
```

**4.** You can find the finished jar in the desktop/build/libs folder
Run it by double click or by console:
```bash
java -jar desktop-1.0.jar
```
