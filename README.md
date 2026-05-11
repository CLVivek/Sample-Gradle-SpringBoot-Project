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

## Tests

### Run All Tests

```bash
./gradlew test
```

This runs all unit and integration tests and generates a test report.

### Unit Tests

Unit tests are isolated tests that verify individual components in isolation using mocks.

```bash
./gradlew unitTest
```

**What's tested:**
- `HelloControllerTest.java` - Tests the `HelloController` with mocked `ApiService`
- `ApiServiceTest.java` - Tests the `ApiService` business logic

**Test structure:**
- Uses `@WebMvcTest` to load only the web layer
- Uses `@MockBean` to mock dependencies
- Verifies HTTP status codes and response bodies
- Does not make real API calls

### Integration Tests

Integration tests verify the entire application flow, including real HTTP requests and service interactions.

```bash
./gradlew integrationTest
```

**What's tested:**
- `ApiIntegrationTest.java` - Tests the full flow from controller to service and external APIs

**Test structure:**
- Tests complete workflows
- Verifies component interactions
- May use test containers or mock external services

### View Test Reports

After running tests, view the HTML reports:

```
build/reports/tests/test/index.html        # All tests
build/reports/tests/unitTest/index.html    # Unit tests only
build/reports/tests/integrationTest/index.html # Integration tests only
```

Open these files in a browser to see:
- Test results and pass/fail statistics
- Individual test details and failures
- Execution times

## Clean

```bash
gradle clean
```
