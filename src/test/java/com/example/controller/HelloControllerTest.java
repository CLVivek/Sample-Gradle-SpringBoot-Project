package com.example.controller;

import com.example.model.ApiRequest;
import com.example.model.ApiResponse;
import com.example.service.ApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloController.class)
@DisplayName("HelloController Unit Tests")
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApiService apiService;

    @Test
    @DisplayName("Should return hello message on GET /api/")
    void testGetRootEndpoint() throws Exception {
        mockMvc.perform(get("/api/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Spring Boot!"));
    }

    @Test
    @DisplayName("Should return welcome message on GET /api/hello")
    void testGetHelloEndpoint() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to Spring Boot API"));
    }

    @Test
    @DisplayName("Should create post and return 201 CREATED status")
    void testCreatePostSuccess() throws Exception {
        // Arrange
        ApiRequest request = new ApiRequest(1, "Test Title", "Test Body");
        ApiResponse response = new ApiResponse();
        response.setId(101);
        response.setUserId(1);
        response.setTitle("Test Title");
        response.setBody("Test Body");

        when(apiService.createPost(any(ApiRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1,\"title\":\"Test Title\",\"body\":\"Test Body\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.body").value("Test Body"));

        verify(apiService, times(1)).createPost(any(ApiRequest.class));
    }

    @Test
    @DisplayName("Should return 500 when post creation fails")
    void testCreatePostFailure() throws Exception {
        // Arrange
        when(apiService.createPost(any(ApiRequest.class)))
                .thenThrow(new RuntimeException("API Error"));

        // Act & Assert
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1,\"title\":\"Test Title\",\"body\":\"Test Body\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should retrieve post by ID and return 200 OK")
    void testGetPostByIdSuccess() throws Exception {
        // Arrange
        int postId = 1;
        ApiResponse response = new ApiResponse();
        response.setId(1);
        response.setUserId(1);
        response.setTitle("Test Post");
        response.setBody("Test Content");

        when(apiService.getPostById(eq(postId))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/posts/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.title").value("Test Post"))
                .andExpect(jsonPath("$.body").value("Test Content"));

        verify(apiService, times(1)).getPostById(postId);
    }

    @Test
    @DisplayName("Should return 404 when post not found")
    void testGetPostByIdNotFound() throws Exception {
        // Arrange
        int postId = 999;
        when(apiService.getPostById(eq(postId)))
                .thenThrow(new RuntimeException("Post not found"));

        // Act & Assert
        mockMvc.perform(get("/api/posts/{id}", postId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should accept valid JSON in POST request")
    void testCreatePostWithValidJson() throws Exception {
        // Arrange
        ApiResponse response = new ApiResponse();
        response.setId(50);
        response.setUserId(5);

        when(apiService.createPost(any(ApiRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":5,\"title\":\"Valid Title\",\"body\":\"Valid Body\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(50));
    }
}
