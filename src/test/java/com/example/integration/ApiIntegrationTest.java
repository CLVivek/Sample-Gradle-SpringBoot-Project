package com.example.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("REST API Integration Tests")
class ApiIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Integration Test: GET / should return hello message")
    void testGetRootEndpoint() {
        given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body(equalTo("Hello, Spring Boot!"));
    }

    @Test
    @DisplayName("Integration Test: GET /hello should return welcome message")
    void testGetHelloEndpoint() {
        given()
                .when()
                .get("/hello")
                .then()
                .statusCode(200)
                .body(equalTo("Welcome to Spring Boot API"));
    }

    @Test
    @DisplayName("Integration Test: POST /posts should create a post successfully")
    void testCreatePostSuccessfully() {
        String requestBody = "{\n" +
                "    \"userId\": 1,\n" +
                "    \"title\": \"Integration Test Post\",\n" +
                "    \"body\": \"This is a test post for integration testing\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("userId", equalTo(1))
                .body("title", equalTo("Integration Test Post"))
                .body("body", equalTo("This is a test post for integration testing"));
    }

    @Test
    @DisplayName("Integration Test: POST /posts with multiple requests")
    void testCreateMultiplePosts() {
        for (int i = 1; i <= 3; i++) {
            String requestBody = String.format("{\n" +
                    "    \"userId\": %d,\n" +
                    "    \"title\": \"Post %d\",\n" +
                    "    \"body\": \"Body for post %d\"\n" +
                    "}", i, i, i);

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/posts")
                    .then()
                    .statusCode(201)
                    .body("userId", equalTo(i))
                    .body("title", equalTo("Post " + i));
        }
    }

    @Test
    @DisplayName("Integration Test: GET /posts/{id} should retrieve a post")
    void testGetPostById() {
        given()
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", notNullValue())
                .body("body", notNullValue());
    }

    @Test
    @DisplayName("Integration Test: GET /posts/{id} with various IDs")
    void testGetPostByDifferentIds() {
        int[] postIds = {1, 5, 10};

        for (int id : postIds) {
            given()
                    .when()
                    .get("/posts/" + id)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(id))
                    .body("userId", notNullValue())
                    .body("title", notNullValue());
        }
    }

    @Test
    @DisplayName("Integration Test: POST /posts response includes all required fields")
    void testCreatePostResponseStructure() {
        String requestBody = "{\n" +
                "    \"userId\": 2,\n" +
                "    \"title\": \"Complete Response Test\",\n" +
                "    \"body\": \"Verify response structure\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("$", hasKey("id"))
                .body("$", hasKey("userId"))
                .body("$", hasKey("title"))
                .body("$", hasKey("body"));
    }

    @Test
    @DisplayName("Integration Test: Verify response headers")
    void testResponseHeaders() {
        given()
                .when()
                .get("/hello")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString("text/plain"))
                .header("Content-Length", notNullValue());
    }

    @Test
    @DisplayName("Integration Test: POST /posts with different data types")
    void testCreatePostWithVariousData() {
        // Test with different userId values
        for (int userId : new int[]{1, 5, 10, 100}) {
            String requestBody = String.format("{\n" +
                    "    \"userId\": %d,\n" +
                    "    \"title\": \"Test for user %d\",\n" +
                    "    \"body\": \"Content for user %d\"\n" +
                    "}", userId, userId, userId);

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/posts")
                    .then()
                    .statusCode(201)
                    .body("userId", equalTo(userId));
        }
    }

    @Test
    @DisplayName("Integration Test: GET /posts/{id} with high ID values")
    void testGetPostWithHighId() {
        given()
                .when()
                .get("/posts/100")
                .then()
                .statusCode(200)
                .body("id", equalTo(100));
    }

    @Test
    @DisplayName("Integration Test: Verify Content-Type in POST response")
    void testPostResponseContentType() {
        String requestBody = "{\n" +
                "    \"userId\": 1,\n" +
                "    \"title\": \"Content Type Test\",\n" +
                "    \"body\": \"Verify response content type\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("Integration Test: Chain multiple REST calls")
    void testChainedRestCalls() {
        // Create a post
        String requestBody = "{\n" +
                "    \"userId\": 3,\n" +
                "    \"title\": \"Chained Test Post\",\n" +
                "    \"body\": \"Testing chained calls\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201);

        // Retrieve existing post
        given()
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("userId", equalTo(1));
    }
}
