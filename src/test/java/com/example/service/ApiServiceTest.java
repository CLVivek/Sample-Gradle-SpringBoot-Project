package com.example.service;

import com.example.model.ApiRequest;
import com.example.model.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiService Unit Tests")
class ApiServiceTest {

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    private ApiService apiService;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        apiService = new ApiService(restTemplateBuilder);
    }

    @Test
    @DisplayName("Should successfully create a post via API")
    void testCreatePostSuccess() {
        ApiRequest request = new ApiRequest(1, "Test Title", "Test Body");
        ApiResponse expectedResponse = new ApiResponse();
        expectedResponse.setId(101);
        expectedResponse.setUserId(1);
        expectedResponse.setTitle("Test Title");
        expectedResponse.setBody("Test Body");

        when(restTemplate.postForObject(
                eq("https://jsonplaceholder.typicode.com/posts"),
                any(ApiRequest.class),
                eq(ApiResponse.class)
        )).thenReturn(expectedResponse);

        ApiResponse result = apiService.createPost(request);

        assertNotNull(result);
        assertEquals(101, result.getId());
        assertEquals(1, result.getUserId());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Body", result.getBody());

        verify(restTemplate, times(1)).postForObject(
                anyString(),
                any(ApiRequest.class),
                any()
        );
    }

    @Test
    @DisplayName("Should throw exception when API call fails for POST")
    void testCreatePostFailure() {
        ApiRequest request = new ApiRequest(1, "Test Title", "Test Body");

        when(restTemplate.postForObject(
                anyString(),
                any(ApiRequest.class),
                any()
        )).thenThrow(new RuntimeException("Connection timeout"));

        assertThrows(RuntimeException.class, () -> apiService.createPost(request));
    }

    @Test
    @DisplayName("Should successfully retrieve a post by ID")
    void testGetPostByIdSuccess() {
        int postId = 1;
        ApiResponse expectedResponse = new ApiResponse();
        expectedResponse.setId(1);
        expectedResponse.setUserId(1);
        expectedResponse.setTitle("sunt aut facere repellat provident");
        expectedResponse.setBody("quia et suscipit");

        when(restTemplate.getForObject(
                "https://jsonplaceholder.typicode.com/posts/" + postId,
                ApiResponse.class
        )).thenReturn(expectedResponse);

        ApiResponse result = apiService.getPostById(postId);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getUserId());
        assertEquals("sunt aut facere repellat provident", result.getTitle());

        verify(restTemplate, times(1)).getForObject(
                "https://jsonplaceholder.typicode.com/posts/" + postId,
                ApiResponse.class
        );
    }

    @Test
    @DisplayName("Should throw exception when post not found")
    void testGetPostByIdNotFound() {
        int postId = 999;

        when(restTemplate.getForObject(
                anyString(),
                eq(ApiResponse.class)
        )).thenThrow(new RuntimeException("Post not found"));

        assertThrows(RuntimeException.class, () -> apiService.getPostById(postId));
    }

    @Test
    @DisplayName("Should use correct API URL for creating posts")
    void testCreatePostUsesCorrectUrl() {
        ApiRequest request = new ApiRequest(2, "Another Post", "Another Body");
        ApiResponse response = new ApiResponse();

        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(response);

        apiService.createPost(request);

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).postForObject(
                urlCaptor.capture(),
                any(ApiRequest.class),
                eq(ApiResponse.class)
        );
        assertEquals("https://jsonplaceholder.typicode.com/posts", urlCaptor.getValue());
    }

    @Test
    @DisplayName("Should handle valid post IDs")
    void testGetPostByIdWithValidId() {
        int[] validIds = {1, 5, 10, 50, 100};
        
        for (int id : validIds) {
            ApiResponse response = new ApiResponse();
            response.setId(id);
            
            when(restTemplate.getForObject(
                    "https://jsonplaceholder.typicode.com/posts/" + id,
                    ApiResponse.class
            )).thenReturn(response);

            ApiResponse result = apiService.getPostById(id);

            assertNotNull(result);
            assertEquals(id, result.getId());
        }
    }
}
