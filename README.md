# CSE 110 Team 28 PantryPal Project

[![Java CI with Gradle](https://github.com/ucsd-cse110-fa23/cse-110-project-team-28/actions/workflows/gradle.yml/badge.svg)](https://github.com/ucsd-cse110-fa23/cse-110-project-team-28/actions/workflows/gradle.yml)

## Repository Structure

The repository is structured as follows:

<!-- tree -I "build|.gradle|.vscode|bin|gradle" -->

```
├── README.md (this file)
├── build.gradle (build configuration file)
├── gradlew (gradle wrapper)
├── gradlew.bat
├── settings.gradle (gradle settings file)
└── src (source code)
    ├── main
    │   ├── java
    │   │   ├── ChatGPT.java
    │   │   ├── RecipeApp.java (main class)
    │   │   ├── Whisper.java
    │   │   ├── client
    │   │   │   ├── App.java
    │   │   │   ├── Controller.java
    │   │   │   ├── Model.java
    │   │   │   └── View.java
    │   │   ├── multithreading
    │   │   │   └── RecordingAppFrame.java
    │   │   └── server
    │   │       ├── MyHandler.java
    │   │       ├── MyServer.java
    │   │       └── RequestHandler.java
    │   └── resources
    │       ├── css
    │       │   └── style.css
    │       ├── fonts
    │       │   ├── Roboto-Black.ttf
    │       │   └── Roboto-Medium.ttf
    │       └── images
    │           ├── add.png
    │           └── download.png
    └── test
        └── java
            └── RecipeAppTest.java (test class)
```

### About Gradle

Gradle is a build automation tool used to compile, test, and run our code. You can invoke Gradle using the `gradlew` script in the root directory of the project. This script will download the correct version of Gradle for you, so you don't need to install Gradle on your machine.

Alternatively, you can install Gradle on your machine and invoke it using the `gradle` command. Please note that this may cause issues if you are using a different version of Gradle than the one specified in `gradle/wrapper/gradle-wrapper.properties`.

## Running the App

To run the app, run the following command in the root directory of the project:

```bash
./gradlew run
```

## Running the Tests

To run the tests, run the following command in the root directory of the project:

```bash
./gradlew test
```

## About GitHub Actions

The configuration for GitHub Actions is stored in `.github/workflows/gradle.yml`. This workflow is triggered on every push to main and every pull request to main. The workflow builds the project with Gradle and runs the tests. If either of these steps fail, the workflow will fail and the push/pull request will be rejected.
