# Spring Boot Application

A simple Spring Boot application built with Gradle.

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── Application.java
│   │   │   └── controller/
│   │   │       └── HelloController.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/
│           └── ApplicationTests.java
├── build.gradle
├── settings.gradle
└── README.md
```

## Prerequisites

- Java 17 or higher
- Gradle 8.4 or higher

## Build

```bash
gradle build
```

## Run

```bash
gradle bootRun
```

The application will start on `http://localhost:8080`

## Endpoints

- `GET /` - Returns greeting message
- `GET /api/hello` - Returns welcome message

## Test

```bash
gradle test
```

## Clean

```bash
gradle clean
```
