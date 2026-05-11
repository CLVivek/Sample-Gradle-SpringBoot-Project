package com.example.controller;

import com.example.model.ApiRequest;
import com.example.model.ApiResponse;
import com.example.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Autowired
    private ApiService apiService;

    @GetMapping("/")
    public String hello() {
        return "Hello, Spring Boot!";
    }

    @GetMapping("/hello")
    public String helloApi() {
        return "Welcome to Spring Boot API";
    }

    /**
     * POST endpoint that accepts data and sends it to a public API
     * Request body: { "userId": 1, "title": "Sample Title", "body": "Sample Body" }
     */
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse> createPost(@RequestBody ApiRequest request) {
        try {
            ApiResponse response = apiService.createPost(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET endpoint that retrieves a post from public API by ID
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<ApiResponse> getPost(@PathVariable int id) {
        try {
            ApiResponse response = apiService.getPostById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
