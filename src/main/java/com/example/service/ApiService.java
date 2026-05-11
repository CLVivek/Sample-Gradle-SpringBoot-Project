package com.example.service;

import com.example.model.ApiRequest;
import com.example.model.ApiResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {
    private final RestTemplate restTemplate;
    private static final String PUBLIC_API_URL = "https://jsonplaceholder.typicode.com/posts";

    public ApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Creates a post via public API (JSONPlaceholder)
     */
    public ApiResponse createPost(ApiRequest request) {
        try {
            ApiResponse response = restTemplate.postForObject(
                    PUBLIC_API_URL,
                    request,
                    ApiResponse.class
            );
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error calling public API: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a post from public API by ID
     */
    public ApiResponse getPostById(int postId) {
        try {
            String url = PUBLIC_API_URL + "/" + postId;
            ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching post from public API: " + e.getMessage(), e);
        }
    }
}
